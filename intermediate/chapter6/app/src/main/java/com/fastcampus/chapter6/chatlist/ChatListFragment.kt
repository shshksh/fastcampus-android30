package com.fastcampus.chapter6.chatlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.chapter6.R
import com.fastcampus.chapter6.data.FirebaseRepository.chatDB
import com.fastcampus.chapter6.data.FirebaseRepository.isLogin
import com.fastcampus.chapter6.databinding.FragmentChatListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatListFragment : Fragment(R.layout.fragment_chat_list) {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatListAdapter

    private val chatRoomList = mutableListOf<ChatListItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatListBinding.bind(view)

        adapter = ChatListAdapter { }

        chatRoomList.clear()

        binding.rvChatList.adapter = adapter
        binding.rvChatList.layoutManager = LinearLayoutManager(requireContext())

        if (!isLogin())
            return

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java) ?: return

                    chatRoomList.add(model)
                }

                adapter.submitList(chatRoomList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
