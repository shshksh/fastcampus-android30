package com.fastcampus.chapter6

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.fastcampus.chapter6.data.FirebaseRepository.articleDB
import com.fastcampus.chapter6.data.FirebaseRepository.articleStorage
import com.fastcampus.chapter6.data.FirebaseRepository.currentUserId
import com.fastcampus.chapter6.databinding.ActivityAddArticleBinding
import com.fastcampus.chapter6.home.ArticleModel

class AddArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddArticleBinding

    private var selectedUri: Uri? = null

    private val getPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            selectedUri = uri
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
            showProgress()

            val title = binding.etTitle.text.toString()
            val price = binding.etPrice.text.toString()
            val sellerId = currentUserId()

            selectedUri?.let {
                uploadPhoto(
                    it,
                    { uri -> uploadArticle(sellerId, title, price, uri) },
                    {
                        Toast.makeText(this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        hideProgress()
                    }
                )
            } ?: run {
                uploadArticle(sellerId, title, price, "")
            }
        }
    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        articleStorage.child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    articleStorage.child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    errorHandler()
                }
            }
    }

    private fun uploadArticle(sellerId: String, title: String, price: String, imageUrl: String) {
        val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "$price 원", imageUrl)
        articleDB.push().setValue(model)

        hideProgress()
        finish()
    }

    private fun showProgress() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgress() {
        binding.progressBar.isVisible = false
    }
}