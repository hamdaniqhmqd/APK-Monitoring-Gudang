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
import android.content.ContentResolver
import android.provider.MediaStore
import java.io.File
import java.io.InputStream
import android.os.Build

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
            val filePath = getRealPathFromURI(profileImageUri!!)
            if (filePath.isNullOrEmpty()) {
                Toast.makeText(this, "Path gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                return
            }

            if (filePath != null) {
                val file = File(filePath)
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val profileImagePart = MultipartBody.Part.createFormData(
                    "profileImagePath", file.name, requestFile
                )

                // Panggil ViewModel untuk memperbarui user dengan gambar
                userViewModel.update(updatedUser, profileImagePart)
            } else {
                // Tampilkan pesan error jika path gambar tidak valid
                Toast.makeText(this, "Gagal mendapatkan path gambar!", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            // Update tanpa gambar
            userViewModel.update(updatedUser, null)
        }

        Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
        finish()
    }

    // Mendapatkan path asli dari URI
    private fun getRealPathFromURI(uri: Uri): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val file = createTempFile(this.cacheDir, "temp_image", ".jpg")
                file.outputStream().use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
                inputStream?.close()
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            contentResolver.query(uri, projection, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.getString(columnIndex)
                } else null
            }
        }
    }

    // Fungsi pembantu untuk membuat file sementara
    private fun createTempFile(cacheDir: File, prefix: String, suffix: String): File {
        return File.createTempFile(prefix, suffix, cacheDir)
    }

    // Peluncur untuk memilih gambar dari galeri
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            profileImageUri = uri
            Glide.with(this).load(uri).into(binding.adminImage) // Pratinjau gambar
        } else {
            Toast.makeText(this, "Gagal memilih gambar!", Toast.LENGTH_SHORT).show()
        }
    }
}
