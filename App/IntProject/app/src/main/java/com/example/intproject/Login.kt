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

    var register: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        btnLogin.setOnClickListener {
            //val username = edtUsername.text.toString()
            //val password = edtPassword.text.toString()

            val url: String
            val respList: Response.Listener<JSONObject>

            if (register) {
                url = "https://glennolsson.se/intnet/newuser"
                respList = Response.Listener<JSONObject> { response ->
                    try {
                        val success = response.getBoolean("success")
                        val reason = response.optString("reason")
                        register(success, reason)
                    } catch (e : Exception) {
                        txtDebug.text = "I Re.Lis: " + e.toString()
                    }
                }
            } else {
                url = "https://glennolsson.se/intnet/login"
                respList = Response.Listener<JSONObject> { response ->
                    try {
                        val success = response.getBoolean("success")
                        val token = response.getString("token")
                        login(success)
                    } catch (e : Exception) {
                        login(false)
                    }
                }
            }

            val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue
            val reqBody = JSONObject()
            reqBody.put("name", edtUsername.text.toString())
            reqBody.put("password", edtPassword.text.toString())



            val req = JsonObjectRequest(
                Request.Method.POST, url, reqBody,
                respList,
                Response.ErrorListener { error ->
                    txtDebug.text = "I Er.Lis: " + error.message
                })

            queue.add(req)
        }

        btnRegister.setOnClickListener {
            switchMode()
        }
    }

    fun switchMode() {
        register = !register
        txtDebug.text = register.toString()
        val l = "LOGIN"
        val r = "REGISTER USER"

        if (!register) {
            btnLogin.text = l
            btnRegister.text = r
        } else {
            btnLogin.text = r
            btnRegister.text = l
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

    fun register(success: Boolean, reason: String?) {
        if (success) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            switchMode()
        } else {
            Toast.makeText(this, "Failed to register user: " + reason, Toast.LENGTH_SHORT).show()
        }
    }
}
