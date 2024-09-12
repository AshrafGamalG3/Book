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
import com.example.bookapp.databinding.FragmentEditBookBinding
import com.example.bookapp.ui.home.data.model.BookModel
import com.example.bookapp.ui.home.data.model.CategoryModel
import com.example.bookapp.utils.AlertDialogCategory
import com.example.bookapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditBookFragment : Fragment() {
    private var _binding: FragmentEditBookBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by viewModels()
    private var listCategory: List<CategoryModel> = emptyList()
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
        return inflater.inflate(R.layout.fragment_edit_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditBookBinding.bind(view)
        val bookModel = EditBookFragmentArgs.fromBundle(requireArguments()).book
        onClick(bookModel)
        setBook(bookModel)
        observeCategories()
        getCategories()
        observeUpdateBook()


    }

    private fun onClick(bookModel: BookModel) {
        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.bookCategory.setOnClickListener {
            val dialog = AlertDialogCategory(requireContext(), listCategory) { selectedCategory ->
                binding.bookCategory.text = selectedCategory
            }
            dialog.show()
        }

        binding.buttonUpdate.setOnClickListener {
            getUpdatedData(bookModel)
        }
    }

    private fun setBook(bookModel: BookModel) {
        binding.edBookTitle.setText(bookModel.title)
        binding.edBookDescription.setText(bookModel.description)
        binding.bookCategory.text = bookModel.categoryName
    }

    private fun getUpdatedData(bookModel: BookModel) {

        val title = binding.edBookTitle.text.toString()
        val description = binding.edBookDescription.text.toString()
        val category = binding.bookCategory.text.toString()
        if (title == bookModel.title && description == bookModel.description && category == bookModel.categoryName) {
            Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show()
            return
        }

        if (category != bookModel.categoryName) {
            Toast.makeText(
                requireContext(),
                "Your change Category to $category",
                Toast.LENGTH_SHORT
            ).show()
        }
        val updatedBookModel = BookModel(
            bookModel.favorite,
            bookModel.bookViews,
            bookModel.bookDownloads,
            category,
            description,
            bookModel.timestamp.toString(),
            bookModel.pdfUrl,
            title,
            bookModel.userId,
            bookModel.timestamp
        )
        updateBook(updatedBookModel)

    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {
                                listCategory = it
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun observeUpdateBook() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateBook.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.buttonUpdate.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonUpdate.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Book updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigateUp()
                        }

                        is Resource.Error -> {
                            binding.buttonUpdate.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun updateBook(bookModel: BookModel) {
        viewModel.updateBook(bookModel)
    }


    private fun getCategories() {
        viewModel.getCategories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}