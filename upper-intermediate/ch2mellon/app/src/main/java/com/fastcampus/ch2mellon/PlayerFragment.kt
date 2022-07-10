package com.fastcampus.ch2mellon

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.fastcampus.ch2mellon.databinding.FragmentPlayerBinding
import kotlinx.coroutines.launch

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding!!

    private lateinit var adapter: PlayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlayerBinding.bind(view)

        initPlayListButton()
        initRecyclerView()

        getVideoList()
    }

    private fun initPlayListButton() {
        binding.playListImageView.setOnClickListener {
            binding.playerViewGroup.isVisible = !binding.playerViewGroup.isVisible
            binding.playListViewGroup.isVisible = !binding.playListViewGroup.isVisible
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
                .submitTo(adapter)
        }
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
