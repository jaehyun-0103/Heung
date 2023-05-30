package com.example.heung

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class CalDetailActivity : AppCompatActivity() {
    private lateinit var calTitleTextView: TextView
    private lateinit var calLocationTextView: TextView
    private lateinit var calStartTimeTextView: TextView
    private lateinit var calEndTimeTextView: TextView
    private lateinit var calMemoTextView: TextView
    private lateinit var DateTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caldetail)

        // 인텐트로 전달된 데이터 받기
        val userId = intent.getStringExtra("userId")
        val calTitle = intent.getStringExtra("calTitle")
        val calLocation = intent.getStringExtra("calLocation")
        val calStartTime = intent.getLongExtra("calStartTime", 0)
        val calEndTime = intent.getLongExtra("calEndTime", 0)
        val calMemo = intent.getStringExtra("calMemo")
        val selectedDate = intent.getStringExtra("selectedDate")

        // TextView 찾기
        calTitleTextView = findViewById(R.id.performName)
        calLocationTextView = findViewById(R.id.performLocation)
        calStartTimeTextView = findViewById(R.id.startTime)
        calEndTimeTextView = findViewById(R.id.endTime)
        calMemoTextView = findViewById(R.id.performMemo)
        DateTextView = findViewById(R.id.Date)

        // 데이터를 TextView에 표시
        calTitleTextView.text = calTitle
        calLocationTextView.text = calLocation
        calStartTimeTextView.text = formatTime(calStartTime)
        calEndTimeTextView.text = formatTime(calEndTime)
        calMemoTextView.text = calMemo
        DateTextView.text = selectedDate

        // 수정 버튼 클릭 리스너 설정
        val editButton = findViewById<Button>(R.id.calEdit)
        editButton.visibility = if (userId?.let { isCurrentUser(it) } == true) View.VISIBLE else View.GONE
        editButton.setOnClickListener {
            // 수정 버튼이 클릭되었을 때의 동작 구현
            // 예: 수정 화면으로 이동
        }

        // 삭제 버튼 클릭 리스너 설정
        val deleteButton = findViewById<Button>(R.id.calDelect)
        deleteButton.visibility = if (userId?.let { isCurrentUser(it) } == true) View.VISIBLE else View.GONE
        deleteButton.setOnClickListener {
            // 삭제 버튼이 클릭되었을 때의 동작 구현
            // 예: 데이터 삭제 등
        }

    }

    // 시간 형식을 변환하는 함수
    private fun formatTime(date: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = date

        val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }

    // 현재 사용자와 동일한 사용자인지 확인하는 함수
    private fun isCurrentUser(userId: String): Boolean {
        val currentUserId = "currentUserId"
        return userId == currentUserId
    }
}