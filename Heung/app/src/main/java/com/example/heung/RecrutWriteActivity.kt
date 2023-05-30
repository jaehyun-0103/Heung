package com.example.heung

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.heung.R
import com.google.firebase.firestore.FirebaseFirestore

class RecrutWriteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var uploadButton: AppCompatButton
    private lateinit var backButton: AppCompatImageButton

    private lateinit var buskingFilterButton: AppCompatButton
    private lateinit var classFilterButton: AppCompatButton

    private lateinit var buskingLayout: LinearLayout
    private lateinit var classLayout: LinearLayout

    private var selectedFilter: String = "" // 선택된 필터를 저장하는 변수

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recrutwrite)

        // Firebase Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // 뷰 초기화
        titleEditText = findViewById(R.id.recruit_title)
        contentEditText = findViewById(R.id.recruit_content)
        uploadButton = findViewById(R.id.recruit_upload)
        backButton = findViewById(R.id.recruit_btn_back)

        buskingFilterButton = findViewById(R.id.recruit_filter_busking)
        classFilterButton = findViewById(R.id.recruit_filter_class)

        buskingLayout = findViewById(R.id.recruit_filter_busking_layout)
        classLayout = findViewById(R.id.recruit_filter_class_layout)

        // 필터 버튼 클릭 리스너 설정
        buskingFilterButton.setOnClickListener {
            selectFilter("버스킹")
        }

        classFilterButton.setOnClickListener {
            selectFilter("클래스")
        }

        // 업로드 버튼 클릭 리스너 설정
        uploadButton.setOnClickListener {
            // 모집글 업로드 처리
            uploadRecruit()
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    // 필터 선택
    private fun selectFilter(filter: String) {
        selectedFilter = filter
        // 선택된 필터에 따라 UI 업데이트
        if (filter == "버스킹") {
            buskingLayout.visibility = View.VISIBLE
            classLayout.visibility = View.GONE
        } else if (filter == "클래스") {
            buskingLayout.visibility = View.GONE
            classLayout.visibility = View.VISIBLE
        }
    }

    private fun uploadRecruit() {
        // 제목과 내용을 에딧텍스트에서 가져옴
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()

        // 필터 선택 여부 확인
        if (selectedFilter.isEmpty()) {
            Toast.makeText(this, "필터를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 모집글 데이터 생성
        val recruitData = hashMapOf(
            "recruit_title" to title,
            "recruit_content" to content,
            "recruit_type" to selectedFilter // 선택된 필터 값 추가
        )

        // Firebase Firestore에 모집글 업로드
        firestore.collection("Recruits")
            .add(recruitData)
            .addOnSuccessListener { documentReference ->
                // 업로드 성공
                Toast.makeText(this, "모집글이 업로드되었습니다!", Toast.LENGTH_SHORT).show()
                // 이전 activity로 돌아가기
                finish()
            }
            .addOnFailureListener { e ->
                // 업로드 실패
                Toast.makeText(this, "모집글 업로드 실패: $e", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        // 뒤로 가기 버튼 클릭 시 이전 activity로 돌아가기
        finish()
    }
}