package com.example.intproject

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.Volley

class RequestSingleton constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: RequestSingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RequestSingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}