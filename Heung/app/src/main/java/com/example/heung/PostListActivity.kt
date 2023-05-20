package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FirebaseFirestore
import data.Posts

class PostListActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postList: MutableList<Posts>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postlist)

        recyclerViewPosts = findViewById(R.id.post_Recycler)
        recyclerViewPosts.layoutManager = LinearLayoutManager(this)

        postList = mutableListOf() // postList 초기화
        postListAdapter = PostListAdapter(postList)
        recyclerViewPosts.adapter = postListAdapter

        // Firebase 데이터베이스 참조 초기화
        firestore = FirebaseFirestore.getInstance()

        // 게시글 목록 데이터 가져오기
        firestore.collection("Posts")
            .orderBy("post_date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    // 에러 처리
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    postList.clear()
                    for (document in it.documents) {
                        val post = document.toObject(Posts::class.java)
                        post?.let {
                            postList.add(post)
                        }
                    }
                    postListAdapter.notifyDataSetChanged()
                }
            }

        val post_create = findViewById<Button>(R.id.post_create)
        post_create.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }
    }
}