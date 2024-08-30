package com.example.bookapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentSplashBinding
import com.example.bookapp.ui.home.presentation.HomeFragment
import com.example.bookapp.ui.splash.SplashViewModel.Companion.HOME_FRAGMENT
import com.example.bookapp.ui.splash.SplashViewModel.Companion.LOGIN_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSplashBinding.bind(view)
        observeData()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            viewModel.startSplash()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigate.collectLatest {
                    when(it){
                        HOME_FRAGMENT -> {
                           findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                        }

                        LOGIN_FRAGMENT ->{
                            findNavController().navigate(it)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

}