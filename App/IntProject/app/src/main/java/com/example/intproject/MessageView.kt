package com.example.intproject

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.room_view.view.*

//import kotlinx.android.synthetic.main.message_view.view.*

class MessageView : LinearLayout {

    // Should add dynamic placing of image/message depending on who sent the message
    // current user->msg to right, other user->msg to left
    constructor(context: Context, sentBy: String, message: String) : super(context) {
        inflate(context, R.layout.message_view, this)

        val imgProfilePic: ImageView = findViewById(R.id.imgProfilePic)
        val txtMessage: TextView = findViewById(R.id.txtMessage)
        /*
        imgProfilePic.setOnClickListener {
            val username: String = sentBy

            val intent = Intent(this, Chat::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }*/

        txtMessage.text = message
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // Based on this custom view tutorial: https://medium.com/@douglas.iacovelli/the-beauty-of-custom-views-and-how-to-do-it-79c7d78e2088
        inflate(context, R.layout.message_view, this)

        attrs?.let {
            val txtMessage: TextView = findViewById(R.id.txtMessage)
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.MessageView)
            txtMessage.text = attributes.getString(R.styleable.MessageView_message)
            // add sentby here as well

            attributes.recycle()
        }
    }
}