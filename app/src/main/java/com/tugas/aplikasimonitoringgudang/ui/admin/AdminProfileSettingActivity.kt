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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.result.contract.ActivityResultContracts
import com.tugas.aplikasimonitoringgudang.R
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.data.user.User
import java.io.File
import java.io.FileOutputStream

class AdminProfileSettingActivity : AppCompatActivity() {

    private lateinit var adminImageView: ImageView
    private lateinit var adminNameTextView: TextView
    private var imageUri: Uri? = null
    private var imagePath: String? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil_setting)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val adminNameInput = findViewById<EditText>(R.id.adminNameInput)
        val okButton = findViewById<Button>(R.id.okButton)
        adminImageView = findViewById(R.id.adminImage)
        adminNameTextView = findViewById(R.id.adminName)

        AppPreferences.init(this)
        val userId = AppPreferences.getUserId()
        val username = AppPreferences.getUsername().toString()
        val isLoggedIn = AppPreferences.isLoggedIn()

        userId.let {
            viewModel.getUserById(it).observe(this) { user ->
                adminNameTextView.text = user.adminName
                adminNameInput.setText(user.adminName)

                if (user.profileImagePath != null) {
                    Glide.with(adminImageView.context)
                        .load(user.profileImagePath)
                        .placeholder(R.drawable.profile)
                        .into(adminImageView)
                } else {
                    adminImageView.setImageResource(R.drawable.profile)
                }

                okButton.setOnClickListener {
                    val newName = adminNameInput.text.toString()
                    // Menyimpan path gambar
                    val updatedImagePath = imageUri?.let { uri ->
                        uriToBitmap(uri)?.let { bitmap ->
                            saveImageToLocalStorage(bitmap, username)
                        }
                    } ?: imagePath ?: user.profileImagePath

                    if (newName != "") {
                        viewModel.update(User(id = userId, username = username, password = user.password, adminName = newName, profileImagePath = updatedImagePath))
                    } else {
                        viewModel.update(User(id = userId, username = username, password = user.password, adminName = user.adminName, profileImagePath = user.profileImagePath))
                    }
                    
                    finish()
                }
            }
        }

        adminNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adminNameTextView.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        adminImageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            Glide.with(this).load(it).into(adminImageView) // Preview image
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = this.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveImageToLocalStorage(imageBitmap: Bitmap, username: String): String {
        val filename = "image_profile_${username}_${System.currentTimeMillis()}.jpg"
        val file = File(this?.filesDir, filename)
        val outputStream = FileOutputStream(file)
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath
    }
}
