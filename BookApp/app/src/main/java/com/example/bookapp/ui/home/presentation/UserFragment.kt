package com.example.bookapp.ui.home.presentation

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
import com.example.bookapp.App.Companion.displayUserEmail
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentUserBinding
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.ui.home.presentation.adapter.BooksPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    private var pagerAdapter: BooksPagerAdapter? = null
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userEmail.text = displayUserEmail()


        getAllCategory()
        allCategoriesObserver()
        onClick()
    }

    private fun onClick(){
        binding.logout.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            firebaseAuth.signOut()


        }
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_profileFragment)
        }
    }

    private fun getAllCategory() {
        viewModel.getCategories()
    }

    private fun allCategoriesObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.observe(viewLifecycleOwner) {
                    it.data?.let { it1 -> setupTabLayout(it1, binding.tabLayout) }
                }
            }
        }
    }

    private fun setupTabLayout(categories: List<CategoryModel>, tabLayout: TabLayout) {
        pagerAdapter = BooksPagerAdapter(this, categories)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categories[position].categoryName
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
