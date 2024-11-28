package com.example.submission1intermediate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1intermediate.model.LoginRequest
import com.example.submission1intermediate.model.LoginResponse
import com.example.submission1intermediate.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> get() = _loginError

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            val loginRequest = LoginRequest(email, password)
            val response = repository.login(loginRequest)

            if (response != null) {
                _loginResponse.postValue(response)
            } else {
                _loginError.postValue("Login failed, please check your credentials.")
            }
        }
    }
}
