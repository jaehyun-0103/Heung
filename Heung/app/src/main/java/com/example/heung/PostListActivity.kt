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

        firestore = FirebaseFirestore.getInstance() // Firebase 데이터베이스 참조 초기화

        // 게시글 목록 데이터 가져오기
        firestore.collection("Posts")
            .orderBy("post_date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) { // 에러 처리
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

        // 게시글 목록 클릭 이벤트 처리
        postListAdapter.setOnItemClickListener { position ->
            val clickedPost = postList[position]

            // 인텐트 생성 및 데이터 전달
            val intent = Intent(this, PostContActivity::class.java)
            intent.putExtra("postId", clickedPost.post_id)
            startActivity(intent)
        }

        // 게시글 작성 버튼 클릭 이벤트 처리
        val postCreate = findViewById<Button>(R.id.post_create)
        postCreate.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }
    }
}