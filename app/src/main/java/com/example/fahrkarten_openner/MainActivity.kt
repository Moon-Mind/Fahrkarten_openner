package com.example.fahrkarten_openner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.graphics.pdf.PdfRenderer
import android.view.ScaleGestureDetector

class MainActivity : AppCompatActivity() {

    private val PDF_NAME = "selected_ticket.pdf" // Filename for internal storage
    private val PICK_PDF = 1
    private lateinit var imageView: ImageView // Declare as lateinit

    private var scale = 1f
    private val gestureDetector by lazy { ScaleGestureDetector(this, object : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true // Return true to indicate we've handled the gesture
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Update scale based on the gesture, but avoid exceeding maximum zoom
            scale = Math.min(scale * detector.scaleFactor, 4.0f) // Max zoom factor of 4x
            imageView.scaleX = scale
            imageView.scaleY = scale
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.imageView)
        val button: FloatingActionButton = findViewById(R.id.fab)

        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "application/pdf"
            startActivityForResult(intent, PICK_PDF)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PDF && resultCode == Activity.RESULT_OK && data != null) {
            val pdfUri: Uri = data.data!!
            val fileName: String = getFileName(pdfUri)
            val imageView: ImageView = findViewById(R.id.imageView) // Reference the imageView

            try {
                // Pre-render at a larger size for better zoom quality
                val bitmap = convertPdfToBitmap(pdfUri, imageView.width * 2, imageView.height * 2)
                imageView.setImageBitmap(bitmap)

                // Optional: Save PDF to internal storage
                savePdfToInternalStorage(pdfUri, fileName)
            } catch (e: IOException) {
                Log.e("MainActivity", "Error processing PDF: ${e.message}")
            }
        }
    }


    private fun getFileName(uri: Uri): String {
        val fileName = uri.lastPathSegment ?: ""
        return if (fileName.isEmpty()) "downloaded_pdf.pdf" else fileName
    }

    private fun savePdfToInternalStorage(pdfUri: Uri, fileName: String) {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        val outputStream = FileOutputStream(file)
        val inputStream = contentResolver.openInputStream(pdfUri)

        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream!!.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }

        inputStream.close()
        outputStream.flush()
        outputStream.close()

        Log.i("MainActivity", "PDF saved: $file")
    }

    // Function to convert the first page of PDF to a bitmap
    // Function to convert the first page of PDF to a bitmap
    fun convertPdfToBitmap(pdfUri: Uri, width: Int, height: Int): Bitmap {
        val fileDescriptor = contentResolver.openFileDescriptor(pdfUri, "r")!!

        val pdfRenderer = PdfRenderer(fileDescriptor)

        val page = pdfRenderer.openPage(0)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        // Use RENDER_MODE_FOR_PRINT for better image quality
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)

        page.close()
        pdfRenderer.close()
        fileDescriptor.close()

        return bitmap
    }

    private fun getImageUriFromInternalStorage(fileName: String): Uri? {
        val directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        // Check if directory exists and create the File object conditionally
        val file = directory?.let { File(it, fileName) }

        if (file?.exists() == true) {
            return Uri.fromFile(file)
        } else {
            if (directory == null) {
                Log.w("MainActivity", "Download directory not available")
            } else {
                Log.w("MainActivity", "Image file not found: $fileName")
            }
        }
        return null
    }

    // Helper function to get real file path from URI (optional)
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val filePath: String
        if (cursor != null) {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(android.provider.MediaStore.MediaColumns.DATA)
            filePath = cursor.getString(idx)
            cursor.close()
        } else {
            filePath = uri.path ?: ""
        }
        return filePath
    }
}
