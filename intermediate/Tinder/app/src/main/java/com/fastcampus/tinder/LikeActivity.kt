package com.fastcampus.tinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.tinder.databinding.ActivityLikeBinding

class LikeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}