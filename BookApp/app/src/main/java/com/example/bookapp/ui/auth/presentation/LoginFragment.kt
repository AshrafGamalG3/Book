package com.example.bookapp.ui.auth.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentLoginBinding
import com.example.bookapp.utils.RegisterValidation
import com.example.bookapp.utils.Resource
import com.example.bookapp.utils.setupBottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        onClick()
        loginObserver()
    }


    private fun onClick() {

        binding.forgotPassword.setOnClickListener {
            setupBottomSheetDialog {
                viewModel.resetPassword(it)
            }
        }

        binding.notHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            buttonLogin.setOnClickListener {
                val email = edTextEmail.text.toString().trim()
                val password = edPasswordLogin.text.toString().trim()
                loginWithEmailAndPassword(email, password)
            }
        }
    }

    private fun loginWithEmailAndPassword(email: String, password: String){
        viewModel.loginWithEmailAndPassword(email, password)
    }

    private fun loginObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.buttonLogin.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonLogin.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }

                        is Resource.Error -> {
                            binding.buttonLogin.revertAnimation()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                        else -> Unit


                    }
                }


            }
        }


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect {
                    if (it.emailValidation is RegisterValidation.Failed) {
                        binding.edTextEmail.apply {
                            requestFocus()
                            error = it.emailValidation.message
                        }
                    }
                    if (it.passwordValidation is RegisterValidation.Failed) {
                        binding.edPasswordLogin.apply {
                            requestFocus()
                            error = it.passwordValidation.message
                        }

                    }
                    if (it.passwordValidation is RegisterValidation.Success) {
                        binding.edPasswordLogin.apply {
                            requestFocus()

                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.resetPassword.collect{
                    when(it){
                        is Resource.Loading ->{

                        }
                        is Resource.Success ->{
                            Toast.makeText(requireContext(), "Reset link sent to your email", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Error ->{
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit

                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}