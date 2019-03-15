package com.example.intproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_rooms.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class Chat : AppCompatActivity() {

    var userCache: HashMap<String, Bitmap> = HashMap()
    var count: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var id = ""

        try {
            id = intent.getStringExtra("id")
            val name: String = intent.getStringExtra("name")
            count = intent.getStringExtra("count").toInt()

            txtName.text = name
            updateCount()
        } catch (e: Exception) {
            txtName.text = "Err: " + e.toString()
        }

        val socket = SocketSingleton.getInstance(this.applicationContext).socket
        val tag = SocketSingleton.getInstance(this.applicationContext).tag

        Log.i(tag, "Joining..")
        val jsonEmit = JSONObject()
        jsonEmit.put("chatid", id)
        socket.emit("join", jsonEmit)

        socket.on("join") {
            count++
            runOnUiThread { updateCount() }
        }

        socket.on("leave") {
            count--
            runOnUiThread { updateCount() }
        }

        socket.on("message") {res ->
            val resJson: JSONObject = res[0] as JSONObject
            try {
                val sentBy = resJson.getString("username")
                val msg = resJson.getString("content")
                runOnUiThread { addMessageView(msg, sentBy, "Alldeles nyss!") }
            } catch (e: Exception) {
                Log.i(tag, e.toString())
            }
        }

        var url = Constants.urlHttp + "/room/" + id
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

        val req = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                try {
                    val messages: JSONArray = response.getJSONArray("messages")

                    for (i in 0..messages.length()-1) {
                        val message = messages.getJSONObject(i)

                        val msg = message.getString("message")
                        val sentBy = message.getString("sentby")
                        val sent = message.getString("sent")

                        addMessageView(msg, sentBy, sent)
                    }
                } catch (e : Exception) {
                    Log.i("Message", e.toString())
                }
            },
            Response.ErrorListener { error ->
                txtDebug.text = error.message
            })

        queue.add(req)

        btnSend.setOnClickListener {
            val jsonEmit = JSONObject()
            jsonEmit.put("content", edtMessage.text)
            socket.emit("message", jsonEmit)
            edtMessage.setText("")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            val socket = SocketSingleton.getInstance(this.applicationContext).socket
            val tag = SocketSingleton.getInstance(this.applicationContext).tag

            Log.i(tag, "Leaving..")
            socket.emit("leave")
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun updateCount() {
        txtCount.text = "Online: " + count
    }

    private fun addMessageView(msg: String, sentBy: String, sentDate: String) {
        val msgView = MessageView(this, sentBy, msg, sentDate)
        val queue = RequestSingleton.getInstance(this.applicationContext).requestQueue

        val u = userCache.get(sentBy)
        if (u != null) {
            msgView.setImage(u)
        } else {
            val url = Constants.urlHttp + "/profile/" + sentBy

            val req = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    val imgB64: String = response.getString("picture")
                    val decodedString: ByteArray = Base64.decode(imgB64, Base64.DEFAULT)
                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size-1)
                    msgView.setImage(decodedByte)
                    userCache.put(sentBy, decodedByte)
                },
                Response.ErrorListener { error ->
                    Log.i("Message", error.toString())
                })
            queue.add(req)
        }

        msgView.setOnClickListener {
            val username: String = sentBy

            val intent = Intent(this, Profile::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        linMessages.addView(msgView)
    }
}
