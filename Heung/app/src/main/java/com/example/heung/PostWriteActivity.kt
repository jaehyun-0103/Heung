package com.example.heung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

class PostWriteActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postwrite)

        var post_save = findViewById<Button>(R.id.post_save)
        var post_title = findViewById<EditText>(R.id.post_title)
        var post_cont = findViewById<EditText>(R.id.post_cont)

        post_save.setOnClickListener {
            val collectionName = "Posts"
            val userId = "user_id"
            val postTitle = "게시글 제목"
            val postContent = "게시글 내용"
            val postDate = "게시글 작성일"

            // 데이터 추가
            val post = hashMapOf(
                "user_id" to userId,
                "post_title" to postTitle,
                "post_content" to postContent,
                "post_date" to postDate
            )

            firestore.collection(collectionName)
                .add(post)
                .addOnSuccessListener {
                    // 데이터 추가 성공
                }
                .addOnFailureListener { e ->
                    // 데이터 추가 실패
                }

            // 게시글 저장 후 입력 필드 초기화
            post_title.text.clear()
            post_cont.text.clear()
        }
    }
}