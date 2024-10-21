package com.example.obesitron.apiServices

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.roundToInt

class OverpassService {

    companion object { private const val TAG = "OverpassService" }

    // I tried changing the timeouts using the examples on this website, but the functions were not recognized
    // Link : https://www.baeldung.com/okhttp-timeouts

    fun searchFastFood(name: String, pos: Pair<Double, Double>, dist: Int, callback: CallOverpassAPI) {

        val boxData = calculateDistance(pos, (dist*0.75).roundToInt())

        val stringBoxData = "bbox:" + boxData[0].first.toString() + "," +
                                      boxData[0].second.toString() + "," +
                                      boxData[1].first.toString() + "," +
                                      boxData[1].second.toString()

        Log.d(TAG, stringBoxData)

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("http://overpass-api.de/api/interpreter?data=[${stringBoxData}][out:json];node[\"name\"=\"${name}\"];out;").build()

        client.newCall(request).enqueue(callback)
        Log.d(TAG, " -> Searching fastfood : $name")
        Log.d(TAG, dist.toString())
    }

    fun searchPreciseFastFood(name: String, pos: Pair<Double, Double>, dist: Int, callback: CallOverpassAPI) {

        val boxData = calculateDistance(pos, dist + 15000)

        val stringBoxData = "bbox:" + boxData[0].first.toString() + "," +
                                      boxData[0].second.toString() + "," +
                                      boxData[1].first.toString() + "," +
                                      boxData[1].second.toString()

        Log.d(TAG, stringBoxData)

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("http://overpass-api.de/api/interpreter?data=[${stringBoxData}][out:json];node[\"name\"=\"${name}\"];out;").build()

        client.newCall(request).enqueue(callback)
        Log.d(TAG, " -> Searching precisely fastfood : $name")
        Log.d(TAG, ((dist + 0.25*dist).toInt().toString()))
    }

    private fun calculateDistance(pos: Pair<Double, Double>, dist: Int): ArrayList<Pair<Double, Double>> {

        // En attendant cela fonctionne à peu près MAIS nous devrions plutôt utiliser la formule de Haversine
        val degrees : Int = dist / 5

        val array = arrayListOf<Pair<Double, Double>>()

        val smallCoords = Pair(pos.first - degrees * 0.0001, pos.second - degrees * 0.0001)
        array.add(smallCoords)

        val bigCoords = Pair(pos.first + degrees * 0.0001, pos.second + degrees * 0.0001)
        array.add(bigCoords)

        return(array)
    }
}