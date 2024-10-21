package com.example.obesitron.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.obesitron.FastFoodAdapter
import com.example.obesitron.answerClasses.Food
import com.example.obesitron.databinding.FragmentMenuselectionBinding
import com.google.gson.reflect.TypeToken
import java.io.IOException

class MenuSelection : Fragment() {

    companion object { private const val TAG = "MenuSelectionFragment" }

    private var _binding: FragmentMenuselectionBinding? = null

    private val binding get() = _binding!!

    private var className = ""
    private var classCals = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMenuselectionBinding.inflate(inflater, container, false)

        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        val prefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        // During the first call of the fragment : We take the values from the last fragment (the fast food's name)
        val name = arguments?.getString("value2").toString()

        // Checking which variables to use :
        val nameToUse : String
        val caloriesToUse : String

        if(name != "null") { // On the first creation of the fragment or ...

            nameToUse = name
            caloriesToUse = "0"
        }
        else { // On the rest (we use the values stored in the shared preferences )

            nameToUse = prefs.getString("fastFoodName", "DEFAULT").toString()
            caloriesToUse = prefs.getString("caloriesCurrent", "DEFAULT").toString()
        }

        className = nameToUse
        classCals = caloriesToUse

        container!!.post { binding.recyclerview.adapter = FastFoodAdapter(loadChosenFastFood(nameToUse), requireContext(), nameToUse, caloriesToUse) }
        return binding.root
    }

    override fun onPause() { // When the fragment is paused : we store the values in myPrefs
        super.onPause()

        val editor = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit()

        editor.apply() {

            editor?.remove("fastFoodName")
            editor?.remove("caloriesCurrent")
            editor?.commit()

            editor?.putString("fastFoodName", className)
            editor?.putString("caloriesCurrent", classCals)
            editor.commit()
        }

        Log.d(TAG, "Finishing the save")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Function used to read the json file attributed to each fast food
    private fun loadChosenFastFood(name : String) : List<Food> {

        val name1 = name.replace(" ", "")
        val fileName = "$name1.json"

        lateinit var jsonString : String

        try {

            jsonString = context?.assets?.open(fileName)?.bufferedReader().use { it!!.readText() }

        } catch (ioException : IOException) { Log.d(TAG, "Can't read menu file") }

        val foods = object : TypeToken<List<Food>>(){}.type
        return( Gson().fromJson(jsonString, foods))
    }
}

