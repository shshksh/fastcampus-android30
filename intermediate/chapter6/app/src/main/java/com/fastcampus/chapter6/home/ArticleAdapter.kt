package com.fastcampus.chapter6.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fastcampus.chapter6.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(
    private val onItemClick: (ArticleModel) -> Unit
) : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articleModel: ArticleModel) {
            val df = SimpleDateFormat("MM월 dd일", Locale.KOREA)
            val date = Date(articleModel.createdAt)

            with(binding) {
                tvTitle.text = articleModel.title
                tvDate.text = df.format(date)
                tvPrice.text = articleModel.price

                if (articleModel.imageUrl.isNotEmpty()) {
                    Glide.with(ivThumbnail)
                        .load(articleModel.imageUrl)
                        .into(ivThumbnail)
                }
            }
            binding.root.setOnClickListener {
                onItemClick(articleModel)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel) =
                oldItem.createdAt == newItem.createdAt

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel) =
                oldItem == newItem
        }
    }
}
