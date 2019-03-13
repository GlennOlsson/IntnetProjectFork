package com.example.intproject

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketSingleton constructor(context: Context) /*: Service*/ {

    val tag = "SocketSingleton"
    /*val socket: Socket by lazy {
        IO.socket("https://glennolsson.se/intnet/socket.io").connect()
    }*/
    //val socket: Socket = IO.socket("https://glennolsson.se/intnet/socket.io")
    val socket: Socket = IO.socket("https://socket-io-chat.now.sh/")


    init {
        //socket.setDe
        socket.connect()
        //createSocket("https://glennolsson.se/intnet")
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
/*
    private fun createSocket(url: String) {
        socket.connect()
            .on(Socket.EVENT_CONNECT, { Log.i(tag, " Connected")})
            .on(Socket.EVENT_DISCONNECT, { Log.i(tag, " Disconnected")})
    }
*/
}