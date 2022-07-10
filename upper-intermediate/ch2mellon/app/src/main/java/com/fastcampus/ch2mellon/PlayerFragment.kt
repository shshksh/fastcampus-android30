package com.fastcampus.ch2mellon

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.fastcampus.ch2mellon.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlayerBinding.bind(view)

        initPlayListButton()
    }

    private fun initPlayListButton() {
        binding.playListImageView.setOnClickListener {
            binding.playerViewGroup.isVisible = !binding.playerViewGroup.isVisible
            binding.playListViewGroup.isVisible = !binding.playListViewGroup.isVisible
        }
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
