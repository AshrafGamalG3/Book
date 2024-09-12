package com.example.bookapp.ui.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.bookapp.App.Companion.loadBookFromUrl

import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentPdfBookBinding
import com.example.bookapp.ui.home.data.model.BookModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PdfBookFragment : Fragment() {
    private var _binding: FragmentPdfBookBinding? = null
    private val binding get() = _binding!!


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
        return inflater.inflate(R.layout.fragment_pdf_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPdfBookBinding.bind(view)
        val book = PdfBookFragmentArgs.fromBundle(requireArguments()).book
        loadData(book)
        onClick()
    }

    private fun loadData(bookModel: BookModel) {
        loadBookFromUrl(
            bookModel.pdfUrl,
            binding.pdfView,
            binding.progressBar,
            binding.numberOfPages
        )
    }

    private fun onClick() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}