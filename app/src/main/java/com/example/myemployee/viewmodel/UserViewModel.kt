package com.example.myemployee.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myemployee.model.User
import com.example.myemployee.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val _users = MutableLiveData<List<User>>()

    val users: LiveData<List<User>> get() = _users
    private val _userDetails = MutableLiveData<User>()

    val userDetails: LiveData<User> get() = _userDetails
    private var currentPage = 0
    private val pageSize = 10

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = userRepository.getUsers(pageSize, currentPage * pageSize)
                _users.postValue(response.users)
                currentPage++
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    fun fetchUserDetails(id: Int) {
        viewModelScope.launch {
            try {
                val response = userRepository.getUserDetails(id)
                _userDetails.postValue(response)
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }
}
