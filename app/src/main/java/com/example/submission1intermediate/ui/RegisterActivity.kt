package com.example.submission1intermediate.ui

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.submission1intermediate.databinding.ActivityRegisterBinding
import com.example.submission1intermediate.model.RegisterRequest
import com.example.submission1intermediate.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize progress bar
        progressBar = binding.progressBar

        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        // Validasi input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Please fill in all fields")
            return
        }

        // Validasi email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email")
            return
        }

        // Validasi password minimal 8 karakter
        if (password.length < 8) {
            showToast("Password must be at least 8 characters")
            return
        }

        showLoading()

        // Membuat objek request untuk registrasi
        val registerRequest = RegisterRequest(name, email, password)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.registerUser(registerRequest)

                hideLoading()

                if (response.isSuccessful) {
                    showToast("Registration successful!")
                    finish()  // Arahkan kembali ke halaman login setelah registrasi berhasil
                } else {
                    showToast("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                hideLoading()
                showToast("Error: ${e.localizedMessage}")
            }
        }
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
