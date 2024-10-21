package com.example.obesitron

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.obesitron.answerClasses.SearchRouteResponse
import com.example.obesitron.apiServices.CallHopperAPI
import com.example.obesitron.apiServices.GraphHopperService
import com.example.obesitron.databinding.ActivityMapBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import java.util.*
import kotlin.math.roundToInt

@Suppress("DEPRECATION")

class MapActivity : AppCompatActivity() {

    companion object { private const val TAG = "MapActivity" }

    private lateinit var binding: ActivityMapBinding

    // Map variables

    private lateinit var map: MapView
    private lateinit var mapController : IMapController // Comes from the interface "IMapController" of the osm library

    // Icons variables

    /*

       Note : [0]

       J'ai du construire une classe qui implémente l'interface OnItemGestureListener pour pouvoir l'utiliser,
       Car kotlin n'accepte pas de constructeur (en direct) pour les interfaces !

   */

    private val itemListener = OverlayClass() // [0]
    private val listItems = arrayListOf<OverlayItem>() // Making a list of items to show on the map
    private lateinit var overlay: ItemizedOverlayWithFocus<OverlayItem> // Corresponds to the UI to be displayed above the map
    private var myPath = Polyline()

    private var indexOfCoords = 0
    private var canClick = false
    private var dist = 0

    // private val listRoutes = arrayListOf(arrayListOf(Double))

    /*

        Tentative ci-dessus de garder en mémoire les différentes routes,
        Afin de limiter le nombres d'appels d'API GraphHopper.

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // These bindings were done using the examples given by the kotlin documentation !
        binding = ActivityMapBinding.inflate(layoutInflater)

        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
        setContentView(binding.root)

        map = findViewById(R.id.map)   // Finding and instanciating the map
        mapController = map.controller // Instantiate once and for all the map controller !

        /* Setting up the map parameters : */

        map.setTileSource(TileSourceFactory.MAPNIK) // Used for map rendering
        map.setBuiltInZoomControls(false)            // Precising that the map can be zoomed on
        map.setMultiTouchControls(true)

        val array = intent.getDoubleArrayExtra("array")
        val length = array!!.size

        val centerPoint = GeoPoint(array[length-3], array[length-2])

        mapController.setZoom(10)             // Setting arbitrary zoom level
        mapController.setCenter(centerPoint)   // We put the center of the map to the coordinates we found earlier

        val yourPos = OverlayItem("Votre position", "", centerPoint)

        listItems.add(yourPos)

        // Now we take the values from the coordinates found before and register them into UI objects on the map
        if(length == null) {} // Case that cannot happen

        else if(length == 3)  // Case where nothing was found by the API

        else{

            for(i in 0 until length-2 step 2) {

                val fastFoodPoint = GeoPoint(array[i], array[i+1])
                lateinit var fastFoodItem: OverlayItem

                // We check what the received coordinates correspond to
                when(array[length-1]) {

                    -1.00 -> fastFoodItem = OverlayItem("NOHING WAS FOUND", "", GeoPoint(0,0))  // Case where nothing is given
                    1.00 -> fastFoodItem = OverlayItem("McDonald's", "", fastFoodPoint)
                    2.00 -> fastFoodItem = OverlayItem("KFC", "", fastFoodPoint)
                    3.00 -> fastFoodItem = OverlayItem("Burger King", "", fastFoodPoint)
                }

                listItems.add(fastFoodItem)
            }
        }

        overlay = ItemizedOverlayWithFocus(applicationContext, listItems, itemListener)
        overlay.setFocusItemsOnTap(true)
        map.overlays.add(overlay)

        binding.imageBack.setOnClickListener {

            val intent = Intent(this@MapActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        binding.btnFollow.setOnClickListener {

            // Checking if the distance found is the biggest !
            val prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            if(canClick) {

                if(prefs.contains("MaxDist")) {

                    val tempMax = prefs.getInt("MaxDist", 0)

                    if(tempMax < dist) {

                        editor.remove("MaxDist")
                        editor.commit()

                        editor.putInt("MaxDist", dist)
                        editor.commit()
                    }
                }
                else {

                    editor.putInt("MaxDist", dist)
                    editor.commit()
                }
            }
            mapController.setCenter(centerPoint)
            mapController.setZoom(20)
        }

        binding.buttonRoute.setOnClickListener {

            if(canClick) {

                canClick = false

                Log.d(TAG, indexOfCoords.toString())
                if(indexOfCoords == (array.size - 5)) { indexOfCoords = 0 }
                else { indexOfCoords += 2 }

                callRoute(array, length, centerPoint)
            }
        }

        // Route creation
        callRoute(array, length, centerPoint)
    }

    private fun callRoute(array : DoubleArray, length : Int, centerPoint : GeoPoint) {

        Log.d(TAG, "Calling Routing")
        GraphHopperService().getRoute(array[length-3], array[length-2], array[indexOfCoords], array[indexOfCoords+1], object: CallHopperAPI() {

            override fun response(data: SearchRouteResponse) {

                // Retrieve the data from the json format given by the API
                val routeArray = data.paths[0].points.coordinates
                val distance = (data.paths[0].distance / 1000).roundToInt()

                dist = distance

                canClick = true
                drawRoute(centerPoint, GeoPoint(array[indexOfCoords], array[indexOfCoords+1]), routeArray)
            }
        })
    }

    private fun drawRoute(start : GeoPoint, finish : GeoPoint, points : List<List<Double>>) {

        Log.d(TAG, "Drawing Route")

        if(map.overlays.contains(myPath)) {

            map.overlays.remove(myPath)
            map.overlays.remove(overlay)
            map.overlays.add(overlay)
        }

        myPath = Polyline()
        myPath.width = 2.5F
        myPath.color = Color.BLUE

        myPath.addPoint(start)

        var tempPoint: GeoPoint

        for(i in points.indices) {

            // We go through all the coordinates and add them to our Route
            tempPoint = GeoPoint(points[i][1],points[i][0])
            myPath.addPoint(tempPoint)
        }

        myPath.addPoint(finish)

        map.overlays.add(myPath)
        map.overlays.remove(overlay)
        map.overlays.add(overlay)
    }

    override fun onPause(){

        super.onPause()
        map.onPause()
    }

    override fun onResume(){

        super.onResume()
        map.onResume()
    }
}