package com.fastcampus.tinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fastcampus.tinder.databinding.ItemMatchedUserBinding

class MatchedUserAdapter : ListAdapter<CardItem, MatchedUserAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMatchedUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemMatchedUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cardItem: CardItem) {
            binding.tvUserName.text = cardItem.name
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>() {
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem) =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem) =
                oldItem == newItem
        }
    }

}
