package com.tugas.aplikasimonitoringgudang.ui.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.R

class AdminProfileSettingActivity : AppCompatActivity() {

    private lateinit var adminImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil_setting)

        val adminNameTextView = findViewById<TextView>(R.id.adminName)
        val adminNameInput = findViewById<EditText>(R.id.adminNameInput)
        val okButton = findViewById<Button>(R.id.okButton)
        adminImageView = findViewById(R.id.adminImage)

        adminNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adminNameTextView.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        okButton.setOnClickListener {
            val newName = adminNameInput.text.toString()
            val sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("adminName", newName)
            editor.apply()
            finish()
        }

        adminImageView.setOnClickListener {
            openImagePicker()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            adminImageView.setImageURI(selectedImage)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}
