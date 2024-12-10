package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = GudangDatabase.getDatabase(application).userDao()
        val networkHelper = NetworkHelper(application)
        repository = UserRepository(userDao, networkHelper)
    }

    fun sinkronisasiDataUser() {
        viewModelScope.launch {
            try {
                repository.sinkronisasiDataUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(user: User): LiveData<User?> = liveData {
        viewModelScope.launch {
            try {
                val result = repository.login(user)
                emit(result)
//                repository.login(user)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(null)
//                e.printStackTrace()
            }
        }
    }

    fun insert(user: User) {
        viewModelScope.launch {
            try {
                repository.insert(user)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            try {
                repository.update(user)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            try {
                repository.delete(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getUserById(id: Int): LiveData<User> {
        val result = MutableLiveData<User>()
        viewModelScope.launch {
            try {
                val user = repository.getUserById(id)
                result.postValue(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    // LiveData untuk setiap hitungan
    private val _barangCount = MutableLiveData<Int>()
    val barangCount: LiveData<Int> get() = _barangCount
    private val _supplierCount = MutableLiveData<Int>()
    val supplierCount: LiveData<Int> get() = _supplierCount
    private val _transaksiMasukCount = MutableLiveData<Int>()
    val transaksiMasukCount: LiveData<Int> get() = _transaksiMasukCount
    private val _transaksiKeluarCount = MutableLiveData<Int>()
    val transaksiKeluarCount: LiveData<Int> get() = _transaksiKeluarCount

    fun updateCounts() {
        viewModelScope.launch {
            _barangCount.value = repository.getBarangCount()
            _supplierCount.value = repository.getSupplierCount()
            _transaksiMasukCount.value = repository.getTransaksiMasukCount()
            _transaksiKeluarCount.value = repository.getTransaksiKeluarCount()
        }
    }
}
