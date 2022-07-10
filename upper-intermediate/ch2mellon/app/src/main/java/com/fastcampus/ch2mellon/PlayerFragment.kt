package com.fastcampus.ch2mellon

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.fastcampus.ch2mellon.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.launch

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding!!

    private var player: ExoPlayer? = null

    private lateinit var adapter: PlayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlayerBinding.bind(view)

        initPlayerView()
        initPlayListButton()
        initPlayControlButtons()
        initRecyclerView()

        getVideoList()
    }

    private fun initPlayerView() {
        player = ExoPlayer.Builder(requireContext()).build()

        binding.playerView.player = player

        player?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying) {
                    binding.playControlImageView.setImageResource(R.drawable.baseline_pause_48)
                } else {
                    binding.playControlImageView.setImageResource(R.drawable.baseline_play_arrow_48)
                }
            }
        })
    }

    private fun initPlayListButton() {
        binding.playListImageView.setOnClickListener {
            binding.playerViewGroup.isVisible = !binding.playerViewGroup.isVisible
            binding.playListViewGroup.isVisible = !binding.playListViewGroup.isVisible
        }
    }

    private fun initPlayControlButtons() {
        binding.playControlImageView.setOnClickListener {
            val player = player ?: return@setOnClickListener

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        binding.skipNextImageView.setOnClickListener {

        }

        binding.skipPrevImageView.setOnClickListener {

        }
    }

    private fun initRecyclerView() {
        adapter = PlayListAdapter {

        }
        binding.playListRecyclerView.adapter = adapter
        binding.playListRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun getVideoList() {
        viewLifecycleOwner.lifecycleScope.launch {
            musicService.listMusics().musics
                .mapIndexed { index, musicEntity -> musicEntity.mapper(index.toLong()) }
                .also { setMusicList(it) }
                .submitTo(adapter)
        }
    }

    private fun setMusicList(modelList: List<MusicModel>) {
        player?.addMediaItems(modelList.map {
            MediaItem.Builder()
                .setMediaId(it.id.toString())
                .setUri(it.streamUrl)
                .build()
        })
        player?.prepare()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <E> List<E>.submitTo(adapter: ListAdapter<E, PlayListAdapter.ViewHolder>) {
        adapter.submitList(this)
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}
