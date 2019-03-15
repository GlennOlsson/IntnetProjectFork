package com.example.intproject

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class CommentView : LinearLayout {

    constructor(context: Context, message: String, sentBy: String, sentDate: String) : super(context) {
        LinearLayout.inflate(context, R.layout.comment_view, this)

        val txtCommentMessage: TextView = findViewById(R.id.txtCommentMessage)
        val txtCommentInfo: TextView = findViewById(R.id.txtCommentInfo)

        txtCommentMessage.text = message
        txtCommentInfo.text = sentBy + ", " + sentDate
    }

    fun setImage(image: Bitmap) {
        val imgCommentPic: ImageView = findViewById(R.id.imgCommentPic)
        imgCommentPic.setImageBitmap(image)
    }
}