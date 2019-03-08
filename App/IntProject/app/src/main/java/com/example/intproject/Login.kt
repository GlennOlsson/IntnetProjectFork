package com.example.intproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject
import java.lang.Exception

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        btnLogin.setOnClickListener {
            //val username = edtUsername.text.toString()
            //val password = edtPassword.text.toString()


            val url = "https://glennolsson.se/intnet/login"
            val queue = Volley.newRequestQueue(this)
            val reqBody = JSONObject()
            reqBody.put("name", edtUsername.text.toString())
            reqBody.put("password", edtPassword.text.toString())


            val req = JsonObjectRequest(
                Request.Method.POST, url, reqBody,
                Response.Listener<JSONObject> { response ->
                    try {
                        val success = response.getBoolean("success")
                        val token = response.getString("token")
                        login(success)
                    } catch (e : Exception) {
                        //txtDebug.text = "I Re.Lis: " + e.toString()
                        login(false)
                    }
                },
                Response.ErrorListener { error ->
                    txtDebug.text = "I Er.Lis: " + error.message
                })

            queue.add(req)
            queue.start()
        }
    }

    fun login(success: Boolean) {
        if (success) {
            val intent = Intent(this, Rooms::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Wrong login credentials!", Toast.LENGTH_SHORT).show()
        }
    }
}
