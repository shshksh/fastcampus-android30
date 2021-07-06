package com.fastcampus.electronicframe

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.fastcampus.electronicframe.databinding.ActivityPhotoFrameBinding
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoFrameBinding

    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPhotoUriFromIntent()
        startTimer()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)

        for (i in 0 until size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(it.toUri())
            }
        }
    }

    private fun startTimer() {
        timer(period = 5000) {
            runOnUiThread {
                val current = currentPosition
                val next = (current + 1) % photoList.size

                binding.ivBackgroundPhoto.setImageURI(photoList[current])

                with(binding.ivPhoto) {
                    alpha = 0f
                    setImageURI(photoList[next])
                    animate()
                        .alpha(1.0f)
                        .setDuration(1000)
                        .start()
                }

                currentPosition = next
            }
        }
    }

}