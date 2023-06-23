package com.example.heung

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import java.text.SimpleDateFormat
import java.util.*

class CalWriteActivity : AppCompatActivity() {
    private lateinit var startTimeImageView: ImageView
    private lateinit var endTimeImageView: ImageView
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView
    private var selectedStartTime: Calendar = Calendar.getInstance()
    private var selectedEndTime: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calwrite)

        // MainActivity에서 선택한 날짜 받아오기
        val selectedDate = intent.getStringExtra("selectedDate")

        startTimeImageView = findViewById(R.id.iv_schedule_time_start)
        startTimeTextView = findViewById(R.id.startTime)
        startDateTextView = findViewById(R.id.startDate)
        startDateTextView.text = selectedDate
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
            val calId = UUID.randomUUID().toString()
            val collectionName = "Calendar"

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    // 프로필 정보 가져오기 실패
                } else if (user != null) {
                    val userId = user.id.toString() // 사용자 ID

                    // Firestore에 일정 데이터 추가
                    val calEvent = hashMapOf(
                        "cal_title" to inputTitle,
                        "cal_location" to inputLocation,
                        "cal_memo" to inputMemo,
                        "cal_date" to inputDate,
                        "cal_id" to calId,
                        "user_id" to userId,
                        "cal_startDate" to selectedStartTime.timeInMillis,
                        "cal_endDate" to selectedEndTime.timeInMillis
                    )

                    // Firestore에 일정 데이터 저장
                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection(collectionName)
                        .add(calEvent)
                        .addOnSuccessListener {
                            // 일정 저장 성공
                            Toast.makeText(this, "일정이 저장되었습니다.", Toast.LENGTH_SHORT).show()

                            // 메인 화면으로 이동
                            val intent = Intent(this, CalActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(0, 0)
                            finish() // 현재 액티비티 종료
                        }
                        .addOnFailureListener { exception ->
                            // 일정 저장 실패
                            Toast.makeText(this, "일정 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    // 게시글 저장 후 입력 필드 초기화
                    calTitle.text.clear()
                    calLocation.text.clear()
                    calMemo.text.clear()
                }
            }
        }

        // 뒤로 가기 버튼
        btnBack.setOnClickListener {
            calTitle.text.clear()
            calLocation.text.clear()
            calMemo.text.clear()
            onBackPressed()
            overridePendingTransition(R.transition.slide_down, R.transition.fade_in)
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

        calSave.isEnabled =
            inputTitle.isNotEmpty() && inputLocation.isNotEmpty() && inputMemo.isNotEmpty() && startTime != "오후 12:00" && endTime != "오후 12:00"
    }
}