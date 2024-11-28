package com.example.submission1intermediate.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submission1intermediate.R
import com.example.submission1intermediate.model.Story

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var ivPhoto: ImageView
    private lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)

        tvName = findViewById(R.id.tvDetailName)
        ivPhoto = findViewById(R.id.ivDetailPhoto)
        tvDescription = findViewById(R.id.tvDetailDescription)

        // Mengambil data Story yang dikirim melalui Intent
        val story: Story? = intent.getSerializableExtra("story") as? Story

        story?.let {
            tvName.text = it.name
            tvDescription.text = it.description
            // Menggunakan Glide untuk memuat gambar
            Glide.with(this)
                .load(it.photoUrl)
                .into(ivPhoto)
        } ?: run {
            Toast.makeText(this, "Story not found", Toast.LENGTH_SHORT).show()
        }
    }
}
