package com.fastcampus.secretdiary

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.fastcampus.secretdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberPickers()

        setOpenBtnListener()

        setChangePasswordBtnListener()
    }

    private fun setChangePasswordBtnListener() {
        binding.btnChangePassword.setOnClickListener {
            val passwordPref = getSharedPreferences("password", MODE_PRIVATE)
            val passwordFromUser =
                "${binding.np1.value}${binding.np2.value}${binding.np3.value}"

            if (changePasswordMode) {
                passwordPref.edit(true) {
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                binding.btnChangePassword.setBackgroundColor(Color.BLACK)
            } else {
                // 비밀번호 체크 후 changePasswordMode 활성화
                if (passwordPref.getString("password", "000") == passwordFromUser) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()

                    binding.btnChangePassword.setBackgroundColor(Color.RED)
                } else {
                    // 패스워드 불일치
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun setupNumberPickers() {
        binding.np1.apply {
            minValue = 0
            maxValue = 9
        }

        binding.np2.apply {
            minValue = 0
            maxValue = 9
        }

        binding.np3.apply {
            minValue = 0
            maxValue = 9
        }
    }

    private fun setOpenBtnListener() {
        binding.btnOpen.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPref = getSharedPreferences("password", MODE_PRIVATE)

            val passwordFromUser = "${binding.np1.value}${binding.np2.value}${binding.np3.value}"

            if (passwordPref.getString("password", "000") == passwordFromUser) {
                // 패스워드 일치
                // TODO: 2021-06-21 다이어리 페이지 작성 후 startActivity
            } else {
                // 패스워드 불일치
                showErrorAlertDialog()
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}