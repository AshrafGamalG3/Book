package com.example.bookapp.ui.admin.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.bookapp.App.Companion.formatDate
import com.example.bookapp.App.Companion.getPdfPageCount
import com.example.bookapp.App.Companion.getPdfSize
import com.example.bookapp.App.Companion.loadPdfFromUrlSinglePage
import com.example.bookapp.network.DownloadService
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentShowBookBinding
import com.example.bookapp.ui.admin.data.model.BookModel
import com.example.bookapp.ui.auth.presentation.AuthViewModel
import com.example.bookapp.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowBookFragment : Fragment() {
    private var _binding: FragmentShowBookBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE_WRITE_STORAGE = 1
    private var favorite: Boolean? = null
    private val viewModel: AdminViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_show_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowBookBinding.bind(view)
        val book = ShowBookFragmentArgs.fromBundle(requireArguments()).book
        viewModel.isFavorite(book)
        loadData(book)
        onClick(book)
        observeFavoriteStatus()
        authViewModel.getUserById(FirebaseAuth.getInstance().currentUser!!.uid)
        observeUser()
    }

    private fun loadData(bookModel: BookModel) {
        binding.bookName.text = bookModel.title
        binding.categoryTv.text = bookModel.categoryName
        binding.dateTv.text = formatDate(bookModel.timestamp)
        binding.viewTv.text = bookModel.bookViews.toString()
        binding.downloadTv.text = bookModel.bookDownloads.toString()
        binding.titleBook.text = bookModel.title
        getPdfSize(bookModel.pdfUrl) { size ->
            binding.sizeTv.text = size
        }
        loadPdfFromUrlSinglePage(bookModel.pdfUrl, binding.viewPage, binding.progressBar)

        getPdfPageCount(bookModel.pdfUrl) {
            binding.pageTv.text = it.toString()
        }
        binding.bookDescription.text = bookModel.description
    }

    private fun onClick(bookModel: BookModel) {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.readBook.setOnClickListener {
            val action =
                ShowBookFragmentDirections.actionShowBookFragmentToPdfBookFragment(bookModel)
            Log.e("TAG", "onClick: read book ")
            findNavController().navigate(action)
            viewModel.updateBook(bookModel.copy(bookViews = bookModel.bookViews + 1))
        }
        binding.downloadBook.setOnClickListener {
            checkStoragePermission()
            viewModel.updateBook(bookModel.copy(bookDownloads = bookModel.bookDownloads + 1))
        }
        binding.favoriteBook.setOnClickListener {
            if (favorite == true) {
                viewModel.deleteFavorite(bookModel)
                val drawable = resources.getDrawable(R.drawable.baseline_favorite_border_24, null)
                binding.favoriteBook.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
                favorite = false
            } else {
                viewModel.addFavorite(bookModel)
                val drawable = resources.getDrawable(R.drawable.baseline_favorite_24, null)
                binding.favoriteBook.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
                favorite = true
            }
        }

    }

    private fun observeFavoriteStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFavorite.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val isFavorite = resource.data ?: false
                            favorite = isFavorite
                            val drawable = if (isFavorite) {
                                resources.getDrawable(R.drawable.baseline_favorite_24, null)
                            } else {
                                resources.getDrawable(R.drawable.baseline_favorite_border_24, null)
                            }
                            binding.favoriteBook.setCompoundDrawablesWithIntrinsicBounds(
                                null, drawable, null, null
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun observeUser() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.user.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val user = resource.data!!
                            if (user.type == "admin") {
                                binding.favoriteBook.visibility = View.GONE
                            } else {
                                binding.favoriteBook.visibility = View.VISIBLE
                            }
                        }

                        else -> Unit
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownloadService()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied to write to storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun startDownloadService() {
        val bookModel = ShowBookFragmentArgs.fromBundle(requireArguments()).book
        val intent = Intent(requireContext(), DownloadService::class.java).apply {
            putExtra("PDF_URL", bookModel.pdfUrl)
            putExtra("FILE_NAME", bookModel.title)
        }
        requireContext().startService(intent)
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                startDownloadService()
            } else {

                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        } else if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_STORAGE
            )
        } else {

            startDownloadService()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
