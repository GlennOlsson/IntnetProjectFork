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

    private var userCache: HashMap<String, Bitmap> = HashMap()
    private var editing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var loggedIn = Constants.loggedIn
        var token = Constants.token
        var username = intent.getStringExtra("username")

        var url = Constants.urlHttp + "/profile/" + username
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val created: String = response.getString("created")
                    var imgB64: String = response.getString("picture")
                    val bio: String = response.getString("bio")
                    val friends: JSONArray = response.getJSONArray("friends")
                    val comments: JSONArray = response.getJSONArray("comments")

                    txtProfileName.text = username
                    txtDescription.setText(bio)

                    val image = b64ToBitmap(imgB64)
                    imgProfilePic.setImageBitmap(image)
                    userCache.put(username, image)

                    for (i in 0..friends.length()-1) {
                        val friendName = friends.getString(i)

                        addFriendView(friendName)
                    }

                    for (i in 0..comments.length()-1) {
                        val comment = comments.getJSONObject(i)
                        val by = comment.getString("by")
                        val content = comment.getString("comment")
                        val date = comment.getString("date")

                        addCommentView(content, by, date)
                    }
                } catch (e : Exception) {
                    txtDescription.setText("OnCreate: " + e.toString())
                }
            },
            Response.ErrorListener { error ->
                Log.i("Profile", error.toString())
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
                    val success = response.getBoolean("success")

                    if (!success) {
                        val reason = response.getString("reason")
                        toastResponse(success, reason)
                    } else {
                        addCommentView(comment, loggedIn.toString(), "Alldeles nyss!")
                    }
                },
                Response.ErrorListener { error ->
                    Log.i("Profile", error.toString())
                })
            queue.add(req)
            edtComment.setText("")
        }

        btnFloating.setOnClickListener() {
            if (loggedIn == username) {
                if (editing) {
                    editing = !editing

                    val reqBody = JSONObject()
                    reqBody.put("token", token)
                    reqBody.put("name", loggedIn)
                    reqBody.put("bio", txtDescription.text.toString())

                    postRequest("/bio", reqBody, "Bio changed!")

                    txtDescription.isEnabled = false
                    txtDescription.inputType = InputType.TYPE_NULL
                } else {
                    editing = !editing

                    Toast.makeText(this, "Edit your description and save by pressing the button again", Toast.LENGTH_SHORT).show()

                    txtDescription.isEnabled = true
                    txtDescription.inputType = InputType.TYPE_CLASS_TEXT
                }
            } else {
                val reqBody = JSONObject()
                reqBody.put("token", token)
                reqBody.put("name", loggedIn)

                postRequest("/friend/"+username, reqBody, "Friend added!")
            }
        }
    }

    private fun addFriendView(name: String) {
        val friendView = FriendView(this, name)

        setUserImage(friendView, name)

        friendView.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("username", name)
            startActivity(intent)
        }

        linFriends.addView(friendView)
    }

    private fun addCommentView(comment: String, by: String, sentDate: String) {
        val commentView = CommentView(this, comment, by, sentDate)

        setUserImage(commentView, by)

        commentView.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("username", by)
            startActivity(intent)
        }

        linComments.addView(commentView)
    }

    private fun postRequest(url: String, body: JSONObject, defaultReason: String) {
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue
        val url = Constants.urlHttp + url

        val req = JsonObjectRequest(
            Request.Method.POST, url, body,
            Response.Listener<JSONObject> { response ->
                val success = response.getBoolean("success")
                var reason = defaultReason

                if (!success) {
                    reason = response.getString("reason")
                }

                toastResponse(success, reason)
            },
            Response.ErrorListener { error ->
                Log.i("Profile", error.toString())
            })
        queue.add(req)
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
                    val image = b64ToBitmap(imgB64)
                    v.setImage(image)
                    userCache.put(username, image)
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

    private fun b64ToBitmap(b64: String): Bitmap {
        val decodedString: ByteArray = Base64.decode(b64, Base64.DEFAULT)
        val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size-1)

        return decodedByte
    }
}
