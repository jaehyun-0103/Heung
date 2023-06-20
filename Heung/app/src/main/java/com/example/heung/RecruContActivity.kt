package com.example.heung

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import data.Recruits
import data.Reply
import java.text.SimpleDateFormat
import java.util.*

class RecruContActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var musicIconImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var nicknameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var sessionTypeTextView: TextView
    private lateinit var classTypeTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var endDateTextView: TextView
    private lateinit var maxParticipantsTextView: TextView
    private lateinit var currentParticipantsTextView: TextView
    private lateinit var replyRecyclerView: RecyclerView
    private lateinit var replyAdapter: ReplyAdapter
    private lateinit var replyList: MutableList<Reply>
    private lateinit var replyEditText: EditText
    private lateinit var replyButton: Button
    private lateinit var participateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var editButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recrucont)

        firestore = FirebaseFirestore.getInstance()
        musicIconImageView = findViewById(R.id.recruit_content_profile)
        titleTextView = findViewById(R.id.recruit_content_title)
        nicknameTextView = findViewById(R.id.recruit_content_author)
        dateTextView = findViewById(R.id.recruit_content_date)
        typeTextView = findViewById(R.id.recruit_content_type)
        sessionTypeTextView = findViewById(R.id.recruit_content_session)
        classTypeTextView = findViewById(R.id.recruit_content_class)
        contentTextView = findViewById(R.id.recruit_content_content)
        endDateTextView = findViewById(R.id.recruit_content_endDate)
        maxParticipantsTextView = findViewById(R.id.recruit_content_max)
        currentParticipantsTextView = findViewById(R.id.recruit_content_curr)
        replyRecyclerView = findViewById(R.id.recruit_content_replyRecyclerView)
        replyEditText = findViewById(R.id.edit_reply)
        replyButton = findViewById(R.id.btn_reply)
        participateButton = findViewById(R.id.recruit_content_participant)
        deleteButton = findViewById(R.id.recruit_reply_delete)
        editButton = findViewById(R.id.recruit_reply_modify)

        val recruitId = intent.getStringExtra("recruit_id")

        if (recruitId != null) {
            loadRecruit(recruitId)
            loadReplies(recruitId)
        } else {
            Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        replyList = mutableListOf()
        replyAdapter = ReplyAdapter(replyList)
        replyRecyclerView.adapter = replyAdapter
        replyRecyclerView.layoutManager = LinearLayoutManager(this)

        replyButton.setOnClickListener {
            val replyText = replyEditText.text.toString().trim()
            if (replyText.isNotEmpty()) {
                addReply(recruitId!!, replyText)
                replyEditText.text.clear()
            } else {
                Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        participateButton.setOnClickListener {
            participateRecruit(recruitId!!)
        }

        deleteButton.setOnClickListener {
            deleteRecruit(recruitId!!)
        }

        editButton.setOnClickListener {
            navigateToWriteActivity(recruitId!!)
        }

        val backButton = findViewById<ImageButton>(R.id.recruit_content_button_back)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        val recruitId = intent.getStringExtra("recruit_id")
        if (recruitId != null) {
            loadRecruit(recruitId)
            loadReplies(recruitId)
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
                        nicknameTextView.text = "닉네임"
                        dateTextView.text = recruit.recruit_date
                        typeTextView.text = recruit.recruit_type
                        sessionTypeTextView.text = recruit.recruit_session + " 구함"
                        classTypeTextView.text = recruit.recruit_class + " 수업"
                        contentTextView.text = recruit.recruit_content
                        endDateTextView.text = recruit.recruit_endDate + "까지"
                        maxParticipantsTextView.text = recruit.recruit_max + "명 구함"
                        currentParticipantsTextView.text = recruit.recruit_curr + "명 참여"
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

    private fun loadReplies(recruitId: String) {
        val replyCollection = firestore.collection("Reply")
        replyCollection
            .whereEqualTo("recruitId", recruitId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                replyList.clear()
                for (document in querySnapshot.documents) {
                    val reply = document.toObject(Reply::class.java)
                    reply?.let {
                        replyList.add(reply)
                    }
                }
                replyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "댓글을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addReply(recruitId: String, replyText: String) {
        val currentDateTime = getCurrentDateTime() // 현재 날짜 및 시간 값 가져오기

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val userId = user.id.toString()

                val replyData = hashMapOf(
                    "recruitId" to recruitId,
                    "reply" to replyText,
                    "reply_date" to currentDateTime,
                    "reply_id" to null as String?,
                    "comment_id" to null as String?,
                    "user_id" to userId
                )

                firestore.collection("Reply")
                    .add(replyData)
                    .addOnSuccessListener { documentReference ->
                        val replyId = documentReference.id
                        val reply = Reply(replyId, null, userId, replyText, currentDateTime)

                        replyList.add(reply)
                        replyAdapter.notifyDataSetChanged()

                        documentReference.update("reply_id", replyId)
                            .addOnSuccessListener {
                                Toast.makeText(this, "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "댓글 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "Error adding reply", e)
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "댓글 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error adding reply", e)
                    }
            }
        }
    }

    private fun participateRecruit(recruitId: String) {
        val recruitRef = firestore.collection("Recruits").document(recruitId)
        recruitRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val recruit = documentSnapshot.toObject(Recruits::class.java)
                if (recruit != null) {
                    val currentParticipants = recruit.recruit_curr.toIntOrNull()
                    val maxParticipants = recruit.recruit_max.toIntOrNull()

                    if (currentParticipants != null && maxParticipants != null) {
                        if (currentParticipants < maxParticipants) {
                            recruitRef.update("recruit_curr", (currentParticipants + 1).toString())
                                .addOnSuccessListener {
                                    currentParticipantsTextView.text =
                                        "${currentParticipants + 1} 명 참여"
                                    Toast.makeText(this, "신청되었습니다.", Toast.LENGTH_SHORT).show()

                                    if (currentParticipants + 1 == maxParticipants) {
                                        Toast.makeText(this, "최대 인원입니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(this, "참여에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "최대 모집인원입니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "참여에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "게시글을 찾는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
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

    private fun navigateToWriteActivity(recruitId: String) {
        val intent = Intent(this, RecrutWriteActivity::class.java)
        intent.putExtra("recruit_id", recruitId)
        intent.putExtra("is_edit", true) // 수정 모드 플래그 추가

        // 게시글 내용을 불러오기 위해 Firestore에서 데이터를 가져옴
        firestore.collection("Recruits")
            .document(recruitId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val recruit = documentSnapshot.toObject(Recruits::class.java)
                    if (recruit != null) {
                        intent.putExtra("recruit_title", recruit.recruit_title)
                        intent.putExtra("recruit_content", recruit.recruit_content)
                        intent.putExtra("recruit_type", recruit.recruit_type)
                        intent.putExtra("recruit_endDate", recruit.recruit_endDate)
                        intent.putExtra("recruit_session", recruit.recruit_session)
                        intent.putExtra("recruit_class", recruit.recruit_class)
                        intent.putExtra("recruit_max", recruit.recruit_max)
                    }
                }
                startActivity(intent)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "게시글을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}