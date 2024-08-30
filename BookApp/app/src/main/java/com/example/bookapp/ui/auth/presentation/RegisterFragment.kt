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
import com.example.bookapp.databinding.FragmentRegisterBinding
import com.example.bookapp.ui.auth.data.model.User
import com.example.bookapp.utils.RegisterValidation
import com.example.bookapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
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

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)
        onClick()
        registerObserver()
    }


    private fun onClick() {
        binding.apply {
            buttonRegister.setOnClickListener {
                val name = edTextName.text.toString().trim()
                val email = edTextEmail.text.toString().trim()
                val password = edPasswordLogin.text.toString().trim()
                val confirmPassword = edPasswordLoginConfirm.text.toString().trim()

                val user = User(name, email, password, confirmPassword, "")

                createAccountWithEmailAndPass(user)
            }
        }

    }

    private fun createAccountWithEmailAndPass(user: User)  {
        viewModel.createAccountWithEmailAndPass(user)
    }


    private fun registerObserver() {

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
                    if (it.nameValidation is RegisterValidation.Failed) {
                        binding.edTextName.apply {
                            requestFocus()
                            error = it.nameValidation.message
                        }
                    }
                    if (it.confirmPasswordValidation is RegisterValidation.Failed) {
                        binding.edPasswordLoginConfirm.apply {
                            requestFocus()
                            error = it.confirmPasswordValidation.message
                        }
                    }

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.buttonRegister.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonRegister.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Your account has been created, please login",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }

                        is Resource.Error -> {
                            binding.buttonRegister.revertAnimation()
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








