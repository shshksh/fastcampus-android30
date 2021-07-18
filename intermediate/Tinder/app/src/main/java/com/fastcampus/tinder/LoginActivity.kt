package com.fastcampus.tinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.fastcampus.tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val callbackManager by lazy { CallbackManager.Factory.create() }

    private lateinit var binding: ActivityLoginBinding

    private val facebookLoginCallback by lazy {
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val credential =
                    FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            handleSuccessLogin()
                        } else {
                            showToast("페이스북 로그인이 실패했습니다.")
                        }
                    }
            }

            override fun onCancel() {}

            override fun onError(error: FacebookException?) {
                showToast("페이스북 로그인이 실패했습니다.")
            }
        }
    }

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

            btnFacebookLogin.setPermissions("email", "public_profile")
            btnFacebookLogin.registerCallback(callbackManager, facebookLoginCallback)
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
                    handleSuccessLogin()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSuccessLogin() {
        if (auth.currentUser == null) {
            showToast("로그인에 실패했습니다.")
            return
        }

        saveUserID()
        finish()
    }

    private fun saveUserID() {
        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)
    }

}