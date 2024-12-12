package com.tugas.aplikasimonitoringgudang.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat
import com.tugas.aplikasimonitoringgudang.R
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.databinding.AdminProfilSettingBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AdminProfileSettingActivity : AppCompatActivity() {

    private lateinit var binding: AdminProfilSettingBinding
    private lateinit var userViewModel: UserViewModel

    private var profileImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding
        binding = AdminProfilSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Inisialisasi preferensi
        AppPreferences.init(this)
        val userId = AppPreferences.getUserId()

        // Observasi data user berdasarkan ID
        userId?.let { id ->
            userViewModel.getUserById(id).observe(this) { user ->
                setupInitialData(user)

                // Tombol OK untuk menyimpan perubahan
                binding.okButton.setOnClickListener {
                    val newName = binding.adminNameInput.text.toString()
                    updateUserProfile(user, newName)
                }
            }
        }

        // Listener untuk mengubah nama admin secara dinamis
        binding.adminNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.adminName.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Pilih gambar dari galeri
        binding.adminImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    // Menampilkan data awal user
    private fun setupInitialData(user: User) {
        binding.adminName.text = user.adminName
        binding.adminNameInput.setText(user.adminName)

        if (!user.profileImagePath.isNullOrEmpty()) {
            val endpoint = "https://gudang-pakaian-api.infitechd.my.id/storage/admin/${user.profileImagePath}"
            Glide.with(binding.adminImage.context)
                .load(endpoint)
                .placeholder(R.drawable.profile)
                .into(binding.adminImage)
        } else {
            binding.adminImage.setImageResource(R.drawable.profile)
        }
    }

    // Mengupdate profil user
    private fun updateUserProfile(user: User, newName: String) {
        val updatedUser = user.copy(adminName = newName)

        if (profileImageUri != null) {
            val file = File(getRealPathFromURI(profileImageUri!!))
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val profileImagePart = MultipartBody.Part.createFormData(
                "profileImagePath", file.name, requestFile
            )

            // Panggil ViewModel untuk memperbarui user dengan gambar
            userViewModel.update(updatedUser, profileImagePart)
        } else {
            // Update tanpa gambar
            userViewModel.update(updatedUser, null)
        }

        Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
        finish()
    }

    // Mendapatkan path asli dari URI
    private fun getRealPathFromURI(uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, filePathColumn, null, null, null).use { cursor ->
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    return it.getString(columnIndex)
                }
            }
        }
        throw IllegalArgumentException("Gagal mendapatkan path dari URI")
    }

    // Peluncur untuk memilih gambar dari galeri
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            profileImageUri = it
            Glide.with(this).load(it).into(binding.adminImage) // Pratinjau gambar yang dipilih
        }
    }
}
