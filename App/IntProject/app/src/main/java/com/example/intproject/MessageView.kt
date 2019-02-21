package com.example.intproject

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView

class MessageView : LinearLayout{

    constructor(context: Context, message: String) : super(context) {
        inflate(context, R.layout.room_view, this)

        val txtMessage: TextView = findViewById(R.id.txtMessage)
        txtMessage.text = message
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // Based on this custom view tutorial: https://medium.com/@douglas.iacovelli/the-beauty-of-custom-views-and-how-to-do-it-79c7d78e2088
        inflate(context, R.layout.room_view, this)

        val txtMessage: TextView = findViewById(R.id.txtMessage)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.MessageView)
            txtMessage.text = attributes.getString(R.styleable.MessageView_message)

            attributes.recycle()
        }
    }

}