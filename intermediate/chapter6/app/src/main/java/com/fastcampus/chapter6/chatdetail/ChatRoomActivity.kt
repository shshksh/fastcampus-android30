package com.fastcampus.chapter6.chatdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcampus.chapter6.data.FirebaseRepository.DB_CHATS
import com.fastcampus.chapter6.data.FirebaseRepository.currentUserId
import com.fastcampus.chapter6.databinding.ActivityChatRoomBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatRoomBinding

    private lateinit var adapter: ChatItemAdapter

    private val chatList = mutableListOf<ChatItem>()

    private lateinit var chatDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatKey = intent.getLongExtra("chatKey", -1)

        chatDB = Firebase.database.reference.child(DB_CHATS).child(chatKey.toString())

        chatDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java) ?: return

                chatList.add(chatItem)

                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        adapter = ChatItemAdapter()

        binding.rvChat.adapter = adapter
        binding.rvChat.layoutManager = LinearLayoutManager(this)

        binding.btnSend.setOnClickListener {
            val chatItem = ChatItem(currentUserId(), binding.etMessage.text.toString())

            chatDB.push().setValue(chatItem)
        }
    }
}
