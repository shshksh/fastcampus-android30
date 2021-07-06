package com.fastcampus.electronicframe

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.fastcampus.electronicframe.databinding.ActivityPhotoFrameBinding

class PhotoFrameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoFrameBinding

    private val photoList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)

        for (i in 0 until size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(it.toUri())
            }
        }
    }

}