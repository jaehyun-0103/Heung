package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.heung.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //여기부터 하단바 관련 코드
    private lateinit var bottomNavigationView: BottomNavigationView
    private var selectedItemId: Int = R.id.nav_main // 초기 선택 항목을 메인으로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener)

        // 초기 선택된 아이템 설정
        bottomNavigationView.selectedItemId = R.id.nav_main


        val btn_postlist = findViewById<Button>(R.id.btn_postlist) // 여기 내가 추가한거야!!!!!!
        btn_postlist.setOnClickListener {
            val intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
        }

    }

    private val navItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    // 현재 액티비티가 이미 MainActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == MainActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 메인화면으로 돌아가도록 합니다.
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_recruit -> {
                    // 현재 액티비티가 이미 RecruListActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 모집 화면으로 돌아가도록 합니다.
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_map -> {
                    // 현재 액티비티가 이미 RentActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 지도 화면으로 돌아가도록 합니다.
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_calendar -> {
                    // 현재 액티비티가 이미 CalActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 달력/일정 화면으로 돌아가도록 합니다.
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_profile -> {
                    // 현재 액티비티가 이미 SelfProfActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 프로필 화면으로 돌아가도록 합니다.
                    return@OnNavigationItemSelectedListener true
                }

                else -> return@OnNavigationItemSelectedListener false
            }
        }
}
//여기까지 하단바 관련 코드 밑에부턴 기존코드
