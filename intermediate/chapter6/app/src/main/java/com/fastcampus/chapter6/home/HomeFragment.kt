package com.fastcampus.chapter6.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.chapter6.AddArticleActivity
import com.fastcampus.chapter6.R
import com.fastcampus.chapter6.chatlist.ChatListItem
import com.fastcampus.chapter6.data.FirebaseRepository.CHILD_CHAT
import com.fastcampus.chapter6.data.FirebaseRepository.articleDB
import com.fastcampus.chapter6.data.FirebaseRepository.currentUserId
import com.fastcampus.chapter6.data.FirebaseRepository.isLogin
import com.fastcampus.chapter6.data.FirebaseRepository.userDB
import com.fastcampus.chapter6.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
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
        adapter = ArticleAdapter { article -> createChatRoom(article, view) }

        initViews()
        articleDB.addChildEventListener(listener)
    }

    private fun createChatRoom(article: ArticleModel, view: View) {
        if (isLogin()) {
            if (currentUserId() != article.sellerId) {
                val chatRoom = ChatListItem(
                    buyerId = currentUserId(),
                    sellerId = article.sellerId,
                    itemTitle = article.title,
                    key = System.currentTimeMillis()
                )

                userDB.child(currentUserId())
                    .child(CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                userDB.child(article.sellerId)
                    .child(CHILD_CHAT)
                    .push()
                    .setValue(chatRoom)

                Snackbar.make(view, "채팅방이 생성되었습니다.", Snackbar.LENGTH_LONG).show()

            } else {
                Snackbar.make(view, "내가 올린 아이템입니다.", Snackbar.LENGTH_LONG).show()
            }
        } else {
            Snackbar.make(view, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        initArticleRecyclerView()
        initFAB()
    }

    private fun initArticleRecyclerView() {
        binding.rvArticle.layoutManager = LinearLayoutManager(context)
        binding.rvArticle.adapter = adapter
    }

    private fun initFAB() {
        binding.fabAdd.setOnClickListener {
//            if (isLogin())
            startActivity(Intent(requireContext(), AddArticleActivity::class.java))
//            else
//                Snackbar.make(it, "로그인 후 사용해주세요.", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        articleDB.removeEventListener(listener)
    }
}
