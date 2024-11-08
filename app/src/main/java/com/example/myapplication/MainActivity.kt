package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var view: ActivityMainBinding
    private lateinit var imageUri: Uri

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                view.imageView.setImageURI(imageUri)
                view.sendSelfie.isEnabled = true
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        view.sendSelfie.isEnabled = false

        imageUri = createImageUri()

        view.takeSelfie.setOnClickListener {

            takePictureLauncher.launch(imageUri)
        }

        view.sendSelfie.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("hodovychenko@op.edu.ua"))
                putExtra(Intent.EXTRA_SUBJECT, "DigiJED Yakubovskyi Maksym")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Переглянути завдання можна за посиланням: https://github.com/Maxyakubobskiy/AndroidTask1"
                )
                putExtra(Intent.EXTRA_STREAM, imageUri)
            }
            startActivity(i)
        }
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "selfie_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IOException("Не вдалося створити URI")
    }
}