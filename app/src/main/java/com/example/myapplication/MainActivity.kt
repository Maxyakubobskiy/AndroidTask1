package com.example.myapplication

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.myapplication.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var view: ActivityMainBinding
    private lateinit var imageUri: Uri

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){success ->
        if(success){
            view.imageView.setImageURI(imageUri)
            view.sendSelfie.isEnabled = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        imageUri = createImageUri()

        view.takeSelfie.setOnClickListener{

            takePictureLauncher.launch(imageUri)
        }

        view.sendSelfie.setOnClickListener{
            val i = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("hodovychenko@op.edu.u"))
                putExtra(Intent.EXTRA_SUBJECT, "DigiJED Yakubovskyi Maksym")
                putExtra(Intent.EXTRA_TEXT, "Це моє селфі")
                putExtra(Intent.EXTRA_STREAM, imageUri)
            }
                startActivity(i)
        }
    }

    private fun createImageUri(): Uri{
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"selfie_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues) ?: throw IOException("Не вдалося створити URI")
    }
}