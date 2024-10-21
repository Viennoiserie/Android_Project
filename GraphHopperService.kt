package com.example.obesitron.apiServices

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

class GraphHopperService {

    companion object { private const val TAG = "GraphHopperService" }

    private val key = "aa8aede1-f8c6-4c45-a2b1-ed87abe05b51"

    fun getRoute(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double, callback : CallHopperAPI) {

        val link = "https://graphhopper.com/api/1/route?point=${lat1},${lon1}&point=${lat2},${lon2}&profile=foot&vehicle=car&key=${key}&type=json&points_encoded=false"

        Log.d(TAG, link)

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("https://graphhopper.com/api/1/route?point=${lat1},${lon1}&point=${lat2},${lon2}&profile=foot&vehicle=car&key=${key}&type=json&points_encoded=false").build()

        client.newCall(request).enqueue(callback)
        Log.d(TAG, " Routing !")
    }
}