package com.example.intproject

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
//import kotlinx.android.synthetic.main.message_view.view.*

class MessageView : LinearLayout {

    // Should add dynamic placing of image/message depending on who sent the message
    // current user->msg to right, other user->msg to left
    constructor(context: Context, message: String) : super(context) {
        inflate(context, R.layout.message_view, this)

        val txtMessage: TextView = findViewById(R.id.txtMessage)
        txtMessage.text = message
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // Based on this custom view tutorial: https://medium.com/@douglas.iacovelli/the-beauty-of-custom-views-and-how-to-do-it-79c7d78e2088
        inflate(context, R.layout.message_view, this)

        attrs?.let {
            val txtMessage: TextView = findViewById(R.id.txtMessage)
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.MessageView)
            txtMessage.text = attributes.getString(R.styleable.MessageView_message)

            attributes.recycle()
        }
    }
}