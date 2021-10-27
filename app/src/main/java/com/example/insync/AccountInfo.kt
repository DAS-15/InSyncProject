package com.example.insync

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.insync.MainActivity
import com.example.insync.R

class AccountInfo: AppCompatActivity()
{
    lateinit var name:TextView
    lateinit var email:TextView
    lateinit var class_code:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_info)

        name = findViewById(R.id.name_value)
        email = findViewById(R.id.email_value)
        class_code  = findViewById(R.id.classcode_value)

        var user = MainActivity.gUser!!
        name.text = user.name
        email.text = user.email
        class_code.text = user.classRoomCode




    }
}