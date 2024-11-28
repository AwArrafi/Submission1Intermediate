package com.example.submission1intermediate

import android.app.Application

class MyApplication : Application() {

    // Menyimpan instance aplikasi untuk akses Context global
    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this  // Menyimpan instance aplikasi
    }
}
