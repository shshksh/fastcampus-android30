package com.fastcampus.chapter6.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.chapter6.R
import com.fastcampus.chapter6.data.FirebaseRepository.articleDB
import com.fastcampus.chapter6.databinding.FragmentHomeBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArticleAdapter

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java) ?: return

            articleList.add(articleModel)
            adapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        adapter = ArticleAdapter()

        initViews()
        articleDB.addChildEventListener(listener)
    }

    private fun initViews() {
        initArticleRecyclerView()
    }

    private fun initArticleRecyclerView() {
        binding.rvArticle.layoutManager = LinearLayoutManager(context)
        binding.rvArticle.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        articleDB.removeEventListener(listener)
    }
}