package com.fastcampus.ch1youtube.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fastcampus.ch1youtube.data.VideoModel
import com.fastcampus.ch1youtube.databinding.ItemVideoBinding

class VideoAdapter : ListAdapter<VideoModel, VideoAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(videoModel: VideoModel) {
            binding.tvTitle.text = videoModel.title
            binding.tvSubTitle.text = videoModel.subtitle

            Glide.with(itemView)
                .load(videoModel.thumb)
                .into(binding.ivThumbnail)
        }
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<VideoModel>() {
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel) =
                oldItem == newItem
        }
    }
}
