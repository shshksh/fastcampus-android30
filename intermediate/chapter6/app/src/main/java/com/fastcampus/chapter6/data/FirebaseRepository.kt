package com.fastcampus.chapter6.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object FirebaseRepository {
    private val auth by lazy { Firebase.auth }

    val articleDB by lazy { Firebase.database.reference.child(DB_ARTICLES) }

    private val storage by lazy { Firebase.storage }
    val articleStorage by lazy { storage.reference.child("article/photo") }

    fun isLogin() = auth.currentUser != null

    fun currentUserId() = auth.currentUser?.uid.orEmpty()

    private const val DB_ARTICLES = "Articles"
}