package com.fastcampus.chapter6

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.chapter6.data.FirebaseRepository.articleDB
import com.fastcampus.chapter6.data.FirebaseRepository.currentUserId
import com.fastcampus.chapter6.databinding.ActivityAddArticleBinding
import com.fastcampus.chapter6.home.ArticleModel

class AddArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddArticleBinding

    private val getPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            binding.ivPhoto.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initAddPhotoButton()
        initSubmitButton()
    }

    private fun initAddPhotoButton() {
        binding.btnAddPhoto.setOnClickListener {
            getPhotoLauncher.launch("image/*")
        }
    }

    private fun initSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val price = binding.etPrice.text.toString()
            val sellerId = currentUserId()

            val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "$price 원", "")
            articleDB.push().setValue(model)

            finish()
        }
    }
}