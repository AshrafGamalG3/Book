package com.example.bookapp.ui.admin.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentPdfBinding
import com.example.bookapp.ui.admin.data.model.BookModel
import com.example.bookapp.ui.admin.data.model.CategoryModel
import com.example.bookapp.utils.AlertDialogCategory
import com.example.bookapp.utils.BookValidationResult
import com.example.bookapp.utils.Resource
import com.example.bookapp.utils.ValidationResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PdfFragment : Fragment() {
    private var _binding: FragmentPdfBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdminViewModel by viewModels()
    private var pdfUri: Uri? = null
    private var listCategory: List<CategoryModel> = emptyList()

    // Registering the result launcher for picking a PDF
    private val pickPdfLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            pdfUri = uri
            Log.e("TAG", "PDF Uri selected: $pdfUri")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPdfBinding.bind(view)
        setupClickListeners()
        observeCategories()
        observeBookAddition()
        observeBookValidation()
        getCategories()
    }

    private fun setupClickListeners() {
        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.bookCategory.setOnClickListener {

            val dialog = AlertDialogCategory(requireContext(), listCategory) { selectedCategory ->
                binding.bookCategory.text = selectedCategory
            }
            dialog.show()

        }
        binding.addBook.setOnClickListener {
            openPdfChooser()
        }
        binding.uploadButton.setOnClickListener {
            if (pdfUri != null) {
                val title = binding.edBookTitle.text.toString()
                val description = binding.edBookDescription.text.toString()
                val category = binding.bookCategory.text.toString()
                val timestamp = System.currentTimeMillis()
                val bookModel = BookModel(
                    false,
                    0,
                    0,
                    category,
                    description,
                    timestamp.toString(),
                    pdfUri.toString(),
                    title,
                    FirebaseAuth.getInstance().currentUser?.uid ?: "",
                    timestamp
                )
                addBookToFirebase(bookModel)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select a PDF file",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }

    private fun addBookToFirebase(bookModel: BookModel) {
        viewModel.addBook(bookModel)
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

    private fun observeBookAddition() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addBook.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.uploadButton.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.uploadButton.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Book added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigateUp()
                        }

                        is Resource.Error -> {
                            binding.uploadButton.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "Failed to add book",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun observeBookValidation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validBooks.collect { validationResult ->
                    handleValidationErrors(validationResult)
                }
            }
        }
    }

    private fun handleValidationErrors(validationResult: BookValidationResult) {
        binding.edBookTitle.error = if (validationResult.titleResult is ValidationResult.Failed) {
            validationResult.titleResult.message
        } else {
            null
        }

        binding.edBookDescription.error =
            if (validationResult.descriptionResult is ValidationResult.Failed) {
                validationResult.descriptionResult.message
            } else {
                null
            }

        binding.bookCategory.error =
            if (validationResult.categoryResult is ValidationResult.Failed) {
                validationResult.categoryResult.message
            } else {
                null
            }

        if (validationResult.pdfUriResult is ValidationResult.Failed) {
            Toast.makeText(
                requireContext(),
                validationResult.pdfUriResult.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPdfChooser() {
        pickPdfLauncher.launch("application/pdf")
    }

    private fun getCategories() {
        viewModel.getCategories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
