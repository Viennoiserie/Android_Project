package com.example.obesitron

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.obesitron.answerClasses.Food
import com.example.obesitron.databinding.RecyclerviewFastfooditemBinding

class FastFoodAdapter(private val foods: List<Food>, private val context : Context, private val name : String, private val calories : String) : RecyclerView.Adapter<FastFoodAdapter.FastFoodViewHolder>(){

    companion object { private const val TAG = "FastFoodAdapter" }

    init {}

    inner class FastFoodViewHolder(val binding: RecyclerviewFastfooditemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastFoodViewHolder {

        val binding = RecyclerviewFastfooditemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FastFoodViewHolder(binding)
    }

    override fun getItemCount(): Int = foods.size

    override fun onBindViewHolder(holder: FastFoodViewHolder, position: Int) {

        val food: Food = foods[position]

        holder.binding.textViewItem.text = food.name
        holder.binding.textViewCalories.text = "Calories : " + food.calories.toString()
        Glide.with(context).load(Uri.parse(food.img)).centerCrop().into(holder.binding.imageViewItem)

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.spawn_anim)

        holder.itemView.setOnClickListener {

            val cal = foods[holder.adapterPosition].calories.toString()
            val bigCal = (foods[holder.adapterPosition].calories.toString().toInt() + calories.toInt()).toString()
            val args = bundleOf("value3" to bigCal, "name" to name, "value33" to cal)

            holder.itemView.findNavController().navigate(R.id.action_MenuSelectionFragment_to_InBetweenFragmen2, args)
        }
    }
}