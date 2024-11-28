package com.example.submission1intermediate.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.submission1intermediate.adapter.StoryAdapter
import com.example.submission1intermediate.databinding.ActivityMainBinding
import com.example.submission1intermediate.model.Story
import com.example.submission1intermediate.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private val storyList = mutableListOf<Story>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengecek apakah user sudah login atau belum
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            redirectToLogin()
        } else {
            val token = sharedPref.getString("authToken", null)
            if (token.isNullOrEmpty()) {
                Log.e("MainActivity", "Token is missing or empty!")
                redirectToLogin()
            } else {
                fetchStories(token)
            }
        }

        // Set up RecyclerView untuk menampilkan daftar cerita
        storyAdapter = StoryAdapter(storyList) { story -> onStoryClicked(story) }
        binding.recyclerView.adapter = storyAdapter

        // Tombol logout
        binding.actionLogout.setOnClickListener {
            logout()
        }
    }

    private fun fetchStories(token: String) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("MainActivity", "Fetching stories with token: $token")
                val response = RetrofitClient.apiService.getStories("Bearer $token", 1, 10, 0)

                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        // Pastikan response body tidak null
                        response.body()?.let { responseBody ->
                            if (responseBody.isEmpty()) {
                                showError("No stories found")
                            } else {
                                storyList.clear()
                                storyList.addAll(responseBody)
                                storyAdapter.notifyDataSetChanged()
                            }
                        } ?: showError("No data received from API")
                    } else {
                        showError("Error: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    showError("Failed to load stories: ${e.message}")
                    Log.e("MainActivity", "Error fetching stories", e)
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()

        redirectToLogin()
    }

    private fun onStoryClicked(story: Story) {
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra("story", story)
        startActivity(intent)
    }
}
