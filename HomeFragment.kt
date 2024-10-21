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
import com.example.obesitron.databinding.FragmentInfoBinding

class HomeFragment : Fragment() {

    companion object { private const val TAG = "HomeFragment" }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val prefs = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        binding.buttonOrder.setOnClickListener { findNavController().navigate(R.id.action_HomeFragment_to_FastFoodFragment) }
        binding.imageProfile.setOnClickListener { findNavController().navigate(R.id.action_HomeFragment_to_StatFragment) }
        binding.imageInfo.setOnClickListener { findNavController().navigate(R.id.action_HomeFragment_to_InfoFragment) }

        if(prefs.contains("Nom") && prefs.contains("Prenom")) {

            val name = prefs.getString("Nom", "")
            val surname = prefs.getString("Prenom", "")

            val txt = binding.bienvenuTXT.text.toString()

            binding.bienvenuTXT.text = "$txt $name $surname"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}