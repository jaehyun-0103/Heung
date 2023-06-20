package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient

class LogcheckActivity: AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logcheck)

        // 사용자 정보 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                userId = user.id.toString()
                checkUserIdExists()
            }
        }

        val check = findViewById<Button>(R.id.check)
        check.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUserInfo() {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("Users")
        val userNickname = userId
        val user = hashMapOf<String, Any>(
            "user_id" to userId,
            "user_nickname" to userNickname
        )

        usersCollection.document(userId)
            .set(user)
            .addOnSuccessListener {
                // 등록 성공
            }
            .addOnFailureListener { e ->
                // 등록 실패
            }
    }

    private fun checkUserIdExists() {
        val usersCollection = firestore.collection("Users")
        usersCollection
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // 사용자 정보가 이미 존재하는 경우
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                } else {
                    // 사용자 정보가 존재하지 않는 경우
                    registerUserInfo()
                }
            }
            .addOnFailureListener { exception ->
                // 오류 처리
            }
    }
}