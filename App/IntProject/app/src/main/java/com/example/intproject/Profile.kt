package com.example.intproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var username: String = ""
        try {
            username = intent.getStringExtra("username")
        } catch (e: Exception) {
            //txtName.text = "Err: " + e.toString()
        }


        var url = "https://glennolsson.se/intnet/profile/" + username
        val queue = Volley.newRequestQueue(this)

        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val created: String = response.getString("created")
                    var pictureB64: String = response.getString("picture")
                    val bio: String = response.getString("bio")
                    val friends: JSONArray = response.getJSONArray("friends")
                    val comments: JSONArray = response.getJSONArray("comments")

                    txtProfileName.text = username
                    txtDescription.text = bio
                    val decodedString: ByteArray = Base64.decode(pictureB64, Base64.DEFAULT)
                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size-1)

                    imgProfilePic.setImageBitmap(decodedByte)
                } catch (e : Exception) {
                    txtDescription.text = "OnCreate: " + e.toString()
                }
            },
            Response.ErrorListener { error ->
                //txtDebug.text = error.message
            })

        queue.add(req)
        queue.start()
    }
}
