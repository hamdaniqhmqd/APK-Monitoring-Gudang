package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel

class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var user_id: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppPreferences.init(requireContext())
        user_id = AppPreferences.getUserId()
        val username = AppPreferences.getUsername()
        val isLoggedIn = AppPreferences.isLoggedIn()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

//        userViewModel.sinkronisasiDataUser()

        user_id?.let { id ->
            userViewModel.getUserById(id).observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    binding.greetingText.text = "Hi! ${user.adminName}"
                    if (user.profileImagePath != null) {
                        val end_point = "https://gudang-pakaian-api.infitechd.my.id/storage/admin/${user.profileImagePath}"
                        Glide.with(binding.userIcon.context)
                            .load(end_point)
                            .placeholder(R.drawable.profile)
                            .into(binding.userIcon)
                    } else {
                        binding.userIcon.setImageResource(R.drawable.profile)
                    }
                } else {
                    // Jika user null, tampilkan placeholder atau pesan default
                    binding.greetingText.text = "Hi! Guest"
                    binding.userIcon.setImageResource(R.drawable.profile)
                }
            }
        }


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
        binding.header.setOnClickListener {
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
