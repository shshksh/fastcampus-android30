package com.fastcampus.tinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.tinder.databinding.ActivityMatchedUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchedUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchedUserBinding

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val userDB by lazy { Firebase.database.reference.child("Users") }

    private val adapter = MatchedUserAdapter()
    private val cardItems = mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchedUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMatchedUserRecyclerView()
    }

    private fun initMatchedUserRecyclerView() {
        binding.rvMatchedUser.adapter = adapter
        binding.rvMatchedUser.layoutManager = LinearLayoutManager(this)
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            showToast("로그인이 되어있지 않습니다.")
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }

}