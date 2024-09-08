package com.example.bookapp.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.example.bookapp.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class DownloadService : Service() {
    private val CHANNEL_ID = "pdf_download_channel"
    private val NOTIFICATION_ID = 1
    private lateinit var fileName: String
    private lateinit var pdfUrl: String
    private var totalSize: Long = 0
    private var downloadJob: Job? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "CANCEL_DOWNLOAD" -> {
                    if (::fileName.isInitialized) {
                        cancelDownload()
                    } else {
                        Log.e("DownloadService", "Cancel download requested, but fileName is not initialized.")
                        showCancelNotification("Download canceled", "No active download for cancellation")
                    }
                }
                else -> {
                    fileName = it.getStringExtra("FILE_NAME") ?: ""
                    pdfUrl = it.getStringExtra("PDF_URL") ?: ""

                    if (fileName.isNotEmpty() && pdfUrl.isNotEmpty()) {
                        downloadPdf(pdfUrl, fileName)
                    } else {
                        Log.e("DownloadService", "File name or URL is missing.")
                    }
                }
            }
        }
        return START_NOT_STICKY
    }



    private fun downloadPdf(pdfUrl: String, fileName: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child(pdfUrl)
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$fileName.pdf")

        Log.e("DownloadService", "Starting download for file: $file")

        // Start the download job
        downloadJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                // Get the total size of the file
                val metadata = storageRef.metadata.await()
                totalSize = metadata.sizeBytes

                val fileTask = storageRef.getFile(file)
                fileTask.addOnProgressListener { snapshot ->
                    val progress = (100.0 * snapshot.bytesTransferred / totalSize).toInt()
                    showProgressNotification(progress)
                }.addOnSuccessListener {
                    Log.d("DownloadService", "Download complete")
                    showCompleteNotification(file)
                }.addOnFailureListener { exception ->
                    Log.e("DownloadService", "Download failed", exception)
                    showErrorNotification(exception)
                }.await()
            } catch (e: Exception) {
                Log.e("DownloadService", "Download failed", e)
                showErrorNotification(e)
            } finally {
                stopSelf()
            }
        }
    }

    private fun showProgressNotification(progress: Int) {
        val sizeText = formatSize(totalSize)
        val cancelIntent = Intent(this, DownloadService::class.java).apply {
            action = "CANCEL_DOWNLOAD"
        }
        val cancelPendingIntent = PendingIntent.getService(
            this,
            0,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading PDF")
            .setContentText("Progress: $progress% ($sizeText)")
            .setSmallIcon(R.drawable.ic_pdf_gray)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(100, progress, false)
            .addAction(R.drawable.ic_pdf_gray, "Cancel", cancelPendingIntent)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun showCompleteNotification(file: File) {
        val openFileIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(FileProvider.getUriForFile(this@DownloadService, "${packageName}.provider", file), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openFileIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Download Complete")
            .setContentText("File: ${file.name} has been downloaded.")
            .setSmallIcon(R.drawable.ic_pdf_gray)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_down_gray, "Open", pendingIntent)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun showErrorNotification(error: Throwable) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("PDF Download Failed")
            .setContentText("There was an error downloading the PDF: ${error.message}")
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PDF Download Channel"
            val descriptionText = "Channel for PDF download notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun formatSize(size: Long): String {
        val sizeInKb = size / 1024
        val sizeInMb = sizeInKb / 1024
        return when {
            sizeInMb >= 1 -> "${sizeInMb} MB"
            sizeInKb >= 1 -> "${sizeInKb} KB"
            else -> "${size} B"
        }
    }



    private fun cancelDownload() {
        downloadJob?.cancel()
        showCancelNotification("Download Canceled", "The PDF download was canceled.")
        stopSelf()
    }

    private fun showCancelNotification(title: String, message: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_down_gray)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

}
