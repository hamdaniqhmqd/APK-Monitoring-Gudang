package com.tugas.aplikasimonitoringgudang.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.user.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun getAdminLiveData(username: String): LiveData<User?> {
        return repository.getAdminLiveData(username)
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }
}
