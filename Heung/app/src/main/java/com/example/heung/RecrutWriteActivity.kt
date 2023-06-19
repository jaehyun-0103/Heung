package com.example.heung

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.sdk.user.UserApiClient
import data.Recruits
import java.text.SimpleDateFormat
import java.util.*

class RecrutWriteActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var uploadButton: AppCompatButton
    private lateinit var backButton: AppCompatImageButton
    private lateinit var buskingFilterButton: AppCompatButton
    private lateinit var classFilterButton: AppCompatButton
    private lateinit var buskingLayout: LinearLayout
    private lateinit var classLayout: LinearLayout
    private lateinit var buskingDateEditText: EditText
    private lateinit var buskingSessionEditText: EditText
    private lateinit var classTypeEditText: EditText
    private lateinit var classDate2EditText: EditText
    private lateinit var maxCapacityPicker: NumberPicker
    private lateinit var maxCapacityPickerClass: NumberPicker
    private lateinit var firestore: FirebaseFirestore
    private var selectedFilter: String = ""
    private var selectedDate: String = ""
    private var nextRecruitId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recrutwrite)

        firestore = FirebaseFirestore.getInstance()

        titleEditText = findViewById(R.id.recruit_title)
        contentEditText = findViewById(R.id.recruit_content)
        uploadButton = findViewById(R.id.recruit_upload)
        backButton = findViewById(R.id.recruit_btn_back)
        buskingFilterButton = findViewById(R.id.recruit_filter_busking)
        classFilterButton = findViewById(R.id.recruit_filter_class)
        buskingLayout = findViewById(R.id.recruit_filter_busking_layout)
        classLayout = findViewById(R.id.recruit_filter_class_layout)
        buskingDateEditText = findViewById(R.id.recruit_filter_busking_date)
        buskingSessionEditText = findViewById(R.id.recruit_filter_busking_session)
        classTypeEditText = findViewById(R.id.recruit_filter_class_type)
        classDate2EditText = findViewById(R.id.recruit_filter_class_date2)
        maxCapacityPicker = findViewById(R.id.recruit_filter_busking_capacity)
        maxCapacityPickerClass = findViewById(R.id.recruit_filter_class_capacity)

        buskingFilterButton.setOnClickListener {
            selectFilter("버스킹")
        }

        classFilterButton.setOnClickListener {
            selectFilter("클래스")
        }

        uploadButton.setOnClickListener {
            uploadRecruit()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        buskingDateEditText.setOnClickListener { view ->
            showDatePickerDialog(view)
        }

        // NumberPicker 설정
        maxCapacityPicker.minValue = 0
        maxCapacityPicker.maxValue = 100

        maxCapacityPickerClass.minValue = 0
        maxCapacityPickerClass.maxValue = 100

        getNextRecruitId()
    }

    private fun selectFilter(filter: String) {
        selectedFilter = filter
        if (filter == "버스킹") {
            buskingLayout.visibility = View.VISIBLE
            classLayout.visibility = View.GONE
            buskingDateEditText.setText(selectedDate)
        } else if (filter == "클래스") {
            buskingLayout.visibility = View.GONE
            classLayout.visibility = View.VISIBLE
            classDate2EditText.setText(selectedDate)
        }
    }

    fun showDatePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedMonth = monthOfYear + 1
                selectedDate = "$year-$selectedMonth-$dayOfMonth"

                if (selectedFilter == "버스킹" || selectedFilter == "클래스") {
                    buskingDateEditText.setText(selectedDate)
                    classDate2EditText.setText(selectedDate)
                }
            }, year, month, day)

        val currentDate = Calendar.getInstance()
        val initialYear = currentDate.get(Calendar.YEAR)
        val initialMonth = currentDate.get(Calendar.MONTH)
        val initialDay = currentDate.get(Calendar.DAY_OF_MONTH)
        datePickerDialog.updateDate(initialYear, initialMonth, initialDay)

        datePickerDialog.show()
    }

    private fun getNextRecruitId() {
        firestore.collection("Recruits")
            .orderBy("recruit_id", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val lastRecruit = querySnapshot.documents[0].toObject(Recruits::class.java)
                    nextRecruitId = if (lastRecruit != null) {
                        (lastRecruit.recruit_id.toIntOrNull()?.plus(1) ?: 1).toString()
                    } else {
                        "1"
                    }
                } else {
                    nextRecruitId = "1" // If no documents exist, set recruit_id to 1
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "게시글 번호 가져오기 실패: $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadRecruit() {
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedFilter.isEmpty()) {
            Toast.makeText(this, "필터를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val endDate = if (selectedFilter == "버스킹") {
            buskingDateEditText.text.toString()
        } else {
            classDate2EditText.text.toString()
        }

        if (endDate.isEmpty()) {
            Toast.makeText(this, "모집 기간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCurrentDate = Calendar.getInstance().time

        val selectedEndDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDate)
        if (selectedEndDate != null && selectedEndDate.before(selectedCurrentDate)) {
            Toast.makeText(this, "오늘 이전의 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val recruitSession = buskingSessionEditText.text.toString()
        val recruitClass = classTypeEditText.text.toString()

        if (selectedFilter == "버스킹" && recruitSession.isEmpty()) {
            Toast.makeText(this, "세션 타입을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val maxCapacity = if (selectedFilter == "버스킹") {
            maxCapacityPicker.value.toString()
        } else {
            maxCapacityPickerClass.value.toString()
        }

        // 사용자 정보 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
                Toast.makeText(this, "사용자 정보 가져오기 실패", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val currentUserId = user.id.toString() // 사용자 ID

                // 사용자 닉네임 가져오기
                firestore.collection("Users")
                    .whereEqualTo("user_id", currentUserId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userNickname = querySnapshot.documents[0].getString("user_nickname")

                            // 가져온 userId와 userNickname을 사용하여 Recruits 객체를 생성합니다.
                            val recruit = Recruits(
                                recruit_id = nextRecruitId,
                                user_id = currentUserId,
                                recruit_title = title,
                                recruit_content = content,
                                recruit_type = selectedFilter,
                                recruit_endDate = endDate,
                                recruit_date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedCurrentDate),
                                recruit_session = recruitSession,
                                recruit_class = recruitClass,
                                recruit_curr = "0",
                                recruit_max = maxCapacity,
                                user_nickname = userNickname.orEmpty()
                            )

                            // Firestore에 모집글 업로드
                            firestore.collection("Recruits")
                                .add(recruit)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this, "모집글이 업로드되었습니다!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "모집글 업로드 실패: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "사용자 정보 가져오기 실패: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
