package com.example.bookapp.ui.admin.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentCategoryDetailsBinding
import com.example.bookapp.ui.admin.data.model.BookModel

import com.example.bookapp.ui.admin.presentation.adapter.BookAdapter

import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailsFragment : Fragment() {
    private var _binding: FragmentCategoryDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by viewModels()
    private val bookAdapter = BookAdapter()
    private var allBooks: List<BookModel> = emptyList()


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
        return inflater.inflate(R.layout.fragment_category_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryDetailsBinding.bind(view)
        val categoryName = CategoryDetailsFragmentArgs.fromBundle(requireArguments()).categoryName
        binding.categoryName.text = categoryName
        getBooksByCategory(categoryName)
        observeBooksByCategory()
        setAdapter()
        setupSearch()
        onClick()

    }

    private fun onClick() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        bookAdapter.onItemClickListener = object : BookAdapter.OnItemClickListener {
            override fun onItemClick(bookModel: BookModel) {
                showDialog(bookModel)
            }


        }
        bookAdapter.onItemClickListener2=object :BookAdapter.onItemClickBookListener{
            override fun onItemClick(bookModel: BookModel) {
                val action=CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToShowBookFragment(bookModel)
                findNavController().navigate(action)
            }

        }
    }

    private fun showDialog(bookModel: BookModel) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()
        val editOption = dialog.findViewById<TextView>(R.id.editOption)
        val deleteOption = dialog.findViewById<TextView>(R.id.deleteOption)
        editOption?.setOnClickListener {
           val action = CategoryDetailsFragmentDirections.actionCategoryDetailsFragmentToEditBookFragment(bookModel)
            findNavController().navigate(action)
            dialog.dismiss()
        }
        deleteOption?.setOnClickListener {
            deleteCategoryFromFirebase(bookModel)
            deleteCategoryFromRecyclerView(bookModel)
            Toast.makeText(requireContext(), "Book deleted", Toast.LENGTH_SHORT).show()
            getBooksByCategory(binding.categoryName.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteCategoryFromFirebase(bookModel: BookModel) {
        viewModel.deleteBook(bookModel)
    }

    private fun deleteCategoryFromRecyclerView(bookModel: BookModel) {
        val currentList = bookAdapter.books.toMutableList()
        currentList.remove(bookModel)
        bookAdapter.books = currentList
    }

    private fun setAdapter() {
        binding.books.adapter = bookAdapter
    }

    private fun getBooksByCategory(categoryName: String) {
        viewModel.getAllBooksByCategory(categoryName)
    }

    private fun observeBooksByCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllBooksByCategory.observe(viewLifecycleOwner) {
                    bookAdapter.books = it.data ?: emptyList()
                    if (it.data?.size==0){
                     binding.notBooks.visibility=View.VISIBLE
                    }
                    allBooks = it.data ?: emptyList()
                }
            }

        }
    }

    private fun searchBooks(query: String) {
        val lowerCaseQuery = query.lowercase()
        val filteredList = if (lowerCaseQuery.isEmpty()) {
            allBooks
        } else {
            allBooks.filter { it.title.lowercase().contains(lowerCaseQuery) }
        }
        bookAdapter.updateCategories(filteredList, lowerCaseQuery)
    }

    private fun setupSearch() {
        binding.bookSearch.addTextChangedListener {

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    delay(500)
                    searchBooks(it.toString())
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}