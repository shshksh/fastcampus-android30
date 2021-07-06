package com.fastcampus.electronicframe

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fastcampus.electronicframe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val imageViewList: List<ImageView> by lazy {
        listOf(binding.iv11, binding.iv12, binding.iv13, binding.iv21, binding.iv22, binding.iv23)
    }

    private val imageUriList = mutableListOf<Uri>()

    private val getPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK)
                return@registerForActivityResult

            it.data?.data?.let { uri ->
                if (imageUriList.size >= 6) {
                    Toast.makeText(this@MainActivity, "이미 사진이 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }
                imageUriList.add(uri)
                imageViewList[imageUriList.lastIndex].setImageURI(uri)
            } ?: run {
                Toast.makeText(this@MainActivity, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestReadExternalStorageLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                navigatePhotos()
            } else {
                Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAddPhotoButton()
        initStartPhotoFrameModeButton()
    }

    private fun initAddPhotoButton() {
        binding.btnAddPhoto.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navigatePhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    requestReadExternalStorageLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        getPhotoLauncher.launch(intent)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자 앱에서 사진을 불러우기 위해 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestReadExternalStorageLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private fun initStartPhotoFrameModeButton() {
//        TODO("Not yet implemented")
    }
}