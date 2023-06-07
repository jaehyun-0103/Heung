package com.example.heung

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.google.firebase.firestore.FirebaseFirestore
import data.Recruits

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

        val recruitId = intent.getStringExtra("recruit_id")

        if (recruitId != null) {
            loadRecruit(recruitId)
        } else {
            Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        val backButton = findViewById<AppCompatImageButton>(R.id.recruit_content_button_back)
        backButton.setOnClickListener {
            onBackPressed()
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
                        nicknameTextView.text = recruit.user_nickname
                        dateTextView.text = recruit.recruit_date
                        typeTextView.text = recruit.recruit_type
                        sessionTypeTextView.text = recruit.recruit_session
                        classTypeTextView.text = recruit.recruit_class
                        contentTextView.text = recruit.recruit_content
                        endDateTextView.text = recruit.recruit_endDate
                        maxParticipantsTextView.text = recruit.recruit_max
                        currentParticipantsTextView.text = recruit.recruit_curr
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
