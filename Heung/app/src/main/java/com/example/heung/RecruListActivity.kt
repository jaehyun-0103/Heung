package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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

        // 전체글 보기 버튼 클릭 이벤트 처리
        val viewAllButton = findViewById<Button>(R.id.recruit_view_all)
        viewAllButton.setOnClickListener {
            loadRecruits()
        }

        // 게시글 작성 버튼 클릭 이벤트 처리
        val recruitCreateButton = findViewById<ImageButton>(R.id.recruit_create)
        recruitCreateButton.setOnClickListener {
            val intent = Intent(this, RecrutWriteActivity::class.java)
            startActivity(intent)
        }

        // 게시글 클릭 이벤트 처리
        recruitListAdapter.setOnItemClickListener(object : RecruitListAdapter.OnItemClickListener {
            override fun onItemClick(recruit: Recruits) {
                val intent = Intent(this@RecruListActivity, RecruContActivity::class.java)
                intent.putExtra("recruit_id", recruit.recruit_id)
                startActivity(intent)
            }
        })

        // 하단바 아이템 선택 이벤트 처리
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    if (this::class.java.canonicalName == MainActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_main)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_recruit -> {
                    true
                }
                R.id.nav_map -> {
                    if (this::class.java.canonicalName == RentActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_recruit)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_calendar -> {
                    if (this::class.java.canonicalName == CalActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_recruit)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    if (this::class.java.canonicalName == SelfProfActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    intent.putExtra("selectedItemId", R.id.nav_recruit)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_recruit)?.isChecked = true
    }

    private fun loadRecruits() {
        firestore.collection("Recruits")
            .orderBy("recruit_date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
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
            .orderBy("recruit_date", Query.Direction.DESCENDING)
            .addSnapshotListener { recruitSnapshot, recruitException ->
                if (recruitException != null) { // Error handling
                    return@addSnapshotListener
                }

                recruitSnapshot?.let {
                    recruitList.clear()
                    for (document in recruitSnapshot.documents) {
                        val recruit = document.toObject(Recruits::class.java)
                        recruit?.let {
                            recruitList.add(recruit)
                        }
                    }

                    recruitListAdapter.notifyDataSetChanged()
                }
            }
    }
}