package com.fastcampus.tinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        with(binding) {
            btnSignUp.setOnClickListener {
                
            }
            btnLogin.setOnClickListener { tryLogin() }
        }
    }

    private fun ActivityLoginBinding.tryLogin() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}