package com.example.intproject

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import kotlinx.android.synthetic.main.activity_rooms.*
import kotlinx.android.synthetic.main.room_view.view.*
import java.lang.Exception

class Rooms : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        // Add rooms dynamically
        try {
            linRooms.addView(createRoomView( "Snacket", 25))
            linRooms.addView(createRoomView( "Tugget", 31))
            linRooms.addView(createRoomView( "Käket", 11))
            linRooms.addView(createRoomView( "Hörnet", 27))
            linRooms.addView(createRoomView( "Fredagsmys", 14))
            linRooms.addView(createRoomView( "Chillkanalen", 32))
        } catch (e: Exception) {
            txtDebug.text = "OnCreate: " + e.toString()
        }
    }

    private fun enterRoom(v: View?) {
        try {
            val r: RoomView = v as RoomView
            val name: String = r.txtRoomName.text.toString()
            val count: String = r.txtOnlineCount.text.toString()

            val intent = Intent(this, Chat::class.java)
            intent.putExtra("name", name)
            intent.putExtra("count", count)
            startActivity(intent)
        } catch (e: Exception) {
            txtDebug.text = "EnterRoom: " + e.toString()
        }

    }

    private fun createRoomView(name: String, count: Int): RoomView {
        try {
            val room: RoomView = RoomView(this, name, count)
            room.setOnClickListener {
                enterRoom(room)
            }

            return room
        } catch (e: Exception) {
            txtDebug.text = "CreateRoom: " + e.toString()
        }
        return RoomView(this, "errorview", -1)
    }
}
