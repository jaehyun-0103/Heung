package com.example.heung

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date

class PostWriteActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postwrite)

        var postSave = findViewById<Button>(R.id.post_save)
        var postTitle = findViewById<EditText>(R.id.post_title)
        var postCont = findViewById<EditText>(R.id.post_cont)

        // 저장 버튼 클릭 이벤트 처리
        postSave.setOnClickListener {
            val inputTitle = postTitle.text.toString()
            val inputCont = postCont.text.toString()
            val collectionName = "Posts"
            val userId = "user_id"
            val postDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

            // 추가할 데이터
            val post = hashMapOf(
                "user_id" to userId,
                "post_title" to inputTitle,
                "post_content" to inputCont,
                "post_date" to dateFormat.format(postDate)
            )

            // 데이터베이스에 데이터 추가
            firestore.collection(collectionName)
                .add(post)
                .addOnSuccessListener {
                    // 데이터 추가 성공
                }
                .addOnFailureListener { e ->
                    // 데이터 추가 실패
                }

            // 게시글 저장 후 입력 필드 초기화
            postTitle.text.clear()
            postCont.text.clear()
        }
    }
}