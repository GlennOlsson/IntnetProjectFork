package com.example.intproject

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_rooms.*
import kotlinx.android.synthetic.main.room_view.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class Rooms : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        var url = "https://glennolsson.se/intnet/rooms"
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

        val req = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                try {
                    for (i in 0..response.length()-1) {
                        val room = response.getJSONObject(i)

                        val id = room.getString("id")
                        val name = room.getString("name")
                        val count = room.getInt("usercount")

                        linRooms.addView(createRoomView(name, count, id))
                    }
                } catch (e : Exception) {
                    txtDebug.text = "OnCreate: " + e.toString()
                }
            },
            Response.ErrorListener { error ->
                txtDebug.text = error.message
            })

        queue.add(req)
    }

    private fun enterRoom(v: View?) {
        try {
            val r: RoomView = v as RoomView
            val name: String = r.txtRoomName.text.toString()
            val count: String = r.txtOnlineCount.text.toString()
            val id: String = r.id

            val intent = Intent(this, Chat::class.java)
            intent.putExtra("name", name)
            intent.putExtra("count", count)
            intent.putExtra("id", id)
            startActivity(intent)
        } catch (e: Exception) {
            txtDebug.text = "EnterRoom: " + e.toString()
        }

    }

    private fun createRoomView(name: String, count: Int, id: String): RoomView {
        try {
            val room: RoomView = RoomView(this, name, count, id)
            room.setOnClickListener {
                enterRoom(room)
            }
            return room
        } catch (e: Exception) {
            txtDebug.text = "CreateRoom: " + e.toString()
        }
        return RoomView(this, "errorview", -1, "")
    }
}
