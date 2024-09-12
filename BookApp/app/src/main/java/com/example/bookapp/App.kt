package com.example.bookapp

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.bookapp.utils.Constants.MAX_BYTES_PDF
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class App @Inject constructor() : Application() {


    companion object {
        private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

        fun getPdfSize(pdfUrl: String, callback: (String) -> Unit) {
            val ref = FirebaseStorage.getInstance().getReference(pdfUrl)
            ref.metadata.addOnSuccessListener { metadata ->
                val size = metadata.sizeBytes
                val sizeInKb = size / 1024
                val sizeInMb = sizeInKb / 1024

                val sizeString = when {
                    sizeInMb >= 1 -> "${sizeInMb.toInt()} MB"
                    sizeInKb >= 1 -> "${sizeInKb.toInt()} KB"
                    else -> "${size} B"
                }
                callback(sizeString)
            }.addOnFailureListener {
                callback("Failed to load")
            }
        }

         fun displayUserEmail():String {
            val userEmail = firebaseAuth.currentUser?.email
            return userEmail ?: "No email available"

        }

        fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }


        fun loadPdfFromUrlSinglePage(
            pdfUrl: String,
            imageView: ImageView,
            progressBar: ProgressBar,
        ) {
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val ref = FirebaseStorage.getInstance().getReference(pdfUrl)
                    val bytes = ref.getBytes(Long.MAX_VALUE).await()

                    val tempFile = File.createTempFile("tempPdf", ".pdf").apply {
                        outputStream().use { it.write(bytes) }
                    }

                    val pdfFile =
                        ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                    val pdfRenderer = PdfRenderer(pdfFile)

                    val bitmap = if (pdfRenderer.pageCount > 0) {
                        val pdfPage = pdfRenderer.openPage(0)
                        Bitmap.createBitmap(pdfPage.width, pdfPage.height, Bitmap.Config.ARGB_8888)
                            .apply {
                                pdfPage.render(
                                    this,
                                    null,
                                    null,
                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                )
                                pdfPage.close()
                            }
                    } else null

                    withContext(Dispatchers.Main) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap)
                        } else {
                            imageView.setImageResource(R.drawable.ic_pdf_gray)
                        }
                        progressBar.visibility = View.GONE
                    }

                    pdfRenderer.close()
                    pdfFile.close()
                    tempFile.delete()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        imageView.setImageResource(R.drawable.ic_pdf_gray)
                    }
                    e.printStackTrace()
                }
            }
        }

        fun getPdfPageCount(
            pdfUrl: String,
            callback: (Int) -> Unit,
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val ref = FirebaseStorage.getInstance().getReference(pdfUrl)
                    val bytes = ref.getBytes(Long.MAX_VALUE).await()


                    val tempFile = File.createTempFile("tempPdf", ".pdf").apply {
                        outputStream().use { it.write(bytes) }
                    }


                    val pdfFile =
                        ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                    val pdfRenderer = PdfRenderer(pdfFile)


                    val pageCount = pdfRenderer.pageCount


                    pdfRenderer.close()
                    pdfFile.close()
                    tempFile.delete()


                    withContext(Dispatchers.Main) {
                        callback(pageCount)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        callback(-1)
                    }
                }
            }
        }

        fun loadBookFromUrl(
            pdfUrl: String,
            pdfView: PDFView,
            progressBar: ProgressBar,
            textView: TextView,
        ) {
            val ref = FirebaseStorage.getInstance().getReference(pdfUrl)
            progressBar.visibility = View.VISIBLE
            ref.getBytes(MAX_BYTES_PDF).addOnSuccessListener { bytes ->
                progressBar.visibility = View.GONE
                pdfView.fromBytes(bytes).swipeHorizontal(false)
                    .onPageChange { page, pageCount ->
                        val currentPage = page + 1
                        textView.text = String.format("%s / %s", currentPage, pageCount)
                    }.load()
            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                textView.text = "Failed to load"
            }


        }


    }

}

