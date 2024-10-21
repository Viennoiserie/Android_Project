package com.example.obesitron

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.obesitron.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    companion object { private const val TAG = "MenuActivity" }

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun goToMap(responseArray: DoubleArray) {

        val intent = Intent(this@MenuActivity, MapActivity::class.java)

        val b = Bundle()

        b.putSerializable("array", responseArray)
        intent.putExtras(b)

        startActivity(intent)
    }
}