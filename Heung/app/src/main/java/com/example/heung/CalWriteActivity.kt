package com.example.heung

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CalWriteActivity: AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var startTimeImageView: ImageView
    private lateinit var endTimeImageView: ImageView
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView
    private var selectedStartTime: Calendar = Calendar.getInstance()
    private var selectedEndTime: Calendar = Calendar.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calwrite)

        // MainActivity에서 선택한 날짜 받아오기
        val selectedDate = intent.getStringExtra("selectedDate")

        startTimeImageView= findViewById(R.id.iv_schedule_time_start)
        startTimeTextView = findViewById(R.id.startTime)
        startDateTextView = findViewById(R.id.startDate)
        startDateTextView.text = selectedDate // 선택한 날짜를 startDate TextView에 표시
        startTimeImageView.setOnClickListener {
            showTimePickerDialog(true)
        }

        endTimeImageView = findViewById(R.id.iv_schedule_time_end)
        endTimeTextView = findViewById(R.id.endTime)
        endDateTextView = findViewById(R.id.endDate)
        endDateTextView.text = selectedDate
        endTimeImageView.setOnClickListener {
            showTimePickerDialog(false)
        }
        var calSave = findViewById<Button>(R.id.cal_save)
        val calTitle = findViewById<EditText>(R.id.cal_title)
        val calLocation = findViewById<EditText>(R.id.cal_location)
        val calMemo = findViewById<EditText>(R.id.cal_memo)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        // 초기 상태에서 버튼 비활성화
        calSave.isEnabled = false

        // 저장 버튼 클릭 이벤트 처리
        calSave.setOnClickListener {
            val inputTitle = calTitle.text.toString()
            val inputLocation = calLocation.text.toString()
            val inputMemo = calMemo.text.toString()
            val inputDate = selectedDate
            val collectionName = "Calendar"
            val userId = "user_id"

            // Firestore에 일정 데이터 추가
            val calEvent = hashMapOf(
                "cal_title" to inputTitle,
                "cal_location" to inputLocation,
                "cal_memo" to inputMemo,
                "cal_date" to inputDate,
                "user_id" to userId,
                "cal_startDate" to selectedStartTime.timeInMillis,
                "cal_endDate" to selectedEndTime.timeInMillis
                )

            // 데이터베이스에 데이터 추가
            firestore.collection(collectionName)
                .add(calEvent)
                .addOnSuccessListener {
                    // 데이터 추가 성공
                }
                .addOnFailureListener { e ->
                    // 데이터 추가 실패
                }

            // 게시글 저장 후 입력 필드 초기화
            calTitle.text.clear()
            calLocation.text.clear()
            calMemo.text.clear()
        }
        // 뒤로 가기 버튼
        btnBack.setOnClickListener {
            calTitle.text.clear()
            calLocation.text.clear()
            calMemo.text.clear()
            onBackPressed()
        }
        // 입력 필드의 텍스트 변경 감지
        calTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        calLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        calMemo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun showTimePickerDialog(isStartTime: Boolean) {
        val currentTime = if (isStartTime) selectedStartTime else selectedEndTime
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
                currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                currentTime.set(Calendar.MINUTE, minute)

                updateSelectedTime(isStartTime)

            },
            currentHour,
            currentMinute,
            false
        )
        timePickerDialog.show()
    }

    private fun updateSelectedTime(isStartTime: Boolean) {

        val selectedTime = if (isStartTime) selectedStartTime else selectedEndTime
// 시작 시간을 타임스탬프로부터 변환하여 버튼에 표시합니다.
        val timeInMillis = selectedTime.timeInMillis
        val timeFormatted = SimpleDateFormat("a hh:mm", Locale.getDefault()).format(timeInMillis)

        if (isStartTime) {
            startTimeTextView.text = timeFormatted
        } else {
            endTimeTextView.text = timeFormatted
        }
    }
    private fun onTextChanged() {
        val calTitle = findViewById<EditText>(R.id.cal_title)
        val calLocation = findViewById<EditText>(R.id.cal_location)
        val calMemo = findViewById<EditText>(R.id.cal_memo)
        val startTimeTextView = findViewById<TextView>(R.id.startTime)
        val endTimeTextView = findViewById<TextView>(R.id.endTime)
        val calSave = findViewById<Button>(R.id.cal_save)

        val inputTitle = calTitle.text.toString()
        val inputLocation = calLocation.text.toString()
        val inputMemo = calMemo.text.toString()
        val startTime = startTimeTextView.text.toString()
        val endTime = endTimeTextView.text.toString()

        calSave.isEnabled = inputTitle.isNotEmpty() && inputLocation.isNotEmpty() && inputMemo.isNotEmpty() && startTime != "오후 12:00" && endTime != "오후 12:00"

    }
}