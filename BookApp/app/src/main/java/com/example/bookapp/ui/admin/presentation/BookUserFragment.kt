package com.example.bookapp.ui.admin.presentation

import android.os.Bundle
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
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentBookUserBinding
import com.example.bookapp.ui.admin.data.model.BookModel
import com.example.bookapp.ui.admin.presentation.adapter.BookAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookUserFragment : Fragment() {
    private var _binding: FragmentBookUserBinding? = null
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

        return inflater.inflate(R.layout.fragment_book_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding= FragmentBookUserBinding.bind(view)
        val categoryName = arguments?.getString("categoryName")
        getBooksByCategory(categoryName!!)
        setupRecyclerView()
        allBooksObserver()
        setupSearch()
        setupSwipeRefreshLayout()


    }


    private fun getBooksByCategory(categoryName: String) {
        viewModel.getAllBooksByCategory(categoryName)
    }
    private fun setupRecyclerView() {
       bookAdapter.apply {
            onItemClickListener2 = object : BookAdapter.onItemClickBookListener {
                override fun onItemClick(bookModel: BookModel) {
                    val action = UserFragmentDirections.actionUserFragmentToShowBookFragment2(bookModel)
                    findNavController().navigate(action)
                }
            }
        }
        binding.booksRecyclerView.adapter = bookAdapter
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getBooksByCategory(arguments?.getString("categoryName")!!)
        }
    }
    private fun allBooksObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllBooksByCategory.observe(viewLifecycleOwner) {

                    binding.swipeRefreshLayout.isRefreshing = false

                    bookAdapter.books = it.data ?: emptyList()
                    if (it.data?.size == 0) {
                        binding.notBooks.visibility = View.VISIBLE
                    } else {
                        binding.notBooks.visibility = View.GONE
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
        _binding=null
    }



}