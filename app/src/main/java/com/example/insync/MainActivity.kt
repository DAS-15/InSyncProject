package com.example.insync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun redirectIt(view: android.view.View) {
        val intent : Intent = Intent(applicationContext, TimeTableList::class.java)
        startActivity(intent)
    }
}