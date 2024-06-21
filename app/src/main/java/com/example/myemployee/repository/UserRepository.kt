package com.example.myemployee.repository

import com.example.myemployee.api.RetrofitInstance

class UserRepository {
    suspend fun getUsers(limit: Int, skip: Int) = RetrofitInstance.api.getUsers(limit, skip)
    suspend fun getUserDetails(id: Int) = RetrofitInstance.api.getUserDetails(id)
}
