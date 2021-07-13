package com.fastcampus.thoughtoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.thoughtoftheday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.viewPager.adapter = QuotesPagerAdapter(emptyList())
    }
}