package com.example.obesitron.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.obesitron.R
import com.example.obesitron.databinding.FragmentFastfoodselectionBinding
import com.example.obesitron.databinding.FragmentHomeBinding
import com.example.obesitron.databinding.FragmentStatBinding

class StatFragment : Fragment() {

    companion object { private const val TAG = "StatFragment" }

    private var _binding: FragmentStatBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentStatBinding.inflate(inflater, container, false)

        binding.imageBack.setOnClickListener { findNavController().navigate(R.id.action_StatFragment_to_HomeFragment) }

        val prefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        var txt = binding.textViewName.text.toString()
        binding.textViewName.text = "$txt  ${prefs.getString("Nom", "").toString()}"

        txt = binding.textViewSurname.text.toString()
        binding.textViewSurname.text = "$txt  ${prefs.getString("Prenom", "").toString()}"

        txt = binding.textViewAge.text.toString()
        binding.textViewAge.text = "$txt  ${prefs.getString("Age", "").toString()}"

        txt = binding.textViewWeight.text.toString()
        binding.textViewWeight.text = "$txt  ${prefs.getString("Poids", "").toString()}"

        txt = binding.textViewMaxC.text.toString()

        if(!prefs.contains("MaxCals")) { binding.textViewMaxC.text = "$txt 0" }
        else { binding.textViewMaxC.text = "$txt  ${prefs.getInt("MaxCals", 0)}" }

        txt = binding.textViewMaxD.text.toString()

        if(!prefs.contains("MaxDist")) { binding.textViewMaxD.text = "$txt 0 Km" }
        else { binding.textViewMaxD.text = "$txt  ${prefs.getInt("MaxDist", 0)} Km" }

        txt = binding.textViewUses.text.toString()

        binding.textViewUses.text = "$txt  ${prefs.getInt("NbUse", 0)}"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}