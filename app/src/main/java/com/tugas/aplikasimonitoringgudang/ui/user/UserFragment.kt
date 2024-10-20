package com.tugas.aplikasimonitoringgudang.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.ui.admin.AdminProfileActivity
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding
import android.content.Intent
import android.net.Uri

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val adminName = sharedPreferences.getString("adminName", "Admin")
        val adminImageUri = sharedPreferences.getString("adminImageUri", null)

        binding.greetingText.text = "Hi! $adminName"

        binding.greetingText.setOnClickListener {
            navigateToAdminProfile()
        }

        binding.userIcon.setOnClickListener {
            navigateToAdminProfile()
        }

        if (adminImageUri != null) {
            binding.userIcon.setImageURI(Uri.parse(adminImageUri))
        }

        //        kode user di bawah ini
    }

    private fun navigateToAdminProfile() {
        val intent = Intent(requireContext(), AdminProfileActivity::class.java)
        startActivity(intent)
    }
}
