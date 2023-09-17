package com.example.deniz_evrendilek_userprofile1

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


const val REQUEST_IMAGE_CAPTURE = 1
const val REQUEST_READ_STORAGE = 2
const val REQUEST_WRITE_STORAGE = 3

class MainActivity : AppCompatActivity() {
    // Buttons | Images
    private lateinit var profilePicImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var saveInfoButton: Button
    private lateinit var cancelSaveInfoButton: Button

    // Input Fields
    private lateinit var inputName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPhone: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var inputClass: EditText
    private lateinit var inputMajor: EditText

    private var profilePicUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        maybeRequestPermissions()
        setupViewVariables()
        loadProfile()

        // TODO: You will need to add an ImageView for displaying the photo and a button to trigger
        //  a Dialog, which will ask the user to either use a camera or existing photos as shown
        //  above (central image). Also, you need to handle screen rotations.
        //  Recall in the class a screen rotation will trigger onCreate( ), which will remove the
        //  unsaved pictures in this case. You can utilize onSaveInstanceState( ) to save the
        //  location of the temporary profile picture and reload it in onCreate().
    }

    private fun loadProfile() {
        // TODO
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("nameInput", "")
        val email = sharedPreferences.getString("emailInput", "")
        val phone = sharedPreferences.getString("phoneInput", "")
        val gender = sharedPreferences.getInt("genderRadio", -1)
        val personClass = sharedPreferences.getString("classInput", "")
        val major = sharedPreferences.getString("majorInput", "")
        profilePicUri = sharedPreferences.getString("profilePicUri", null)?.toUri()

        println(
            "loading data:$name, $email, $phone, $gender, $personClass, $major, $profilePicUri",
        )

        inputName.setText(name)
        inputEmail.setText(email)
        inputPhone.setText(phone)
        radioGroupGender.check(gender)
        inputClass.setText(personClass)
        inputMajor.setText(major)
        if (profilePicUri != null) {
            profilePicImageView.setImageURI(profilePicUri)
        }
    }

    private fun saveProfile() {
        // TODO
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val sharedPrefEditor = sharedPref.edit()

        // Retrieve values from input fields using getFormValues()
        val formValues = getFormValues()
        sharedPrefEditor.putString("nameInput", formValues["nameInput"].toString())
        sharedPrefEditor.putString("emailInput", formValues["emailInput"].toString())
        sharedPrefEditor.putString("phoneInput", formValues["phoneInput"].toString())
        sharedPrefEditor.putInt(
            "genderRadio", Integer.parseInt(formValues["genderRadio"].toString())
        )
        sharedPrefEditor.putString("classInput", formValues["classInput"].toString())
        sharedPrefEditor.putString("majorInput", formValues["majorInput"].toString())
        sharedPrefEditor.putString("profilePicUri", profilePicUri.toString())

        sharedPrefEditor.commit() // TODO: replace with async version
    }

    private fun displayToastMessage(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    private fun hasCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasReadStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWriteStoragePermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun maybeRequestCameraPermission() {
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE
            )
        }
    }

    private fun maybeRequestReadStoragePermission() {
        if (!hasReadStoragePermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE
            )
        }
    }

    private fun maybeRequestWriteStoragePermission() {
        if (!hasWriteStoragePermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE
            )
        }
    }

    private fun maybeRequestPermissions() {
        maybeRequestCameraPermission()
        maybeRequestReadStoragePermission()
        maybeRequestWriteStoragePermission()
    }

    private fun setupViewVariables() {
        selectImageButton = findViewById(R.id.select_picture_button)
        saveInfoButton = findViewById(R.id.button_save_info)
        cancelSaveInfoButton = findViewById(R.id.button_cancel_save_info)
        profilePicImageView = findViewById(R.id.image_view_profile_picture)
        addViewListeners()

        inputName = findViewById(R.id.input_name)
        inputEmail = findViewById(R.id.input_email)
        inputPhone = findViewById(R.id.input_phone)
        radioGroupGender = findViewById(R.id.radio_group_gender)
        inputClass = findViewById(R.id.input_class)
        inputMajor = findViewById(R.id.input_major)
    }

    private fun exitApp() {
        finish()
    }

    private fun getFormValues(): Map<String, Any> {
        println(
            "" + "${inputName.text}, " + "${inputEmail.text}, " + "${inputPhone.text}, " + "${radioGroupGender.checkedRadioButtonId}, " + "${inputClass.text}, " + "${inputMajor.text}" + ""
        )

        return mapOf(
            "nameInput" to inputName.text,
            "emailInput" to inputEmail.text,
            "phoneInput" to inputPhone.text,
            "genderRadio" to radioGroupGender.checkedRadioButtonId,
            "classInput" to inputClass.text,
            "majorInput" to inputMajor.text
        )
    }

    private fun addViewListeners() {
        selectImageButton.setOnClickListener {
            handleOnSelectImage()

        }
        saveInfoButton.setOnClickListener {
            handleOnSave()
        }
        cancelSaveInfoButton.setOnClickListener {
            handleOnCancelSave()
        }
    }

    /**
     * @source: https://developer.android.com/develop/ui/views/components/radiobutton
     */
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_gender_female -> if (checked) {
                    println("radio_gender_female: ${view.getId()}")
                }

                R.id.radio_gender_male -> if (checked) {
                    println("radio_gender_male: ${view.getId()}")
                }
            }
        }
    }

    private fun handleOnSave() {
        println("Save Info")
        // TODO: Save data to storage
        saveProfile()
        displayToastMessage("Saved!")
        exitApp()
    }

    private fun handleOnCancelSave() {
        println("Cancel Save Info")
        exitApp()
    }

    private fun handleOnSelectImage() {
        if (!hasCameraPermission()) {
            exitApp()
            return
        }
        println("Select Image")
        val takePicIntent = Intent(ACTION_IMAGE_CAPTURE)
        @Suppress("DEPRECATION") startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE)
    }

    /**
     * @source https://developer.android.com/training/camera-deprecated/photobasics
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            println("onActivityResult resultCode != RESULT_OK")
            return
        }

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                @Suppress("DEPRECATION") val imageBitmap = data?.extras?.get("data") as Bitmap
                profilePicImageView.setImageBitmap(imageBitmap)
                profilePicUri = saveImageLocally(imageBitmap)
            }
        }
    }

    private fun saveImageLocally(bm: Bitmap): Uri? {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir == null) {
            displayToastMessage(
                "Cannot save profile image locally, " +
                        "photo directory doesn't exist"
            )
            return null
        }
        val imagePrefix = System.currentTimeMillis()
        val imageFile = File(storageDir, "${packageName}_profile_${imagePrefix}_.jpg")

        try {
            val out = FileOutputStream(imageFile)
            bm.compress(Bitmap.CompressFormat.JPEG, 50, out)
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return Uri.fromFile(imageFile)
    }
}
