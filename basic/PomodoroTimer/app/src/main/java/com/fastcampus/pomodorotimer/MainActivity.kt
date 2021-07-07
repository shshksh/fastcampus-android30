package com.fastcampus.pomodorotimer

import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.pomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var timer: CountDownTimer? = null

    private val soundPool = SoundPool.Builder().build()

    private var tickingSoundId: Int? = null

    private var bellSoundId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViews()

        initSounds()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun bindViews() {
        binding.sbTimer.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    updateRemainTime(progress * 60L * 1000L)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                stopCountDown()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (seekBar.progress == 0)
                    stopCountDown()
                else
                    startCountDown()
            }
        })
    }

    private fun startCountDown() {
        timer = createCountDownTimer(binding.sbTimer.progress * 1000L * 60L)
        timer?.start()

        tickingSoundId?.let {
            soundPool.play(it, 1f, 1f, 0, -1, 1f)
        }
    }

    private fun initSounds() {
        tickingSoundId = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekbar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun stopCountDown() {
        timer?.cancel()
        timer = null
        soundPool.autoPause()
    }

    private fun completeCountDown() {
        updateRemainTime(0L)
        updateSeekbar(0L)

        soundPool.autoPause()
        bellSoundId?.let {
            soundPool.play(it, 1f, 1f, 0, 0, 1f)
        }
    }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        binding.tvRemainMinutes.text = "%02d'".format(remainSeconds / 60)
        binding.tvRemainSeconds.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekbar(remainMillis: Long) {
        binding.sbTimer.progress = (remainMillis / 1000 / 60).toInt()
    }
}