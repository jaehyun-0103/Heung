package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import data.Recruits

class RecruListActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recruitListAdapter: RecruitListAdapter
    private lateinit var recyclerViewRecruits: RecyclerView
    private lateinit var recruitList: MutableList<Recruits>
    private lateinit var buskingFilterButton: Button
    private lateinit var classFilterButton: Button

    //여기부터 하단바 관련 코드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recrulist)

        recyclerViewRecruits = findViewById(R.id.recruit_list)
        recyclerViewRecruits.layoutManager = LinearLayoutManager(this)

        recruitList = mutableListOf()
        recruitListAdapter = RecruitListAdapter(recruitList)
        recyclerViewRecruits.adapter = recruitListAdapter

        firestore = FirebaseFirestore.getInstance()

        // 데이터베이스에서 모집글 목록 가져오기
        loadRecruits()

        // 필터 버튼 초기화
        buskingFilterButton = findViewById(R.id.recruit_filter_busking)
        classFilterButton = findViewById(R.id.recruit_filter_class)

        // 버스킹 필터 버튼 클릭 이벤트 처리
        buskingFilterButton.setOnClickListener {
            filterByType("버스킹")
        }

        // 클래스 필터 버튼 클릭 이벤트 처리
        classFilterButton.setOnClickListener {
            filterByType("클래스")
        }

        // 하단바 아이템 선택 이벤트 처리
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    // 이미 MainActivity인 경우 액티비티 전환하지 않음
                    if (this::class.java.canonicalName == MainActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_main) // 현재 선택된 항목 ID를 전달
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_recruit -> {
                    // 현재 액티비티이므로 아무 작업 없이 true 반환
                    true
                }
                R.id.nav_map -> {
                    // 이미 RentActivity인 경우 액티비티 전환하지 않음
                    if (this::class.java.canonicalName == RentActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_recruit) // 현재 선택된 항목 ID를 전달
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_calendar -> {
                    // 이미 CalActivity인 경우 액티비티 전환하지 않음
                    if (this::class.java.canonicalName == CalActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_recruit) // 현재 선택된 항목 ID를 전달
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    // 이미 SelfProfActivity인 경우 액티비티 전환하지 않음
                    if (this::class.java.canonicalName == SelfProfActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_recruit) // 현재 선택된 항목 ID를 전달
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.menu.findItem(R.id.nav_recruit)?.isChecked = true
        //여기까지 하단바 관련 코드 밑에부턴 기존코드

        // 전체글 보기 버튼 클릭 이벤트 처리
        val viewAllButton = findViewById<Button>(R.id.recruit_view_all)
        viewAllButton.setOnClickListener {
            loadRecruits()
        }

        // 게시글 작성 버튼 클릭 이벤트 처리
        val recruitCreateButton = findViewById<Button>(R.id.recruit_create)
        recruitCreateButton.setOnClickListener {
            val intent = Intent(this, RecrutWriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadRecruits() {
        firestore.collection("Recruits")
            .orderBy("recruit_id", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    // 에러 처리
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    recruitList.clear()
                    for (document in it.documents) {
                        val recruit = document.toObject(Recruits::class.java)
                        recruit?.let {
                            if (recruit.recruit_id.isNotEmpty()) {
                                recruitList.add(recruit)
                            }
                        }
                    }
                    recruitListAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun filterByType(type: String) {
        firestore.collection("Recruits")
            .whereEqualTo("recruit_type", type)
            .orderBy("recruit_id", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    // 에러 처리
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    recruitList.clear()
                    for (document in it.documents) {
                        val recruit = document.toObject(Recruits::class.java)
                        recruit?.let {
                            if (recruit.recruit_id.isNotEmpty()) {
                                recruitList.add(recruit)
                            }
                        }
                    }
                    recruitListAdapter.notifyDataSetChanged()
                }
            }
    }
}
