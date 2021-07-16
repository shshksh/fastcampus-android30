package com.fastcampus.tinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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
            btnSignUp.setOnClickListener { trySignUp() }
            btnLogin.setOnClickListener { tryLogin() }

            etEmail.addTextChangedListener { checkEmailPasswordInput() }
            etPassword.addTextChangedListener { checkEmailPasswordInput() }
        }
    }

    private fun ActivityLoginBinding.trySignUp() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity) { task ->
                if (task.isSuccessful) {
                    showToast("회원가입에 성공했습니다. 로그인 버튼을 눌러 로그인 해주세요.")
                } else {
                    showToast("이미 가입한 이메일이거나, 회원가입에 실패했습니다.")
                }
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
                    showToast("로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.")
                }
            }
    }

    private fun ActivityLoginBinding.checkEmailPasswordInput() {
        val enable = etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()
        btnSignUp.isEnabled = enable
        btnLogin.isEnabled = enable
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this@LoginActivity,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}