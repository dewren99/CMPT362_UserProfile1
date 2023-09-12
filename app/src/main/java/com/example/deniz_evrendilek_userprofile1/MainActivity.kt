package com.example.deniz_evrendilek_userprofile1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectImageButton: Button = findViewById(R.id.select_picture_button)
        selectImageButton.setOnClickListener() {
            println("Select Image")
        }
    }
}