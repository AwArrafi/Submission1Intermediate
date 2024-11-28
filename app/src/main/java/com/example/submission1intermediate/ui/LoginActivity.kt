package com.example.submission1intermediate.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.submission1intermediate.databinding.ActivityLoginBinding
import com.example.submission1intermediate.repository.LoginRepository
import com.example.submission1intermediate.viewmodel.LoginViewModel
import com.example.submission1intermediate.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize progress bar
        progressBar = binding.progressBar

        // Observe login response
        loginViewModel.loginResponse.observe(this, Observer { response ->
            response?.let {
                // Simpan token dan informasi login ke SharedPreferences
                val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("authToken", it.loginResult.token)
                editor.putBoolean("isLoggedIn", true)
                editor.putString("email", binding.edLoginEmail.text.toString())
                editor.apply()

                // Login sukses
                showToast("Login successful!")
                finish()
            }
        })

        // Observe error
        loginViewModel.loginError.observe(this, Observer { error ->
            showToast(error)
            hideLoading()
        })

        // Set up login button click listener
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please fill in all fields")
                return@setOnClickListener
            }

            // Trigger login
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        showLoading()
        loginViewModel.loginUser(email, password)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }
}
