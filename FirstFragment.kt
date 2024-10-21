package com.example.obesitron.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.obesitron.R
import com.example.obesitron.databinding.FragmentFirstBinding
import com.example.obesitron.databinding.FragmentSecondBinding

class FirstFragment : Fragment() {

    companion object { private const val TAG = "FirstFragment" }

    private var _binding1: FragmentFirstBinding? = null
    private var _binding2: FragmentSecondBinding? = null

    private val binding1 get() = _binding1!!
    private val binding2 get() = _binding2!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val prefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if(!prefs.contains("NbUse")) {

            _binding1 = FragmentFirstBinding.inflate(inflater, container, false)

            binding1.btnSubmit.setOnClickListener {

                if(binding1.textInputName.text.isNullOrEmpty() || binding1.textInputEditAge.text.isNullOrEmpty() || binding1.textInputEditWeight.text.isNullOrEmpty() || binding1.textInputEditSurname.text.isNullOrEmpty()) { }
                else {

                    editor.putString("Nom", binding1.textInputName.text.toString())
                    editor.putString("Prenom", binding1.textInputEditSurname.text.toString())
                    editor.putString("Age", binding1.textInputEditAge.text.toString())
                    editor.putString("Poids", binding1.textInputEditWeight.text.toString())
                    editor.putInt("NbUse", 1)
                    editor.commit()

                    findNavController().navigate(R.id.action_FirstFragment_to_HomeFragment)
                }
            }

            return binding1.root
        }

        else {

            _binding2 = FragmentSecondBinding.inflate(inflater, container, false)

            binding2.button2.setOnClickListener { findNavController().navigate(R.id.action_FirstFragment_to_HomeFragment) }

            var nbUse = prefs.getInt("NbUse", 0)
            nbUse += 1

            prefs.edit().remove("NbUse").commit()
            prefs.edit().putInt("NbUse", nbUse).commit()

            Log.d(TAG, prefs.getInt("NbUse", 0).toString())

            return binding2.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding1 = null
        _binding2 = null
    }
}