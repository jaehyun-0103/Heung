package com.example.heung

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import data.Posts

class MainActivity : AppCompatActivity() {
    private lateinit var popularPostsRecyclerView: RecyclerView
    private lateinit var latestPostsRecyclerView: RecyclerView
    private lateinit var postListAdapter: PostListAdapter
    private val popularPostList = mutableListOf<Posts>()
    private val latestPostList = mutableListOf<Posts>()
    private lateinit var bottomNavigationView: BottomNavigationView
    private var selectedItemId: Int = R.id.nav_main

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popularPostsRecyclerView = findViewById(R.id.popular_posts_recyclerview)
        latestPostsRecyclerView = findViewById(R.id.latest_posts_recyclerview)
        postListAdapter = PostListAdapter(popularPostList)
        popularPostsRecyclerView.adapter = postListAdapter
        popularPostsRecyclerView.layoutManager = LinearLayoutManager(this)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.nav_main

        loadPopularPosts() // 인기 게시글 가져오기
        loadLatestPosts() // 최신 게시글 가져오기

        val btnPostList = findViewById<Button>(R.id.btn_postlist)
        btnPostList.setOnClickListener {
            val intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
        }

        // 하단바 설정
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.nav_main
    }

    // 인기 게시글 가져오기
    private fun loadPopularPosts() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Likes")
            .orderBy("like")
            .startAt(5)
            .addSnapshotListener { likesSnapshot, likesException ->
                if (likesException != null) {
                    return@addSnapshotListener
                }

                val likedUserIds = mutableListOf<String>()
                likesSnapshot?.let {
                    likedUserIds.clear()
                    for (document in it.documents) {
                        val likedUserId = document.getString("user_id")
                        likedUserId?.let {
                            likedUserIds.add(likedUserId)
                        }
                    }
                }

                firestore.collection("Posts")
                    .whereIn("user_id", likedUserIds)
                    .orderBy("post_date", Query.Direction.DESCENDING)
                    .limit(5) // 최대 5개의 인기 글만 가져오도록 제한
                    .addSnapshotListener { postsSnapshot, postsException ->
                        if (postsException != null) {
                            return@addSnapshotListener
                        }
                        postsSnapshot?.let {
                            popularPostList.clear()
                            for (document in it.documents) {
                                val post = document.toObject(Posts::class.java)
                                post?.let {
                                    popularPostList.add(post)
                                }
                            }
                            postListAdapter.notifyDataSetChanged()
                        }
                    }
            }
    }

    // 최신 게시글 가져오기
    private fun loadLatestPosts() {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("Posts")
            .orderBy("post_date", Query.Direction.DESCENDING)
            .limit(5) // 최신 5개 글만 가져오도록 제한
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) { // 에러 처리
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    latestPostList.clear()
                    for (document in it.documents) {
                        val post = document.toObject(Posts::class.java)
                        post?.let {
                            latestPostList.add(post)
                        }
                    }
                    postListAdapter.notifyDataSetChanged()

                    val latestPostListAdapter = PostListAdapter(latestPostList)
                    latestPostsRecyclerView.adapter = latestPostListAdapter
                    latestPostsRecyclerView.layoutManager = LinearLayoutManager(this)
                }
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