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

    private var imageUri: Uri? = null
    private var imagePart: MultipartBody.Part? = null
    private var imageUpdate : String? = null
    private var profileImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan View Binding untuk mengganti setContentView
        binding = AdminProfilSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Preferensi untuk pengguna saat ini
        AppPreferences.init(this)
        val userId = AppPreferences.getUserId()

        userId?.let {
            userViewModel.getUserById(it).observe(this) { user ->
                // Set data awal
                binding.adminName.text = user.adminName
                binding.adminNameInput.setText(user.adminName)

                // Muat gambar profil jika tersedia
                if (user.profileImagePath != null) {
                    val end_point = "https://gudang-pakaian-api.infitechd.my.id/storage/admin/${user.profileImagePath}"
                    Glide.with(binding.adminImage.context)
                        .load(end_point)
                        .placeholder(R.drawable.profile)
                        .into(binding.adminImage)
                } else {
                    binding.adminImage.setImageResource(R.drawable.profile)
                }

                binding.okButton.setOnClickListener {
                    val newName = binding.adminNameInput.text.toString()

                    user.profileImagePath?.let { imageUrl ->
                        Glide.with(this).load(imageUrl).into(binding.adminImage)
                    }

                    val user = User(
                        id = userId,
                        username = user.username,
                        password = user.password,
                        adminName = newName
                    )

                    val file = File(this.getRealPathFromURI(profileImageUri!!)!!)
                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val profileImagePart = MultipartBody.Part.createFormData(
                        "profileImagePath", file.name, requestFile
                    )

//                    val file = File()
//                    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                    val profileImagePart = MultipartBody.Part.createFormData(
//                        "profileImagePath", file.name, requestBody
//                    )


                    userViewModel.update(user, profileImagePart)

//                    if (profileImageUri != null) {
//                        val file = File(this.getRealPathFromURI(profileImageUri!!).toString())
//                        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                        val profileImagePart = MultipartBody.Part.createFormData(
//                            "profileImagePath", file.name, requestFile
//                        )
//
//                        userViewModel.update(user, profileImagePart)
//                    }

                    finish() // Tutup aktivitas setelah berhasil
                }
            }
        }

        // Tambahkan listener untuk mengubah nama admin secara dinamis
        binding.adminNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.adminName.text = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Pilih gambar
        binding.adminImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            pickImageLauncher.launch("image/*")
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//            profileImageUri = data?.data
//        }
//    }

    private fun Context.getRealPathFromURI(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            if (columnIndex != -1) {
                cursor.moveToFirst()
                val id = cursor.getLong(columnIndex)
                val fileUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                return fileUri.path
            }
        }
        return null // return null if column index is invalid or data is not found
    }


    // Peluncur untuk memilih gambar dari galeri
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            profileImageUri = it
            Glide.with(this).load(it).into(binding.adminImage) // Pratinjau gambar yang dipilih
        }
    }
}
