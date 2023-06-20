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
import data.Posts

class MainActivity : AppCompatActivity() {
    private lateinit var popularPostsRecyclerView: RecyclerView
    private lateinit var latestPostsRecyclerView: RecyclerView
    private lateinit var postListAdapter: MainAdapter
    private val popularPostList = mutableListOf<Posts>()
    private val latestPostList = mutableListOf<Posts>()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popularPostsRecyclerView = findViewById(R.id.popular_posts_recyclerview)
        latestPostsRecyclerView = findViewById(R.id.latest_posts_recyclerview)
        postListAdapter = MainAdapter(popularPostList)
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

        val btnpopPostList = findViewById<Button>(R.id.btn_populars_post)
        btnpopPostList.setOnClickListener {
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
            .addSnapshotListener { likesSnapshot, likesException ->
                if (likesException != null) {
                    return@addSnapshotListener
                }

                val postIdList = mutableListOf<String>()
                likesSnapshot?.let {
                    postIdList.clear()
                    var count = 0
                    for (document in it.documents) {
                        if (count >= 5) {
                            break
                        }
                        val postId = document.getString("post_id")
                        val like = document.getString("like")?.toIntOrNull()
                            ?: 0 // Parse like value as integer
                        if (like > 5) {
                            postId?.let {
                                postIdList.add(postId)
                                count++
                            }
                        }
                    }
                }
                if (postIdList.isNotEmpty()) {
                    firestore.collection("Posts")
                        .apply {
                            if (postIdList.isNotEmpty()) {
                                whereIn("post_id", postIdList)
                            }
                        }
                        .orderBy("post_date", Query.Direction.DESCENDING)
                        .addSnapshotListener { postsSnapshot, postsException ->
                            if (postsException != null) { // Error handling
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

                    val latestPostListAdapter = MainAdapter(latestPostList)
                    latestPostsRecyclerView.adapter = latestPostListAdapter
                    latestPostsRecyclerView.layoutManager = LinearLayoutManager(this)
                }
            }
    }

    private val navItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    if (javaClass.name == MainActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_recruit -> {
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_map -> {
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_calendar -> {
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@OnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                else -> return@OnNavigationItemSelectedListener false
            }
        }
}