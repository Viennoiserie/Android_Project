package com.example.obesitron.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.obesitron.R
import com.example.obesitron.databinding.FragmentExplanationfastfoodBinding

class ExplanationFastFoodFragment : Fragment() {

    companion object { private const val TAG = "ExplanationFastFoodFragment" }

    private var _binding: FragmentExplanationfastfoodBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentExplanationfastfoodBinding.inflate(inflater, container, false)

        var name = arguments?.getString("value1").toString()
        var args = bundleOf("value2" to name)

        binding.imageView5.setOnClickListener { findNavController().navigate(R.id.action_InBetweenFragment1_to_FastFoodFragment) }
        binding.button.setOnClickListener { findNavController().navigate(R.id.action_InBetweenFragment1_to_MenuSelectionFragment, args) }

        when (name) {

            "McDonald's" -> {

                binding.mcdo.visibility = View.VISIBLE
                binding.bk.visibility = View.INVISIBLE
                binding.kfc.visibility = View.INVISIBLE
            }
            "Burger King" -> {

                binding.bk.visibility = View.VISIBLE
                binding.mcdo.visibility = View.INVISIBLE
                binding.kfc.visibility = View.INVISIBLE
            }
            "KFC" -> {

                binding.kfc.visibility = View.VISIBLE
                binding.mcdo.visibility = View.INVISIBLE
                binding.bk.visibility = View.INVISIBLE
            }
        }

        val txt = binding.textView.text.toString()
        binding.textView.text = "$txt $name"

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