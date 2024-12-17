package com.example.bookapp.ui.auth.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWelcomeBinding.bind(view)
        observeData()
        onCheckboxClicked()


    }

    private fun observeData() {


        binding.apply {
            loginBtn.setOnClickListener {
                if (type == null) {
                    Toast.makeText(requireContext(), "Please select a type", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val action = type?.let { it1 ->
                        WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment(
                            it1
                        )
                    }
                    findNavController().navigate(action!!)
                }

            }


                registerBtn.setOnClickListener {
                    if (type == null) {
                        Toast.makeText(requireContext(), "Please select a type", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val action = type?.let { it1 ->
                            WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment(
                                it1
                            )
                        }
                        findNavController().navigate(action!!)
                    }



            }

        }
    }

    private fun onCheckboxClicked() {
        binding.normalUser.setOnClickListener {
            binding.librarian.isChecked = false
            binding.normalUser.isChecked = true
            type = "Normal User"

        }
        binding.librarian.setOnClickListener {
            binding.normalUser.isChecked = false
            binding.librarian.isChecked = true
            type = "Librarian"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}