package com.tugas.aplikasimonitoringgudang.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.user.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val userDao = GudangDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun getAdminLiveData(username: String): LiveData<User?> {
        return repository.getAdminLiveData(username)
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
    }

    suspend fun getUserById(id: Int): User? {
        return repository.getUserById(id)
    }
}
