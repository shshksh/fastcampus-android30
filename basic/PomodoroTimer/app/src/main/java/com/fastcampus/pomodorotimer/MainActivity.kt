package com.fastcampus.pomodorotimer

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.pomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViews()
    }

    private fun bindViews() {
        binding.sbTimer.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    updateRemainTime(progress * 60L * 1000L)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                timer?.cancel()
                timer = null
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                timer = createCountDownTimer(binding.sbTimer.progress * 1000L * 60L)
                timer?.start()
            }

        })
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekbar(millisUntilFinished)
            }

            override fun onFinish() {
                updateRemainTime(0L)
                updateSeekbar(0L)
            }
        }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        binding.tvRemainMinutes.text = "%02d".format(remainSeconds / 60)
        binding.tvRemainSeconds.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekbar(remainMillis: Long) {
        binding.sbTimer.progress = (remainMillis / 1000 / 60).toInt()
    }
}