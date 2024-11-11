package com.tugas.aplikasimonitoringgudang.ui.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding
import com.tugas.aplikasimonitoringgudang.ui.admin.AdminProfileActivity
import java.io.File

class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.barangCount.observe(viewLifecycleOwner) { count ->
            binding.barangCount.text = count.toString()
        }

        userViewModel.supplierCount.observe(viewLifecycleOwner) { count ->
            binding.supplierCount.text = count.toString()
        }

        userViewModel.transaksiMasukCount.observe(viewLifecycleOwner) { count ->
            binding.transaksiMasukCount.text = count.toString()
        }

        userViewModel.transaksiKeluarCount.observe(viewLifecycleOwner) { count ->
            binding.transaksiKeluarCount.text = count.toString()
        }

        // Trigger the count updates
        userViewModel.updateCounts()

        // Set click listener for admin profile
        binding.userIcon.setOnClickListener {
            // Navigate to AdminProfileActivity
            val intent = Intent(requireContext(), AdminProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileImage()
        loadAdminName()
    }

    private fun loadProfileImage() {
        val sharedPreferences = requireContext().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val imagePath = sharedPreferences.getString("adminImagePath", null)
        if (imagePath != null) {
            try {
                val imageFile = File(imagePath)
                if (imageFile.exists()) {
                    val imageUri = Uri.fromFile(imageFile)
                    binding.userIcon.setImageURI(imageUri)
                } else {
                    Log.e("UserFragment", "Image file does not exist")
                    Toast.makeText(requireContext(), "Image file not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("UserFragment", "Error setting image from path", e)
                Toast.makeText(requireContext(), "Error loading profile image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("UserFragment", "Image path is null")
        }
    }

    private fun loadAdminName() {
        val sharedPreferences = requireContext().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        
        userViewModel.getAdminName(username).observe(viewLifecycleOwner) { adminName ->
            val displayName = if (adminName.isNullOrEmpty()) "Admin" else adminName
            binding.greetingText.text = "Hi! $displayName"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
