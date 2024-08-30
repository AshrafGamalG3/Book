package com.example.bookapp.ui.home.presentation

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
import com.example.bookapp.databinding.FragmentAddCategoryBinding
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCategoryFragment : Fragment() {
    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         _binding = FragmentAddCategoryBinding.bind(view)
        onClick()
        observeAddCategory()

    }

    private fun onClick() {
        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonAddCategory.setOnClickListener {
            val categoryTitle = binding.edCategoryTitle.text.toString().trim()
            val category = CategoryModel(categoryTitle, FirebaseAuth.getInstance().uid.toString(), System.currentTimeMillis())
            addCategory(category)
        }
    }

    private fun addCategory(categoryModel: CategoryModel) {
        viewModel.addCategory(categoryModel)

    }

    private fun observeAddCategory() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addCategory.collect {
                    when(it){
                        is Resource.Loading -> {
                            binding.buttonAddCategory.startAnimation()
                        }
                        is Resource.Success -> {
                            binding.buttonAddCategory.revertAnimation()
                            Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                        is Resource.Error -> {
                            binding.buttonAddCategory.revertAnimation()
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