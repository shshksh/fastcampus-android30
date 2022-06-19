package com.fastcampus.ch1youtube

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.ch1youtube.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun updateMotionProgress(progress: Float) {
        binding.motionLayout.progress = progress
    }
}
