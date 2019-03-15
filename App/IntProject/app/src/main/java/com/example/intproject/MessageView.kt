package com.example.intproject

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat.startActivity
import android.util.AttributeSet
import android.util.Base64
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.room_view.view.*
import org.json.JSONObject


class MessageView : LinearLayout {
    constructor(context: Context, sentBy: String, message: String, sentDate: String) : super(context) {
        inflate(context, R.layout.message_view, this)

        val txtMessage: TextView = findViewById(R.id.txtMessage)
        val txtMessageInfo: TextView = findViewById(R.id.txtMessageInfo)

        txtMessage.text = message
        txtMessageInfo.text = sentBy + ", " + sentDate
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

    fun setImage(image: Bitmap) {
        val imgProfilePic: ImageView = findViewById(R.id.imgProfilePic)
        imgProfilePic.setImageBitmap(image)
    }
}