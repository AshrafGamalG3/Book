package com.example.bookapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentSplashBinding
import com.example.bookapp.ui.splash.SplashViewModel.Companion.ADMIN_FRAGMENT
import com.example.bookapp.ui.splash.SplashViewModel.Companion.LOGIN_FRAGMENT
import com.example.bookapp.ui.splash.SplashViewModel.Companion.USER_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Use the binding to inflate the layout
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigate.collectLatest { navigationTarget ->
                    when (navigationTarget) {
                        ADMIN_FRAGMENT -> findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                        LOGIN_FRAGMENT -> findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
                        USER_FRAGMENT -> findNavController().navigate(R.id.action_splashFragment_to_userFragment)
                        else -> Unit
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding
    }
}
