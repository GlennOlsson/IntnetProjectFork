package com.example.intproject

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class FriendView : LinearLayout {

    constructor(context: Context, name: String) : super(context) {
        inflate(context, R.layout.friend_view, this)

        val txtFriendName: TextView = findViewById(R.id.txtFriendName)
        txtFriendName.text = name
    }

    fun setImage(image: Bitmap) {
        val imgFriendPic: ImageView = findViewById(R.id.imgFriendPic)
        imgFriendPic.setImageBitmap(image)
    }
}