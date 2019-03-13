package com.example.intproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject
import java.lang.Exception

class Login : AppCompatActivity() {

    var register: Boolean = false
    var username: String = ""
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val socketSingleton = SocketSingleton.getInstance(this.applicationContext)
        val tag = socketSingleton.tag
        val socket = socketSingleton.socket

        socket.on(Socket.EVENT_CONNECT) {
            Log.i(tag, "Socket connected!")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.i(tag, "Socket disconnected!")
        }
        socket.on(Socket.EVENT_ERROR) { error ->
            Log.i(tag, error.toString())
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) { error ->
            Log.i("ConErr"+tag, error[0].toString())
        }

        //Log.i(tag+"L", socket.toString())
        socketSingleton.logSocket()

        val jsonEmit = JSONObject()
        jsonEmit.put("username", "oscarekh")
        //socket.emit("init", jsonEmit)

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
                        val jsonEmit = JSONObject()
                        jsonEmit.put("username", username)
                        socket.emit("init", jsonEmit)
                        login(success)
                    } catch (e : Exception) {
                        login(false)
                    }
                }
            }

            val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue
            val reqBody = JSONObject()
            username = edtUsername.text.toString()
            password = edtPassword.text.toString()
            reqBody.put("name", username)
            reqBody.put("password", password)



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
