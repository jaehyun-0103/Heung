package com.example.heung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.heung.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.bottomNavigation

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    // Main 탭 클릭 시 MainActivity로 이동
                    val intent = Intent(this, MainActivity::class.java)
                    if (item.isChecked) {
                        startActivity(intent)
                        finish()
                    }
                }
                R.id.nav_calendar -> {
                    // Calendar 탭 클릭 시 CalActivity로 이동
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_map -> {
                    // Map 탭 클릭 시 RentActivity로 이동
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_profile -> {
                    // Profile 탭 클릭 시 SelfProfActivity로 이동
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_recruit -> {
                    // Recruit 탭 클릭 시 RecruitListActivity로 이동
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        val btn_postlist = findViewById<Button>(R.id.btn_postlist)
        btn_postlist.setOnClickListener {
            val intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
        }
    }
}
