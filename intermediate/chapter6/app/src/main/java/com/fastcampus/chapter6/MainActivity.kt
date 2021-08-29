package com.fastcampus.chapter6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fastcampus.chapter6.chatlist.ChatListFragment
import com.fastcampus.chapter6.databinding.ActivityMainBinding
import com.fastcampus.chapter6.home.HomeFragment
import com.fastcampus.chapter6.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.navBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    replaceFragment(HomeFragment::class.java)
                }
                R.id.menu_chatList -> {
                    replaceFragment(ChatListFragment::class.java)
                }
                R.id.menu_myPage -> {
                    replaceFragment(MyPageFragment::class.java)
                }
            }
            true
        }
    }

    private fun replaceFragment(fClass: Class<out Fragment>) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fClass, null)
            .commit()
    }
}