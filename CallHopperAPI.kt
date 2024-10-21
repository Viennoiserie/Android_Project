package com.example.obesitron.apiServices

import android.util.Log
import com.example.obesitron.answerClasses.SearchRouteResponse
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

abstract class CallHopperAPI : Callback {

    companion object { private const val TAG = "CallSearchRoute" }

    abstract fun response(data: SearchRouteResponse)

    override fun onFailure(call: Call, e: IOException) {
        Log.e(TAG, " -> Failed to get query", e)
    }

    override fun onResponse(call: Call, response: Response) {
        Log.e(TAG, " -> Query executed")

        val gson = Gson()
        val data: SearchRouteResponse = gson.fromJson(response.body!!.charStream(), SearchRouteResponse::class.java)

        response(data)
    }
}
