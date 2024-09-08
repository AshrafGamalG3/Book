package com.example.bookapp.ui.admin.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bookapp.ui.admin.data.model.CategoryModel
import com.example.bookapp.ui.admin.presentation.BookUserFragment
import javax.inject.Inject

class BooksPagerAdapter @Inject constructor(
    fragment: Fragment,
    private val categories: List<CategoryModel>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
      return  categories.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = BookUserFragment()
        fragment.arguments = Bundle().apply {
            putString("categoryName", categories[position].categoryName)
        }
        return fragment
    }
}