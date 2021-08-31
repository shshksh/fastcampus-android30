package com.fastcampus.chapter6.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.fastcampus.chapter6.R
import com.fastcampus.chapter6.data.FirebaseRepository.getEmail
import com.fastcampus.chapter6.data.FirebaseRepository.isLogin
import com.fastcampus.chapter6.data.FirebaseRepository.login
import com.fastcampus.chapter6.data.FirebaseRepository.signOut
import com.fastcampus.chapter6.data.FirebaseRepository.signUp
import com.fastcampus.chapter6.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyPageBinding.bind(view)

        initViews()
    }

    private fun initViews() {
        initSignInOutButton()
        initSignUpButton()
        initEmailEditText()
        initPasswordEditText()
    }

    private fun initSignInOutButton() {
        binding.btnSignInOut.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (isLogin()) {
                signOut()
                binding.etEmail.text.clear()
                binding.etEmail.isEnabled = true
                binding.etPassword.text.clear()
                binding.etPassword.isEnabled = true

                binding.btnSignInOut.text = "로그인"
                binding.btnSignInOut.isEnabled = false
                binding.btnSignUp.isEnabled = false
            } else {
                login(email, password).addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        successSignIn()
                    } else {
                        Toast.makeText(
                            context,
                            "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun successSignIn() {
        if (!isLogin()) {
            Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        binding.etEmail.isEnabled = false
        binding.etPassword.isEnabled = false
        binding.btnSignUp.isEnabled = false
        binding.btnSignInOut.text = "로그아웃"
    }

    private fun initSignUpButton() {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            signUp(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initEmailEditText() {
        binding.etEmail.doOnTextChanged { _, _, _, _ ->
            val enable = binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()
            binding.btnSignUp.isEnabled = enable
            binding.btnSignInOut.isEnabled = enable
        }
    }

    private fun initPasswordEditText() {
        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            val enable = binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()
            binding.btnSignUp.isEnabled = enable
            binding.btnSignInOut.isEnabled = enable
        }
    }

    override fun onStart() {
        super.onStart()

        if (isLogin()) {
            binding.etEmail.setText(getEmail())
            binding.etEmail.isEnabled = false
            binding.etPassword.setText("********")
            binding.etPassword.isEnabled = false
            binding.btnSignInOut.text = "로그아웃"
            binding.btnSignInOut.isEnabled = true
            binding.btnSignUp.isEnabled = false
        } else {
            binding.etEmail.text.clear()
            binding.etEmail.isEnabled = true
            binding.etPassword.text.clear()
            binding.etPassword.isEnabled = true
            binding.btnSignInOut.text = "로그인"
            binding.btnSignInOut.isEnabled = false
            binding.btnSignUp.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}