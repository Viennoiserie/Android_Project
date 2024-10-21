package com.example.obesitron.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.obesitron.R
import com.example.obesitron.databinding.FragmentFastfoodselectionBinding

class FastFoodSelection : Fragment() {

    companion object { private const val TAG = "FastFoodSelectionFragment" }

    private var _binding: FragmentFastfoodselectionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFastfoodselectionBinding.inflate(inflater, container, false)

        binding.imageView5.setOnClickListener { findNavController().navigate(R.id.action_FastFoodFragment_to_HomeFragment) }

        binding.btnMcdo.setOnClickListener() {

            var value = "McDonald's"
            var bundle = bundleOf("value1" to value)
            findNavController().navigate(R.id.action_FastFoodFragment_to_InBetweenFragment1, bundle)
        }

        binding.btnKfc.setOnClickListener() {

            var value = "KFC"
            var bundle = bundleOf("value1" to value)
            findNavController().navigate(R.id.action_FastFoodFragment_to_InBetweenFragment1, bundle)
        }

        binding.btnBk.setOnClickListener() {

            var value = "Burger King"
            var bundle = bundleOf("value1" to value)
            findNavController().navigate(R.id.action_FastFoodFragment_to_InBetweenFragment1, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}