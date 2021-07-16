package com.fastcampus.bookreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fastcampus.bookreview.databinding.ActivityDetailBinding
import com.fastcampus.bookreview.model.Book
import com.fastcampus.bookreview.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val db by lazy { getAppDatabase(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = loadDetailData()
        initSaveReviewButton(model)

        loadReview(model)
    }

    private fun initSaveReviewButton(model: Book?) {
        binding.btnSave.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0, binding.etReview.text.toString())
                )
            }.start()
        }
    }

    private fun loadReview(model: Book?) {
        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.etReview.setText(review?.review.orEmpty())
            }
        }.start()
    }

    private fun loadDetailData(): Book? {
        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.tvTitle.text = model?.title.orEmpty()
        binding.tvDescription.text = model?.description.orEmpty()

        Glide.with(binding.ivCover.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.ivCover)
        return model
    }
}