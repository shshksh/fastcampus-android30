package com.fastcampus.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fastcampus.bookreview.databinding.ItemBookBinding
import com.fastcampus.bookreview.model.Book

class BookAdapter(private val itemClickListener: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookItemViewHolder(
        ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class BookItemViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            with(binding) {
                tvTitle.text = bookModel.title
                tvDescription.text = bookModel.description
                root.setOnClickListener {
                    itemClickListener(bookModel)
                }
                Glide.with(root)
                    .load(bookModel.coverSmallUrl)
                    .into(ivCover)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }
        }
    }
}