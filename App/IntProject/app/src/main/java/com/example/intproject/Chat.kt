package com.example.intproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_chat.*
import java.lang.Exception

class Chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        try {
            val name: String = intent.getStringExtra("name")
            val count: String = intent.getStringExtra("count")

            txtName.text = name
            txtCount.text = count
        } catch (e: Exception) {
            txtName.text = "Err: " + e.toString()
        }

    }
}
