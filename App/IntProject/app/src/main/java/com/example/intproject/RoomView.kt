package com.example.intproject

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class RoomView : LinearLayout {
    var id : String = ""

    constructor(context: Context, name: String, onlineCount: Int, roomId: String) : super(context) {
        inflate(context, R.layout.room_view, this)

        val txtRoomName: TextView = findViewById(R.id.txtRoomName)
        val txtOnlineCount: TextView = findViewById(R.id.txtOnlineCount)
        txtRoomName.text = name
        txtOnlineCount.text = onlineCount.toString()

        id = roomId
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // Based on this custom view tutorial: https://medium.com/@douglas.iacovelli/the-beauty-of-custom-views-and-how-to-do-it-79c7d78e2088
        inflate(context, R.layout.room_view, this)

        val txtRoomName: TextView = findViewById(R.id.txtRoomName)
        val txtOnlineCount: TextView = findViewById(R.id.txtOnlineCount)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.RoomView)
            txtRoomName.text = attributes.getString(R.styleable.RoomView_name)
            txtOnlineCount.text = attributes.getString(R.styleable.RoomView_count)

            attributes.recycle()
        }
    }
}