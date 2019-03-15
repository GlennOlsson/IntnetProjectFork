package com.example.intproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
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


        var url = Constants.urlHttp + "/profile/" + username
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
                    txtDescription.setText(bio)
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

                        val commentView = CommentView(this, content, by, date)
                        setUserImage(commentView, by)
                        commentView.setOnClickListener {
                            val intent = Intent(this, Profile::class.java)
                            intent.putExtra("username", by)
                            startActivity(intent)
                        }
                        linComments.addView(commentView)
                    }
                } catch (e : Exception) {
                    txtDescription.setText("OnCreate: " + e.toString())
                }
            },
            Response.ErrorListener { error ->
                //txtDebug.text = error.message
            })

        queue.add(req)

        btnComment.setOnClickListener() {
            val comment = edtComment.text.toString()

            val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

            val url = Constants.urlHttp + "/comment/" + username

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
                        val commentView = CommentView(this, comment, loggedIn.toString(), "Alldeles nyss!")
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
            edtComment.setText("")
        }

        btnFloating.setOnClickListener() {
            if (loggedIn == username) {
                if (editing) {
                    editing = !editing

                    // send edits
                    val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

                    val url = Constants.urlHttp + "/bio"

                    val reqBody = JSONObject()
                    reqBody.put("token", token)
                    reqBody.put("name", loggedIn)
                    reqBody.put("bio", txtDescription.text.toString())

                    val req = JsonObjectRequest(
                        Request.Method.POST, url, reqBody,
                        Response.Listener<JSONObject> { response ->
                            val success = response.getBoolean("success")
                            var reason = "Bio changed!"
                            if (!success) {
                                reason = response.getString("reason")
                            }

                            toastResponse(success, reason)
                        },
                        Response.ErrorListener { error ->
                            //txtDebug.text = error.message
                        })
                    queue.add(req)

                    txtDescription.isEnabled = false
                    txtDescription.inputType = InputType.TYPE_NULL
                } else {
                    editing = !editing

                    // signal that editing mode is active

                    txtDescription.isEnabled = true
                    txtDescription.inputType = InputType.TYPE_CLASS_TEXT
                }
            } else {
                val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

                val url = Constants.urlHttp + "/friend/" + username

                val reqBody = JSONObject()
                reqBody.put("token", token)
                reqBody.put("name", loggedIn)

                val req = JsonObjectRequest(
                    Request.Method.POST, url, reqBody,
                    Response.Listener<JSONObject> { response ->
                        val success = response.getBoolean("success")
                        var reason = "Friend added!"
                        if (!success) {
                            reason = response.getString("reason")
                        }

                        toastResponse(success, reason)
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
            val url = Constants.urlHttp + "/profile/" + username

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
            val url = Constants.urlHttp + "/profile/" + username

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

    private fun toastResponse(success: Boolean, reason: String) {
        var status = "Fail"

        if (success) {
            status = "Success"
        }

        Toast.makeText(this, status + ": " + reason, Toast.LENGTH_SHORT).show()
    }
}
