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

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // Based on this custom view tutorial: https://medium.com/@douglas.iacovelli/the-beauty-of-custom-views-and-how-to-do-it-79c7d78e2088
        LinearLayout.inflate(context, R.layout.comment_view, this)

        attrs?.let {
            /*
            Finish or remove completely?
            val txtFriendName: TextView = findViewById(R.id.txtFriendName)
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.FriendView)
            txtFriendName.text = attributes.getString(R.styleable.FriendView_name)
            // add sentby here as well

            attributes.recycle()
            */
        }
    }

    fun setImage(image: Bitmap) {
        val imgCommentPic: ImageView = findViewById(R.id.imgCommentPic)
        imgCommentPic.setImageBitmap(image)
    }
}