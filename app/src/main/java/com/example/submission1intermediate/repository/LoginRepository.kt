package com.example.submission1intermediate.repository

import com.example.submission1intermediate.model.LoginRequest
import com.example.submission1intermediate.model.LoginResponse
import com.example.submission1intermediate.retrofit.RetrofitClient

class LoginRepository {

    suspend fun login(loginRequest: LoginRequest): LoginResponse? {
        try {
            val response = RetrofitClient.apiService.login(loginRequest)
            return if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            return null
        }
    }
}
