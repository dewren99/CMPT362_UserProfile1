package com.example.deniz_evrendilek_userprofile1

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


const val REQUEST_IMAGE_CAPTURE = 1

class MainActivity : AppCompatActivity() {
    private lateinit var profilePicImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var saveInfoButton: Button
    private lateinit var cancelSaveInfoButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        maybeRequestCameraPermission()
        setupViewVariables()
    }

    private fun hasCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun maybeRequestCameraPermission() {
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
            )
        }
    }

    private fun setupViewVariables() {
        selectImageButton = findViewById(R.id.select_picture_button)
        saveInfoButton = findViewById(R.id.button_save_info)
        cancelSaveInfoButton = findViewById(R.id.button_cancel_save_info)
        profilePicImageView = findViewById(R.id.image_view_profile_picture)

        addButtonListeners()
    }

    private fun exitApp() {
        finishAndRemoveTask()
    }

    private fun getFormValues() {
        val inputName: EditText = findViewById(R.id.input_name)
        val inputEmail: EditText = findViewById(R.id.input_email)
        val inputPhone: EditText = findViewById(R.id.input_phone)
        val radioGroupGender: RadioGroup = findViewById(R.id.radio_group_gender)
        val inputClass: EditText = findViewById(R.id.input_class)
        val inputMajor: EditText = findViewById(R.id.input_major)
        println(
            "" +
                    "${inputName.text}, " +
                    "${inputEmail.text}, " +
                    "${inputPhone.text}, " +
                    "${radioGroupGender.checkedRadioButtonId}, " +
                    "${inputClass.text}, " +
                    "${inputMajor.text}" +
                    ""
        )
    }

    private fun addButtonListeners() {
        selectImageButton.setOnClickListener {
            if (!hasCameraPermission()) {
                exitApp()
            }

            println("Select Image")
            val takePicIntent = Intent(ACTION_IMAGE_CAPTURE)
            @Suppress("DEPRECATION")
            startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE)

        }
        saveInfoButton.setOnClickListener {
            println("Save Info")
            getFormValues()
        }
        cancelSaveInfoButton.setOnClickListener {
            println("Cancel Save Info")
        }
    }

    /**
     * @source https://developer.android.com/training/camera-deprecated/photobasics
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            @Suppress("DEPRECATION") val imageBitmap = data?.extras?.get("data") as Bitmap
            profilePicImageView.setImageBitmap(imageBitmap)
        }
    }
}