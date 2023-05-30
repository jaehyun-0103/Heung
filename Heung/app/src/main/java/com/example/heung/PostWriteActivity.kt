package com.example.heung

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date

class PostWriteActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var latestPostId: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postwrite)

        val postSave = findViewById<Button>(R.id.post_save)
        val postTitle = findViewById<EditText>(R.id.post_title)
        val postCont = findViewById<EditText>(R.id.post_cont)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        // 초기 상태에서 버튼 비활성화
        postSave.isEnabled = false

        // 게시글 ID 가져오기
        getLatestPostId()

        // 저장 버튼 클릭 이벤트 처리
        postSave.setOnClickListener {
            val inputTitle = postTitle.text.toString()
            val inputCont = postCont.text.toString()
            val collectionName = "Posts"
            val userId = "user_id"
            val postDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

            // 새로운 게시글 ID 생성
            val newPostId = generateNewPostId()

            // 추가할 데이터
            val post = hashMapOf(
                "post_id" to newPostId,
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

                    // 등록 성공 후 이전 페이지로 이동
                    finish()
                }
                .addOnFailureListener { e ->
                    // 데이터 추가 실패
                }

            // 게시글 저장 후 입력 필드 초기화
            postTitle.text.clear()
            postCont.text.clear()
        }

        // 뒤로 가기 버튼
        btnBack.setOnClickListener {
            postTitle.text.clear()
            postCont.text.clear()
            onBackPressed()
        }

        // 입력 필드의 텍스트 변경 감지
        postTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        postCont.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 가장 최근 게시글 ID 가져오기
    private fun getLatestPostId() {
        firestore.collection("Posts")
            .orderBy("post_id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    latestPostId = document.getString("post_id") ?: "0"
                } else {
                    latestPostId = "0"
                }
            }
            .addOnFailureListener { e ->
                latestPostId = "0"
            }
    }

    // 새로운 게시글 ID 생성
    private fun generateNewPostId(): String {
        val latestId = latestPostId.toIntOrNull() ?: 0
        return (latestId + 1).toString()
    }

    // 입력 필드의 텍스트 변경 시 호출되는 메서드
    private fun onTextChanged() {
        val postTitle = findViewById<EditText>(R.id.post_title)
        val postCont = findViewById<EditText>(R.id.post_cont)
        val postSave = findViewById<Button>(R.id.post_save)

        val inputTitle = postTitle.text.toString()
        val inputCont = postCont.text.toString()

        // 둘 다 내용이 없을 경우 버튼 비활성화, 그렇지 않을 경우 활성화
        postSave.isEnabled = inputTitle.isNotEmpty() && inputCont.isNotEmpty()
    }
}