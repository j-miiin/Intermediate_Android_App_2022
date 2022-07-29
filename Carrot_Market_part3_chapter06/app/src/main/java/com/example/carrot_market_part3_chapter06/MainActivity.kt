package com.example.carrot_market_part3_chapter06

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.carrot_market_part3_chapter06.chatlist.ChatListFragment
import com.example.carrot_market_part3_chapter06.home.HomeFragment
import com.example.carrot_market_part3_chapter06.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)

            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        // supportFragmentManager : Activity에 attach 되어있는 fragment를 관리
        // beginTransaction : 이 작업을 시작하겠다고 알려줌. transaction ~ commit 까지는 이 작업만 해라! 이런 느낌
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}