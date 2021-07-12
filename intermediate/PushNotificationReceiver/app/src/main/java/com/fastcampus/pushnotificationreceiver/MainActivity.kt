package com.fastcampus.pushnotificationreceiver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.pushnotificationreceiver.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.tvFirebaseToken.text = task.result
            }
        }
    }
}