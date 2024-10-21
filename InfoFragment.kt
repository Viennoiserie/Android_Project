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
import com.example.obesitron.databinding.FragmentHomeBinding
import com.example.obesitron.databinding.FragmentInfoBinding
import com.example.obesitron.databinding.FragmentStatBinding

class InfoFragment : Fragment() {

    companion object { private const val TAG = "InfoFragment" }

    private var _binding: FragmentInfoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        binding.imageBack.setOnClickListener { findNavController().navigate(R.id.action_InfoFragment_to_HomeFragment) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}