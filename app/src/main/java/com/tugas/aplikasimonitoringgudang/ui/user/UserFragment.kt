package com.tugas.aplikasimonitoringgudang.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding
import com.tugas.aplikasimonitoringgudang.ui.admin.AdminProfileActivity

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
