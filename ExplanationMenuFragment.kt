package com.example.obesitron.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.obesitron.R
import com.example.obesitron.databinding.FragmentExplanationmenuBinding

class ExplanationMenuFragment : Fragment() {

    companion object { private const val TAG = "ExplanationFastFoodFragment" }

    private var _binding: FragmentExplanationmenuBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentExplanationmenuBinding.inflate(inflater, container, false)

        // We take what was given in the arguments of the "FastFoodAdapter" class
        val name = arguments?.getString("name").toString()
        val cals = arguments?.getString("value3").toString()
        val args = bundleOf("value4" to cals, "name" to name) // And make a bundle out of them

        var canRemove = true // Variable used to check if we can (or not) remove the latest item of our menu

        val tempTxt = binding.txtCalories.text.toString()
        binding.txtCalories.text = "$tempTxt $cals calories"

        val editor = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE).edit()

        // We change the shared preferences of the maximum calories in order to find them when going back to "MenuFragment"
        editor.remove("caloriesCurrent")
        editor.commit()

        editor.putString("caloriesCurrent", cals)
        editor.commit()

        binding.btnAdd.setOnClickListener { findNavController().navigate(R.id.action_InBetweenFragment2_to_MenuSelectionFragment) }
        binding.btnRemove.setOnClickListener {

            if(canRemove) {

                val itemCals = arguments?.getString("value33").toString().toInt()
                val newMaxCals = (cals.toInt() - itemCals).toString()

                editor.remove("caloriesCurrent")
                editor.commit()

                editor.putString("caloriesCurrent", newMaxCals)
                editor.commit()

                binding.txtCalories.text = "$tempTxt $newMaxCals calories"

                canRemove = false
            }
        }

        binding.btnValidate.setOnClickListener {


            val prefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()

            if(prefs.contains("MaxCals")) {

                val tempMax = prefs.getInt("MaxCals", 0)

                if(tempMax < cals.toInt()) {

                    editor.remove("MaxCals")
                    editor.commit()

                    editor.putInt("MaxCals", cals.toInt())
                    editor.commit()
                }
            }
            else {

                editor.putInt("MaxCals", cals.toInt())
                editor.commit()
            }

            findNavController().navigate(R.id.action_InBetweenFragmen2_to_ValidationFragment, args)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}