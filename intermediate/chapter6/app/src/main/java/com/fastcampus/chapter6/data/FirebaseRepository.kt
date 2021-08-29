package com.fastcampus.chapter6.data

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseRepository {
    val articleDB by lazy { Firebase.database.reference.child(DB_ARTICLES) }

    private const val DB_ARTICLES = "Articles"
}