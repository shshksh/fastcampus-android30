package com.fastcampus.bookreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.fastcampus.bookreview.databinding.ActivityDetailBinding
import com.fastcampus.bookreview.model.Book
import com.fastcampus.bookreview.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.tvTitle.text = model?.title.orEmpty()
        binding.tvDescription.text = model?.description.orEmpty()

        Glide.with(binding.ivCover.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.ivCover)

        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.etReview.setText(review?.review.orEmpty())
            }
        }.start()

        binding.btnSave.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0, binding.etReview.text.toString())
                )
            }.start()
        }
    }
}