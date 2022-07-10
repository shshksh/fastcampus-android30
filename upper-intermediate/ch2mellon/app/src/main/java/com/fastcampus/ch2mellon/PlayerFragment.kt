package com.fastcampus.ch2mellon

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var model = PlayerModel()

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
            if (model.currentPosition == -1) return@setOnClickListener

            binding.playerViewGroup.isVisible = model.isWatchingPlayListView
            binding.playListViewGroup.isVisible = !model.isWatchingPlayListView

            model.isWatchingPlayListView = !model.isWatchingPlayListView
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
            val nextMusic = model.nextMusic() ?: return@setOnClickListener
            playMusic(nextMusic)
        }

        binding.skipPrevImageView.setOnClickListener {
            val prevMusic = model.prevMusic() ?: return@setOnClickListener
            playMusic(prevMusic)
        }
    }

    private fun initRecyclerView() {
        adapter = PlayListAdapter { playMusic(it) }
        binding.playListRecyclerView.adapter = adapter
        binding.playListRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun getVideoList() {
        viewLifecycleOwner.lifecycleScope.launch {
            musicService.listMusics().let {
                model = it.mapper()
                setMusicList(model.getAdapterModels())
                adapter.submitList(model.getAdapterModels())
            }
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

    private fun playMusic(musicModel: MusicModel) {
        model.updateCurrentPosition(musicModel)
        player?.seekTo(model.currentPosition, 0)
        player?.play()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}
