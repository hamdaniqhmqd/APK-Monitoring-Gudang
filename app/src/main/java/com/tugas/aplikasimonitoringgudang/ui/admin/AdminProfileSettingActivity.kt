package com.tugas.aplikasimonitoringgudang.ui.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tugas.aplikasimonitoringgudang.ui.user.UserViewModel
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import com.tugas.aplikasimonitoringgudang.R
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import java.io.File

class AdminProfileSettingActivity : AppCompatActivity() {

    private lateinit var adminImageView: ImageView
    private lateinit var adminNameTextView: TextView
    private var selectedImage: Uri? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil_setting)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val adminNameInput = findViewById<EditText>(R.id.adminNameInput)
        val okButton = findViewById<Button>(R.id.okButton)
        adminImageView = findViewById(R.id.adminImage)
        adminNameTextView = findViewById(R.id.adminName)

        // Initialize username
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        adminNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adminNameTextView.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        okButton.setOnClickListener {
            val newName = adminNameInput.text.toString()
            val imagePath = selectedImage?.let { getPathFromUri(it) } ?: ""
            viewModel.updateAdminProfile(username, newName, imagePath)
            finish()
        }

        adminImageView.setOnClickListener {
            openImagePicker()
        }

        viewModel.getAdminLiveData(username).observe(this) { admin ->
            admin?.let {
                adminNameTextView.text = it.adminName
                if (it.profileImagePath != null) {
                    val imageUri = Uri.fromFile(File(it.profileImagePath))
                    adminImageView.setImageURI(imageUri)
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            selectedImage = data?.data
            selectedImage?.let { uri ->
                val imagePath = getPathFromUri(uri)
                if (imagePath != null) {
                    adminImageView.setImageURI(uri)

                    // Save the selected image path
                    val sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("adminImagePath", imagePath)
                    editor.apply()
                } else {
                    Log.e("AdminProfileSetting", "Failed to get image path")
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Log.e("AdminProfileSetting", "Selected image URI is null")
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = cursor.getString(columnIndex)
            }
        }
        return path
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        adminNameTextView.text = username
        try {
            val imagePath = sharedPreferences.getString("adminImagePath", null)
            if (imagePath != null) {
                val imageUri = Uri.parse(imagePath)
                adminImageView.setImageURI(imageUri)
            }
        } catch (e: Exception) {
            Log.e("AdminProfileActivity", "Error setting image URI", e)
            Toast.makeText(this, "Error loading profile image", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}
