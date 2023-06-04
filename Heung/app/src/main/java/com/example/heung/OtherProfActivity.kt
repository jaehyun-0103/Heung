package com.example.heung

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import data.Posts

class OtherProfActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OtherProfAdapter
    private val COLLECTION_NAME = "Posts"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherprof)

        // Firebase 앱 초기화
        FirebaseApp.initializeApp(this)

        recyclerView = findViewById(R.id.other_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getPostsList()

    }

    private fun getPostsList() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Posts")
            .whereEqualTo("user_id", "jaehyun")
            .get()
            .addOnSuccessListener { documents ->
                val postsList = mutableListOf<Posts>()

                for (document in documents) {
                    val postId = document.id
                    val userId = document.getString("user_id") ?: ""
                    val title = document.getString("post_title") ?: ""
                    val content = document.getString("post_content") ?: ""
                    val date = document.getString("post_date") ?: ""

                    val post = Posts(postId, userId, title, content, date)
                    postsList.add(post)
                }

                adapter = OtherProfAdapter(postsList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // 오류 처리
            }


    }
}