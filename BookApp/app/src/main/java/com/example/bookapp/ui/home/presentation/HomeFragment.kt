package com.example.bookapp.ui.home.presentation

import android.os.Bundle

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.App.Companion.displayUserEmail
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentHomeBinding
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.presentation.adapter.CategoryAdapter
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.delay

import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapterCategory = CategoryAdapter()
    private val viewModel: AppViewModel by viewModels()
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var allCategories: List<CategoryModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.userEmail.text=displayUserEmail()
        setupRecyclerView()
        onClick()
        observeCategory()
        getCategories()

        setupSearch()
    }

    private fun onClick() {
        binding.addCategory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addCategoryFragment)
        }
        binding.addPdfCategory.setOnClickListener {
        findNavController().navigate(R.id.action_homeFragment_to_pdfFragment)
        }

        adapterCategory.onItemClickListener=object :CategoryAdapter.OnDeleteClickListener{
            override fun onDeleteClick(category: CategoryModel) {
                deleteCategoryFromFirebase(category)
                deleteCategoryFromRecyclerView(category)


            }

        }

        adapterCategory.onItemClickListener2=object :CategoryAdapter.OnItemClickListener{
            override fun onItemClick(category: String) {
                passCategoryName(category)
            }

        }
        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

        }

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun passCategoryName(category: String){
        val action = HomeFragmentDirections.actionHomeFragmentToCategoryDetailsFragment(category)
        findNavController().navigate(action)

    }

    private fun setupRecyclerView() {
        binding.categoryList.adapter = adapterCategory

    }

    private fun getCategories() {
        viewModel.getCategories()
    }

    private fun observeCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.categories.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapterCategory.categories = resource.data ?: emptyList()
                        allCategories = resource.data ?: emptyList()
                        Log.e("HomeFragment", "Data loaded: ${resource.data}")
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomeFragment", "Error loading data: ${resource.message}")
                    }

                    else -> Unit
                }
            }
            }
        }

    }

    private fun searchCategories(query: String) {
        val lowerCaseQuery = query.lowercase()
        val filteredList = if (lowerCaseQuery.isEmpty()) {
            allCategories
        } else {
            allCategories.filter { it.categoryName.lowercase().contains(lowerCaseQuery) }
        }
        adapterCategory.updateCategories(filteredList, lowerCaseQuery)
    }


    private fun setupSearch() {
        binding.searchCategory.addTextChangedListener {


            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    delay(500)
                    searchCategories(it.toString())
                }

            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun deleteCategoryFromFirebase(category: CategoryModel) {
        viewModel.deleteCategory(category)
    }
    private fun deleteCategoryFromRecyclerView(category: CategoryModel){
        val currentList = adapterCategory.categories.toMutableList()
        currentList.remove(category)
        adapterCategory.categories = currentList
    }
}
