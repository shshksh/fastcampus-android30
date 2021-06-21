package com.fastcampus.secretdiary

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.secretdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberPickers()

        setOpenBtnListener()
    }

    private fun setOpenBtnListener() {
        binding.btnOpen.setOnClickListener {
            val passwordPref = getSharedPreferences("password", MODE_PRIVATE)

            val passwordFromUser = "${binding.np1.value}${binding.np2.value}${binding.np3.value}"

            if (passwordPref.getString("password", "000") == passwordFromUser) {
                // 패스워드 일치
                // TODO: 2021-06-21 다이어리 페이지 작성 후 startActivity
            } else {
                // 패스워드 불일치
                AlertDialog.Builder(this)
                    .setTitle("실패!!")
                    .setMessage("비밀번호가 잘못되었습니다.")
                    .setPositiveButton("확인") { _, _ -> }
                    .create()
                    .show()
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
}