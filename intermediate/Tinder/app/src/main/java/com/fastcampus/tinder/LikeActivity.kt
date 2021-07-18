package com.fastcampus.tinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.tinder.databinding.ActivityLikeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LikeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLikeBinding

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val userDB by lazy { Firebase.database.reference.child("Users") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUserDB = userDB.child(getCurrentUserID())
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            showToast("로그인이 되어있지 않습니다.")
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }
}