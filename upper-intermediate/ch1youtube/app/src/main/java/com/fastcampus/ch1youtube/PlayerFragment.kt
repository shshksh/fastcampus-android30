package com.fastcampus.ch1youtube

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.ch1youtube.adapter.VideoAdapter
import com.fastcampus.ch1youtube.databinding.FragmentPlayerBinding
import kotlinx.coroutines.launch

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var videoAdapter: VideoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPlayerBinding.bind(view)

        initMotionLayoutEvent()
        initRecyclerView()
        observeVideoList()
    }

    private fun initMotionLayoutEvent() {
        binding.motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
                (requireActivity() as? MainActivity)?.updateMotionProgress(progress)
            }
        })
    }

    private fun initRecyclerView() {
        videoAdapter = VideoAdapter()
        binding.rvAdditionalVideo.adapter = videoAdapter
        binding.rvAdditionalVideo.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeVideoList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.videoList.collect {
                        videoAdapter.submitList(it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
