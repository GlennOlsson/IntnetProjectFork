package com.example.intproject

import android.app.Application
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketSingleton {

    //private var notificationSingleton: NotificationManager? = null


    constructor(context: Context) {
        val notificationSingleton = NotificationSingleton.getInstance(context)

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

    val tag = "SocketSingleton"
    val socket: Socket by lazy {
        IO.socket(Constants.urlSocket)
    }

    private fun sendNotification() {
        //notificationManager
    }
}

/*
class SocketSingleton : Application() {

    val tag = "SocketSingleton"
    val socket: Socket by lazy {
        IO.socket("https://glennolsson.se/intnet/socket.io").connect()
    }
    val socket: Socket = IO.socket("http://glennolsson.se:8082")
    //val socket: Socket = IO.socket("http://130.229.129.92:8082/socket.io")


    init {
        instance = this

        socket.on(Socket.EVENT_CONNECT) {
            Log.i(tag, "Socket connected!")
        }
        socket.on(Socket.EVENT_DISCONNECT) {
            Log.i(tag, "Socket disconnected!")
        }
        socket.on(Socket.EVENT_ERROR) { error ->
            Log.i(tag, "Event Error: " + error[0].toString())
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) { error ->
            Log.i("ConErr", "Event Connect Error: " + error[0].toString())
        }
        socket.on("notification") {
            // do something
        }
        socket.connect()
    }


    companion object {
        private var instance: SocketSingleton? = null

        fun applicationContext() : SocketSingleton {
            return instance as SocketSingleton
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        socket.on(Socket.EVENT_CONNECT) {
            Log.i(tag, "Socket connected!")
        }
        socket.on(Socket.EVENT_DISCONNECT) {
            Log.i(tag, "Socket disconnected!")
        }
        socket.on(Socket.EVENT_ERROR) { error ->
            Log.i(tag, "Event Error: " + error[0].toString())
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) { error ->
            Log.i("ConErr", "Event Connect Error: " + error[0].toString())
        }
        socket.on("notification") {
            // do something
        }
        socket.connect()
    }

    companion object {
        lateinit var instance: SocketSingleton
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

    fun logSocket() {
        Log.i(tag, socket.connect().toString())
    }
}
*/