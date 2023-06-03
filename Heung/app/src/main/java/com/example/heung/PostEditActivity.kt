package com.example.heung

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PostEditActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postId: String
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postwrite)

        etTitle = findViewById(R.id.post_title)
        etContent = findViewById(R.id.post_cont)
        val btnSave = findViewById<Button>(R.id.post_save)

        firestore = FirebaseFirestore.getInstance()

        val intent = intent
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        postId = intent.getStringExtra("postId")!!
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        etTitle.setText(postTitle)
        etContent.setText(postContent)

        btnSave.setOnClickListener {
            val newTitle = etTitle.text.toString()
            val newContent = etContent.text.toString()

            updatePost(postId, newTitle, newContent)
        }

        // 뒤로 가기 버튼
        btnBack.setOnClickListener {

            onBackPressed()
        }
    }

    private fun updatePost(postId: String, newTitle: String, newContent: String) {
        val collectionName = "Posts"

        val collectionRef = FirebaseFirestore.getInstance().collection(collectionName)
        val query = collectionRef.whereEqualTo("post_id", postId)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot.documents) {
                    documentSnapshot.reference.update(
                        "post_title", newTitle,
                        "post_content", newContent
                    )
                        .addOnSuccessListener {
                            Toast.makeText(this, "수정 성공했습니다.", Toast.LENGTH_SHORT).show()

                            onBackPressed()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "수정 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                // 쿼리 실행 실패
            }
    }
}
