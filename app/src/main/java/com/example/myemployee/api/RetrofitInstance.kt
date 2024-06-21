package com.example.myemployee.api

import com.example.myemployee.constants.Constant.Companion.BASE_URL
import com.example.myemployee.model.User
import com.example.myemployee.model.UserResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("users")
    suspend fun getUsers(@Query("limit") limit: Int, @Query("skip") skip: Int): UserResponse

    @GET("users/{id}")
    suspend fun getUserDetails(@Path("id") id: Int): User
}
