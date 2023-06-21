package com.example.heung

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import data.Recruits
import data.Users

class RecruContActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var titleTextView: TextView
    private lateinit var nicknameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var recruitType: TextView
    private lateinit var contentTextView: TextView
    private lateinit var endDateTextView: TextView
    private lateinit var maxParticipantsTextView: TextView
    private lateinit var currentParticipantsTextView: TextView
    private lateinit var recruitcontact: TextView
    private lateinit var deleteButton: Button
    private lateinit var maxCapacityPicker: NumberPicker
    private lateinit var completeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recrucont)

        firestore = FirebaseFirestore.getInstance()
        titleTextView = findViewById(R.id.recruit_content_title)
        nicknameTextView = findViewById(R.id.recruit_content_author)
        dateTextView = findViewById(R.id.recruit_content_date)
        typeTextView = findViewById(R.id.recruit_content_type)
        recruitType = findViewById(R.id.recruit_type)
        contentTextView = findViewById(R.id.recruit_content_content)
        endDateTextView = findViewById(R.id.recruit_content_endDate)
        maxParticipantsTextView = findViewById(R.id.recruit_content_max)
        currentParticipantsTextView = findViewById(R.id.recruit_content_curr)
        recruitcontact = findViewById(R.id.recruit_contact)
        deleteButton = findViewById(R.id.recruit_reply_delete)
        maxCapacityPicker = findViewById(R.id.recruit_curr)
        completeButton = findViewById(R.id.complete)

        val recruitId = intent.getStringExtra("recruit_id")

        if (recruitId != null) {
            loadRecruit(recruitId)
        } else {
            Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        deleteButton.setOnClickListener {
            deleteRecruit(recruitId!!)
        }

        val backButton = findViewById<ImageButton>(R.id.recruit_content_button_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // NumberPicker 설정
        maxCapacityPicker.minValue = 0
        maxCapacityPicker.maxValue = 100

        completeButton.setOnClickListener {
            val currParticipants = maxCapacityPicker.value.toString()
            if (recruitId != null) {
                if (currParticipants.toInt() > maxParticipantsTextView.text.toString().replace("명 구함", "").toInt()) {
                    Toast.makeText(this, "인원을 다시 배정해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    updateRecruitCurr(recruitId, currParticipants)
                }
            }
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                var userId = user.id.toString()
                val recruitsCollection = firestore.collection("Recruits")
                recruitsCollection
                    .whereEqualTo("recruit_id", recruitId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val documentSnapshot = querySnapshot.documents[0]
                            val recruit = documentSnapshot.toObject(Recruits::class.java)
                            if (recruit != null) {
                                // 게시글 작성자와 현재 사용자의 아이디가 일치하는지 확인하여 버튼 처리
                                if (recruit.user_id == userId) {
                                    deleteButton.visibility = View.VISIBLE
                                    maxCapacityPicker.visibility = View.VISIBLE
                                    completeButton.visibility = View.VISIBLE
                                } else {
                                    deleteButton.visibility = View.GONE
                                    maxCapacityPicker.visibility = View.GONE
                                    completeButton.visibility = View.GONE
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val recruitId = intent.getStringExtra("recruit_id")
        if (recruitId != null) {
            loadRecruit(recruitId)
        }
    }

    private fun loadRecruit(recruitId: String) {
        val recruitsCollection = firestore.collection("Recruits")
        recruitsCollection
            .whereEqualTo("recruit_id", recruitId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val recruit = documentSnapshot.toObject(Recruits::class.java)
                    if (recruit != null) {
                        titleTextView.text = recruit.recruit_title
                        val usersCollection = firestore.collection("Users")
                        usersCollection
                            .whereEqualTo("user_id", recruit.user_id)
                            .get()
                            .addOnSuccessListener { userQuerySnapshot ->
                                if (!userQuerySnapshot.isEmpty) {
                                    val userDocumentSnapshot = userQuerySnapshot.documents[0]
                                    val user = userDocumentSnapshot.toObject(Users::class.java)
                                    if (user != null) {
                                        nicknameTextView.text = user.user_nickname
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->

                            }
                        dateTextView.text = recruit.recruit_date
                        typeTextView.text = recruit.recruit_type
                        if (recruit.recruit_session.isNotEmpty()) {
                            recruitType.text = recruit.recruit_session + " 구함"
                        } else {
                            recruitType.text = recruit.recruit_class + " 수업"
                        }
                        contentTextView.text = recruit.recruit_content
                        endDateTextView.text = recruit.recruit_endDate + "까지"
                        maxParticipantsTextView.text = recruit.recruit_max + "명 구함"
                        currentParticipantsTextView.text = recruit.recruit_curr + "명 참여"
                        recruitcontact.text = recruit.recruit_contact
                    }
                } else {
                    Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "게시글을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun deleteRecruit(recruitId: String) {
        firestore.collection("Recruits")
            .whereEqualTo("recruit_id", recruitId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val recruit = documentSnapshot.toObject(Recruits::class.java)
                    if (recruit != null) {
                        documentSnapshot.reference.delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "게시글 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "게시글을 삭제하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun updateRecruitCurr(recruitId: String, currParticipants: String) {
        val recruitsCollection = firestore.collection("Recruits")
        val recruitQuery = recruitsCollection.whereEqualTo("recruit_id", recruitId)

        recruitQuery
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    val recruitRef = documentSnapshot.reference

                    val data = hashMapOf<String, Any>(
                        "recruit_curr" to currParticipants
                    )

                    recruitRef
                        .update(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "참가 인원이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "참가 인원 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "게시글을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
