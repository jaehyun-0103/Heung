package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heung.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.sdk.user.UserApiClient
import data.Posts

class MainActivity : AppCompatActivity() {
    private lateinit var popularPostsRecyclerView: RecyclerView
    private lateinit var latestPostsRecyclerView: RecyclerView
    private lateinit var postListAdapter: PostListAdapter
    private val popularPostList = mutableListOf<Posts>()
    private val latestPostList = mutableListOf<Posts>()
    private lateinit var bottomNavigationView: BottomNavigationView
    private var selectedItemId: Int = R.id.nav_main // 초기 선택 항목을 메인으로 설정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ///////////////////
        val logout = findViewById<Button>(R.id.logout)

        val textView = findViewById<TextView>(R.id.textView)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
                // 에러 처리 로직 추가
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val userId = user.id.toString()
                // userId 변수에 사용자 식별자가 저장됩니다.

                // TextView에 식별자를 설정합니다.
                textView.text = userId
            }
        }


        // 로그아웃 버튼 클릭 시
        logout.setOnClickListener {
            // 카카오 로그아웃 요청
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    // 로그아웃 실패
                    // 오류 처리 코드를 추가하세요.
                } else {
                    // 로그아웃 성공
                    // LoginActivity로 돌아가는 인텐트 생성
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }


            val btn_postlist = findViewById<Button>(R.id.btn_postlist)
            btn_postlist.setOnClickListener {
                val intent = Intent(this, PostListActivity::class.java)
                startActivity(intent)
            }
        }

        //////////////////
        popularPostsRecyclerView = findViewById(R.id.popular_posts_recyclerview)
        latestPostsRecyclerView = findViewById(R.id.latest_posts_recyclerview)
        postListAdapter = PostListAdapter(popularPostList)
        popularPostsRecyclerView.adapter = postListAdapter
        popularPostsRecyclerView.layoutManager = LinearLayoutManager(this)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener)

        // 초기 선택된 아이템 설정
        bottomNavigationView.selectedItemId = R.id.nav_main

        // 인기 게시글 가져오기
        loadPopularPosts()

        // 최신 게시글 가져오기
        loadLatestPosts()

        val btnPostList = findViewById(R.id.btn_postlist) as Button
        btnPostList.setOnClickListener {
            val intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
        }


        // 하단바 설정
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener)

        // 초기 선택된 아이템 설정
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

                    // 최신 게시글을 latestPostsRecyclerView에 표시
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