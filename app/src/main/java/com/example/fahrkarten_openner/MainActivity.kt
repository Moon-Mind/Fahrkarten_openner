package com.example.fahrkarten_openner

import android.app.Activity
<<<<<<< HEAD
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.FileOutputStream
import android.content.Context
import java.io.IOException
import com.google.android.material.floatingactionbutton.FloatingActionButton
=======
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
>>>>>>> 971be3e (update)

class MainActivity : AppCompatActivity() {
    private val IMAGE_NAME = "selectedImage.jpg"
    private val PICK_IMAGE = 1

<<<<<<< HEAD
=======
    private lateinit var imageView: ImageView
    private lateinit var fab: FloatingActionButton
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private val matrix = Matrix()
    private var scaleFactor = 1.0f
    private var translateX = 0f
    private var translateY = 0f

>>>>>>> 971be3e (update)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

<<<<<<< HEAD
        val imageView: ImageView = findViewById(R.id.imageView)
        val imageUri = getImageUriFromInternalStorage()
        if (imageUri != null) {
            imageView.setImageURI(imageUri)
        }

        val button: FloatingActionButton = findViewById(R.id.fab)
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }
    }

=======
        imageView = findViewById(R.id.imageView)
        fab = findViewById(R.id.fab)

        loadSavedImage()

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        gestureDetector = GestureDetector(this, GestureListener())

        fab.setOnClickListener {
            openGallery()
        }

        // Restore state
        savedInstanceState?.let {
            scaleFactor = it.getFloat("scaleFactor")
            translateX = it.getFloat("matrixTranslateX")
            translateY = it.getFloat("matrixTranslateY")
            matrix.setScale(scaleFactor, scaleFactor)
            matrix.postTranslate(translateX, translateY)
            imageView.imageMatrix = matrix
        }
    }

    private fun loadSavedImage() {
        getImageUriFromInternalStorage()?.let { uri ->
            imageView.setImageURI(uri)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE)
    }

>>>>>>> 971be3e (update)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
<<<<<<< HEAD
            val imageUri = data.data
            val imageView: ImageView = findViewById(R.id.imageView)
            imageView.setImageURI(imageUri)

            // Bild in internen Speicher speichern
            try {
                val bitmap: Bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                saveImageToInternalStorage(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
    private fun getImageUriFromInternalStorage(): Uri? {
        val file = getFileStreamPath(IMAGE_NAME)
        return if (file != null && file.exists()) Uri.fromFile(file) else null
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) {
        try {
            val fos = openFileOutput(IMAGE_NAME, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.close()
=======
            data.data?.let { imageUri ->
                val bitmap = decodeSampledBitmapFromUri(imageUri, imageView.width, imageView.height)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                    saveBitmapToInternalStorage(bitmap)
                } else {
                    Toast.makeText(this, "Failed to load image.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getImageUriFromInternalStorage(): Uri? {
        val file = getFileStreamPath(IMAGE_NAME)
        return if (file.exists()) Uri.fromFile(file) else null
    }

    private fun saveBitmapToInternalStorage(bitmap: Bitmap) {
        try {
            openFileOutput(IMAGE_NAME, Context.MODE_PRIVATE).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }
>>>>>>> 971be3e (update)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
<<<<<<< HEAD
}
=======

    private fun decodeSampledBitmapFromUri(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, this)
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
        }
        return BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            scaleGestureDetector.onTouchEvent(it)
            gestureDetector.onTouchEvent(it)
        }
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f))
            matrix.setScale(scaleFactor, scaleFactor)
            imageView.imageMatrix = matrix
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (e1 != null && e2 != null) {
                translateX -= distanceX
                translateY -= distanceY
                matrix.postTranslate(-distanceX, -distanceY)
                imageView.imageMatrix = matrix
                return true
            }
            return false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("scaleFactor", scaleFactor)
        outState.putFloat("matrixTranslateX", translateX)
        outState.putFloat("matrixTranslateY", translateY)
    }
}
>>>>>>> 971be3e (update)
