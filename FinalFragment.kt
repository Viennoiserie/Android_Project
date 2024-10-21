package com.example.obesitron.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.obesitron.answerClasses.SearchFastFoodResponse
import com.example.obesitron.apiServices.CallOverpassAPI
import com.example.obesitron.MenuActivity
import com.example.obesitron.apiServices.OverpassService
import com.example.obesitron.databinding.FragmentFinalBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FinalFragment : Fragment() {

    companion object { private const val TAG = "FinalFragment" }

    private var _binding: FragmentFinalBinding? = null

    private val binding get() = _binding!!

    // Instantiating my threads for later
    private val job = Job()
    private val uiScope1 = CoroutineScope((Dispatchers.Main + job))
    private val uiScope2 = CoroutineScope((Dispatchers.Main + job))
    private val uiScope3 = CoroutineScope((Dispatchers.Main + job))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFinalBinding.inflate(inflater, container, false)

        binding.progressBar.setProgress(0, false)
        binding.indicatorText.text  = ""

        getLocation(LocationServices.getFusedLocationProviderClient(requireActivity()))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //region getLocation Function

    private fun getLocation(provider: FusedLocationProviderClient) {

        binding.indicatorText.text = "Requesting GPS location"

        // We send a request to get our own location
        val request = provider.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY,

            // Cancellation token : used when willing to sending location requests
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
                override fun isCancellationRequested() = false
            }
        )

        // We check for permissions once again (because it is needed, even though they were checked before !)
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }

        binding.progressBar.setProgress(10, true)
        binding.indicatorText.text = "Finding GPS location"

        // What we do if the GPS request succeeds :
        request.addOnSuccessListener {

            if (it != null) {

                // Declaring the variables as global (when inside this if statement)
                var rep1 = false // Used as a flag
                var rep2 = false // used as a flag

                var resp1 = DoubleArray(0) // The array of the first API call
                var resp2 = DoubleArray(0) // The array of the second API call

                var size1 = 0 // The size of the first API call
                var size2 = 0 // The size of the second API call

                val pair = Pair(it.latitude, it.longitude) // Our position

                // If it succeeds we call the overpass API
                val value = arguments?.getString("value5")
                val name = arguments?.getString("name")

                binding.progressBar.setProgress(50, true)
                binding.indicatorText.text = "Finding your requested fast foods"

                uiScope1.launch { // Launch the first thread : the first API call

                    Log.d(TAG, "1 Started")

                    OverpassService().searchFastFood(name.toString(), pair, calsToDist(value.toString()), object: CallOverpassAPI() {

                        override fun response(data: SearchFastFoodResponse) {

                            // We get the array's length
                            size1 = data.elements.size

                            Log.d(TAG, "size1 : $size1")

                            var j = 0

                            // In the array we need all our answers (lat / long)
                            resp1 = DoubleArray(size1 * 2)

                            for(i in 0 until size1) {

                                resp1[j] = data.elements[i].lat
                                j++
                                resp1[j] = data.elements[i].lon
                                j++
                            }

                            rep1 = true // We indicate that this step is finished

                            if(binding.progressBar.progress == 50) { binding.progressBar.setProgress(75, false) }
                            else if(binding.progressBar.progress == 75) { binding.progressBar.setProgress(100, false) }
                        }
                    })
                }

                uiScope2.launch{ // Launch the second thread : second API call

                    Log.d(TAG, "2 Started")

                    OverpassService().searchPreciseFastFood(name.toString(), pair, calsToDist(value.toString()), object: CallOverpassAPI() {

                        override fun response(data: SearchFastFoodResponse) {

                            // We get the array's length
                            size2 = data.elements.size

                            Log.d(TAG, "size2 : $size2")

                            var j = 0

                            // In the array we need all our answers (lat / long)
                            resp2 = DoubleArray(size2 * 2)

                            for(i in 0 until size2) {

                                resp2[j] = data.elements[i].lat
                                j++
                                resp2[j] = data.elements[i].lon
                                j++
                            }

                            rep2 = true // We indicate that this step is finished

                            if(binding.progressBar.progress == 50) { binding.progressBar.setProgress(75, false) }
                            else if(binding.progressBar.progress == 75) { binding.progressBar.setProgress(100, false) }
                        }
                    })
                }

                uiScope3.launch{ // Launch the third thread : used to wait for the 2 API calls to be finished

                    Log.d(TAG, "3 started")

                    while(!rep1 || !rep2){ Log.d(TAG, "1 : $rep1 et 2 : $rep2") }

                    // We take the values that are different between the two arrays
                    val arrayToSend = compareArray(resp1, resp2, size1*2, size2*2)

                    // The last two (that are automatically 0.00 and 0.00) indexes are filled with our own coordinates
                    arrayToSend[arrayToSend.size - 3] = it.latitude
                    arrayToSend[arrayToSend.size - 2] = it.longitude

                    when(name.toString()) {

                        "McDonald's" -> arrayToSend[arrayToSend.size - 1] = 1.00
                        "KFC" -> arrayToSend[arrayToSend.size - 1] = 2.00
                        "Burger King" -> arrayToSend[arrayToSend.size - 1] = 3.00
                    }

                    binding.indicatorText.text = "Processes finished ... Moving to map !"
                    Log.d(TAG, "Coroutines finished !")
                    job.cancel()

                    // We then switch to the map activity WITH the found coordinates
                    (activity as MenuActivity).goToMap(arrayToSend)
                }
            }
        }

        request.addOnFailureListener { Log.d(TAG, "Failed to find coordinates") }
    }

    //endregion

    private fun calsToDist(cals : String) : Int {

        // 10 000 steps = 500 calories AND 10 000 steps = 7620m (on average)
        val calories = cals.toInt()
        val nb = (calories/500)
        val res = 7620 * nb

        return(res)
    }

    // region compareArray Function (Could more efficient : but it works fine)

    private fun compareArray(arr1: DoubleArray, arr2: DoubleArray, l1: Int, l2: Int): DoubleArray {

        /*

            Our API calls are basically : one square and another bigger square
            Therefore, the bigger square will have the same elements as the smaller one + new elements
            I only want those new elements ! So the result will be as long as the difference in size between the two answers of our API

         */

        val resLength = l2 - l1 + 2 + 1 // + our coordinates (given earlier by the GPS) + a number indicating which fast food was chosen

        Log.d(TAG, resLength.toString())

        val resArray = DoubleArray(resLength)

        var ij = 0 // Index that will iterate through our resArray
        var isIn = false

        lateinit var pair1: Pair<Double, Double>
        lateinit var pair2: Pair<Double, Double>

        for(i in 0 until l1 step 2) { // We check pair by pair if the elements of the first array are contained in the second one !

            isIn = false

            pair1 = Pair(arr1[i], arr1[i+1])

            for(j in 0 until l2 step 2) {

                pair2 = Pair(arr2[j], arr2[j+1])

                if(pair1 == pair2) {

                    arr2[j] = 0.00
                    arr2[j+1] = 0.00
                    isIn = true
                    break
                }
            }

            if(!isIn) {

                resArray[ij] = arr1[i]
                ij++
                resArray[ij] = arr1[i+1]
                ij++
            }
        }

        for(i in 0 until l2 step 2) { // Now we take the remaining elements

            pair2 = Pair(arr2[i], arr2[i+1])

            if(pair2 != Pair(0.00, 0.00)) {

                resArray[ij] = arr2[i]
                ij++
                resArray[ij] = arr2[i+1]
                ij++
            }
        }

        // These elements will, in the future, be filled by our GPS coordinates and the food indicator
        resArray[resLength - 3] = -1.00
        resArray[resLength - 2] = -1.00
        resArray[resLength - 1] = -1.00

        return(resArray)
    }

    //endregion
}