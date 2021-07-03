package com.fastcampus.secretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.fastcampus.secretdiary.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {

    lateinit var binding: ActivityDiaryBinding

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailPref = getSharedPreferences("diary", Context.MODE_PRIVATE)
        binding.etDiary.setText(detailPref.getString("detail", ""))

        val runnable = Runnable {
            detailPref.edit {
                putString("detail", binding.etDiary.text.toString())
            }
        }

        binding.etDiary.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}