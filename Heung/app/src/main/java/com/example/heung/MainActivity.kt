package com.example.heung

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import android.view.MenuItem
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // BottomNavigationView 찾기
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // BottomNavigationView 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)

        // 초기 프래그먼트를 MainFragment로 설정
        val initialFragment = MainFragment()
        loadFragment(initialFragment)
    }

    // BottomNavigationView의 아이템 클릭에 대한 리스너
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment? = when (item.itemId) {
            R.id.nav_main -> MainFragment()
            R.id.nav_recruit -> RecruFragment()
            R.id.nav_calendar -> CalFragment()
            R.id.nav_profile -> ProfFragment()
            else -> null
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment)
        } else {
            // MapActivity로 전환할 때에는 map 아이템 itemId를 이곳에 추가하세요.
            if (item.itemId == R.id.nav_map) {
                // MainFragment를 숨깁니다.
                supportFragmentManager.beginTransaction().hide(MainFragment()).commit()
                val mapIntent = Intent(this, RentActivity::class.java)
                startActivity(mapIntent)
                return@OnNavigationItemSelectedListener true
            }
        }
        true
    }

    // 프래그먼트를 로드하고 트랜잭션을 처리하는 함수
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        val existingFragment = supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName)
        if (existingFragment == null) {
            // 존재하지 않는 프래그먼트일 경우, 추가하고 숨김
            transaction.add(R.id.fragment_container, fragment, fragment::class.java.simpleName)
            transaction.hide(fragment)
        }

        for (i in 0 until supportFragmentManager.fragments.size) {
            val existingFrag = supportFragmentManager.fragments[i]

            if (existingFrag.javaClass == fragment.javaClass) {
                // 같은 타입의 프래그먼트가 이미 추가되어 있다면, 숨김 해제
                transaction.show(existingFrag)
            } else {
                // 그 외의 경우, 프래그먼트를 숨깁니다.
                transaction.hide(existingFrag)
            }
        }

        transaction.commit()
    }



    // 뒤로가기 버튼을 누르면 항상 MainActivity로 돌아가게 하는 코드
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
