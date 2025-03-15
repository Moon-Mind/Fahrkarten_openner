package com.example.fahrkarten_openner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.IOException
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val IMAGE_NAME = "selectedImage.jpg"
    private val PICK_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setze den Titel der ActionBar
        supportActionBar?.title = "Dein Titel hier"

        val imageView: ImageView = findViewById(R.id.imageView)
        val imageUri = getImageUriFromInternalStorage()
        if (imageUri != null) {
            imageView.setImageURI(imageUri)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_add_image -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
                true
            }
            R.id.action_change_title -> {
                showChangeTitleDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showChangeTitleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Titel ändern")

        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_change_title, null)
        val input = viewInflated.findViewById<EditText>(R.id.input)

        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            val newTitle = input.text.toString()
            supportActionBar?.title = newTitle
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
