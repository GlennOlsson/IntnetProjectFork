package com.example.intproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
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

    private var userCache: HashMap<String, Bitmap> = HashMap<String, Bitmap>()
    private var editing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var loggedIn = Constants.loggedIn
        var token = Constants.token
        var username: String = ""

        Log.i("Profile", loggedIn + " " + token)

        try {
            username = intent.getStringExtra("username")
        } catch (e: Exception) {
            //txtName.text = "Err: " + e.toString()
        }


        var url = "https://glennolsson.se/intnet/profile/" + username
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

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

                    for (i in 0..friends.length()-1) {
                        val friendName = friends.getString(i)

                        val friendView = FriendView(this, friendName)
                        setUserImage(friendView, friendName)

                        friendView.setOnClickListener {
                            val intent = Intent(this, Profile::class.java)
                            intent.putExtra("username", friendName)
                            startActivity(intent)
                        }
                        linFriends.addView(friendView)
                    }

                    for (i in 0..comments.length()-1) {
                        val comment = comments.getJSONObject(i)
                        val by = comment.getString("by")
                        val content = comment.getString("comment")
                        val date = comment.getString("date")

                        val commentView = CommentView(this, content)
                        setUserImage(commentView, by)
                        commentView.setOnClickListener {
                            val intent = Intent(this, Profile::class.java)
                            intent.putExtra("username", by)
                            startActivity(intent)
                        }
                        linComments.addView(commentView)
                    }
                } catch (e : Exception) {
                    txtDescription.text = "OnCreate: " + e.toString()
                }
            },
            Response.ErrorListener { error ->
                //txtDebug.text = error.message
            })

        queue.add(req)

        btnComment.setOnClickListener() {
            val comment = edtComment.text.toString()

            val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

            val url = "https://glennolsson.se/intnet/comment/" + username

            val reqBody = JSONObject()
            reqBody.put("token", token)
            reqBody.put("name", loggedIn)
            reqBody.put("comment", comment)

            val req = JsonObjectRequest(
                Request.Method.POST, url, reqBody,
                Response.Listener<JSONObject> { response ->
                    try {
                        val reason = response.getString("reason")
                    } catch (e: Exception) {

                    }
                    val success = response.getBoolean("success")


                    if (success) {
                        val commentView = CommentView(this, comment)
                        setUserImage(commentView, loggedIn.toString())
                        commentView.setOnClickListener {
                            val intent = Intent(this, Profile::class.java)
                            intent.putExtra("username", loggedIn.toString())
                            startActivity(intent)
                        }
                        linComments.addView(commentView)
                    } else {
                        Log.i("Profile", "no can do")
                    }
                },
                Response.ErrorListener { error ->
                    //txtDebug.text = error.message
                })
            queue.add(req)
        }

        btnFloating.setOnClickListener() {
            if (loggedIn == username) {
                if (editing) {
                    // save edits
                } else {
                    // start editing profile
                }
            } else {
                val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

                val url = "https://glennolsson.se/intnet/friend/" + username

                val reqBody = JSONObject()
                reqBody.put("token", token)
                reqBody.put("name", loggedIn)

                val req = JsonObjectRequest(
                    Request.Method.POST, url, reqBody,
                    Response.Listener<JSONObject> { response ->
                        val success = response.getBoolean("success")
                        if (success) {
                            // alert
                        } else {
                            val reason = response.getString("reason")
                        }
                    },
                    Response.ErrorListener { error ->
                        //txtDebug.text = error.message
                    })
                queue.add(req)
            }
        }
    }

    private fun setUserImage(v: CommentView, username: String) {
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

        val u = userCache.get(username)
        if (u != null) {
            v.setImage(u)
        } else {
            val url = "https://glennolsson.se/intnet/profile/" + username

            val req = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    val imgB64: String = response.getString("picture")
                    val decodedString: ByteArray = Base64.decode(imgB64, Base64.DEFAULT)
                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size-1)
                    v.setImage(decodedByte)
                    userCache.put(username, decodedByte)
                },
                Response.ErrorListener { error ->
                    //txtDebug.text = error.message
                })
            queue.add(req)
        }
    }

    private fun setUserImage(v: FriendView, username: String) {
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

        val u = userCache.get(username)
        if (u != null) {
            v.setImage(u)
        } else {
            val url = "https://glennolsson.se/intnet/profile/" + username

            val req = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    val imgB64: String = response.getString("picture")
                    val decodedString: ByteArray = Base64.decode(imgB64, Base64.DEFAULT)
                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size-1)
                    v.setImage(decodedByte)
                    userCache.put(username, decodedByte)
                },
                Response.ErrorListener { error ->
                    //txtDebug.text = error.message
                })
            queue.add(req)
        }
    }
}
