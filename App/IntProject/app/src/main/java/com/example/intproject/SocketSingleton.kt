package com.example.intproject

import android.content.Context
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketSingleton {

    val tag = "SocketSingleton"
    val socket: Socket by lazy {
        IO.socket(Constants.urlSocket)
    }

    constructor(context: Context) {

        socket.on(Socket.EVENT_CONNECT) {
            Log.i(tag, "Socket connected!")
            //notificationSingleton.sendNotification("Title", "I am connected")
        }
        socket.on(Socket.EVENT_DISCONNECT) {
            Log.i(tag, "Socket disconnected!")
        }
        socket.on(Socket.EVENT_ERROR) { error ->
            Log.i(tag, "Event Error: " + error[0].toString())
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) { error ->
            Log.i(tag, "Event Connect Error: " + error[0].toString())
        }
        socket.on(Socket.EVENT_CONNECTING) {
            Log.i(tag, "Socket connectING!")
        }
        socket.on(Socket.EVENT_CONNECT_TIMEOUT) {
            Log.i(tag, "Socket connection timed out!")
        }
        socket.on(Socket.EVENT_MESSAGE) {
            Log.i(tag, "Socket message!")
        }

        val notificationSingleton = NotificationSingleton.getInstance(context)
        socket.on("notification") { res ->
            Log.i(tag, "Det funkar kanske?? " + res[0].toString())
            val resJson: JSONObject = res[0] as JSONObject
            val title = "Pling Plong"
            val content = resJson.getString("message")
            notificationSingleton.sendNotification(title, content)
        }

        socket.connect()
    }

    companion object {
        @Volatile
        private var INSTANCE: SocketSingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SocketSingleton(context).also {
                    INSTANCE = it
                }
            }
    }
}