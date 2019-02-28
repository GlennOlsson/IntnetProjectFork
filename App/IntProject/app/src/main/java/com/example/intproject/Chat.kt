package com.example.intproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_rooms.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var id : String = ""

        try {
            id = intent.getStringExtra("id")
            val name: String = intent.getStringExtra("name")
            val count: String = intent.getStringExtra("count")

            txtName.text = name
            txtCount.text = "Online: " + count

        } catch (e: Exception) {
            txtName.text = "Err: " + e.toString()
        }

        var url = "http://130.229.132.61/room/" + id
        val queue = Volley.newRequestQueue(this)

        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val messages: JSONArray = response.getJSONArray("messages")

                    for (i in 0..messages.length()-1) {
                        val message = messages.getJSONObject(i)

                        val msg = message.getString("message")
                        val sentBy = message.getString("sentby")
                        //val sent = room.getInt("sent")

                        linMessages.addView(MessageView(this, msg))
                    }
                } catch (e : Exception) {
                    txtDebug.text = "OnCreate: " + e.toString()
                }
            },
            Response.ErrorListener { error ->
                txtDebug.text = error.message
            })

        queue.add(req)
        queue.start()
    }
}
