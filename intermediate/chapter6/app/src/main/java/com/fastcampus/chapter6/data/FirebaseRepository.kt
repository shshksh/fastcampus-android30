package com.fastcampus.chapter6.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

object FirebaseRepository {
    private val auth by lazy { Firebase.auth }

    val articleDB by lazy { Firebase.database.reference.child(DB_ARTICLES) }
    val userDB by lazy { Firebase.database.reference.child(DB_USERS) }
    val chatDB by lazy {
        Firebase.database.reference.child(DB_USERS).child(currentUserId()).child(CHILD_CHAT)
    }

    private val storage by lazy { Firebase.storage }
    val articleStorage by lazy { storage.reference.child("article/photo") }

    fun isLogin() = auth.currentUser != null

    fun currentUserId() = auth.currentUser?.uid.orEmpty()

    fun signOut() {
        auth.signOut()
    }

    fun login(email: String, password: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    fun getEmail() = auth.currentUser?.email

    fun signUp(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    private const val DB_ARTICLES = "Articles"
    private const val DB_USERS = "Users"
    const val CHILD_CHAT = "chat"
}
