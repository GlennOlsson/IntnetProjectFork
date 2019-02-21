package com.example.intproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username == "Oscar Ekholm" && password == "hunter2") {
                val intent = Intent(this, Rooms::class.java)
                //intent.putExtra("identifier", "hej")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Wrong login credentials!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
