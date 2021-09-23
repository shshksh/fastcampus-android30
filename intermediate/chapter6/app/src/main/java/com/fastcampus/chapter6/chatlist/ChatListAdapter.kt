package com.fastcampus.chapter6.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fastcampus.chapter6.databinding.ItemChatListBinding

class ChatListAdapter(
    private val onItemClick: (ChatListItem) -> Unit
) : ListAdapter<ChatListItem, ChatListAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemChatListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatListItem: ChatListItem) {
            binding.root.setOnClickListener { onItemClick(chatListItem) }

            binding.tvChatTitle.text = chatListItem.itemTitle
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ChatListItem>() {
            override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem) =
                oldItem.key == newItem.key

            override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem) =
                oldItem == newItem
        }
    }
}
