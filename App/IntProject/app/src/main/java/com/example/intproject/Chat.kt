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
            txtCount.text = "Online" + count

            linMessages.addView(MessageView(this, "Hejsan!"))
            linMessages.addView(MessageView(this, "Hej!"))
            linMessages.addView(MessageView(this, "Hur gammal är du?"))
            linMessages.addView(MessageView(this, "Jag är 200 år gammal"))
            linMessages.addView(MessageView(this, "Ojdå, det var gammalt."))
        } catch (e: Exception) {
            txtName.text = "Err: " + e.toString()
        }

    }
}
