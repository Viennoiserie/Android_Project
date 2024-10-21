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
import com.example.obesitron.databinding.FragmentValidationBinding

class ValidationFragment : Fragment() {

    companion object { private const val TAG = "ExplanationFastFoodFragment" }

    private var _binding: FragmentValidationBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentValidationBinding.inflate(inflater, container, false)

        val name = arguments?.getString("name").toString()
        val cals = arguments?.getString("value4").toString()
        val args = bundleOf("value5" to cals, "name" to name)

        val dist = (((cals.toInt()/500) * 7620)/1000).toString()

        binding.textDistance.text = "$dist Km"

        binding.btnCancel.setOnClickListener { findNavController().navigate(R.id.action_ValidationFragment_to_FastFoodFragment) }
        binding.btnProceed.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }
            else { findNavController().navigate(R.id.action_ValidationFragment_to_FinalFragment, args) }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}