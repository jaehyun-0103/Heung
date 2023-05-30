package com.example.heung

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import data.Comments
import java.text.SimpleDateFormat
import java.util.*

class PostContActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var Adapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var comments: MutableList<Comments>

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postcont)

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvAuthor = findViewById<TextView>(R.id.tv_author)

        val intent = intent
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        val postAuthor = intent.getStringExtra("postAuthor")
        val postId = intent.getStringExtra("postId")

        tvTitle.text = postTitle
        tvContent.text = postContent
        tvAuthor.text = postAuthor

        recyclerView = findViewById(R.id.content_photo_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        comments = mutableListOf() // postList 초기화
        Adapter = CommentsAdapter(comments)
        recyclerView.adapter = Adapter

        firestore = FirebaseFirestore.getInstance() // Firebase 데이터베이스 참조 초기화

        val commentTextview = findViewById<EditText>(R.id.comment_textview)
        val btnCtv = findViewById<Button>(R.id.btn_ctv)

        // 저장 버튼 클릭 이벤트 처리
        btnCtv.setOnClickListener {
            val inputCont = commentTextview.text.toString()
            val collectionName = "Comments"
            val userId = "user_id"
            val commentDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

            // 추가할 데이터
            val comment = hashMapOf(
                "post_id" to postId,
                "user_id" to userId,
                "comment" to inputCont,
                "comment_date" to dateFormat.format(commentDate)
            )

            // 데이터베이스에 데이터 추가
            firestore.collection(collectionName)
                .add(comment)
                .addOnSuccessListener {
                    // 데이터 추가 성공
                }
                .addOnFailureListener { e ->
                    // 데이터 추가 실패
                }

            // 게시글 저장 후 입력 필드 초기화
            commentTextview.text.clear()
        }

        // 게시글 목록 데이터 가져오기
        firestore.collection("Comments")
            .whereEqualTo("post_id", postId)
            .orderBy("comment_date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) { // 에러 처리
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    comments.clear()
                    for (document in it.documents) {
                        val comment = document.toObject(Comments::class.java)
                        comment?.let {
                            comments.add(comment)
                        }
                    }
                    Adapter.notifyDataSetChanged()
                }
            }
    }
}