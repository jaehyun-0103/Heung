package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
