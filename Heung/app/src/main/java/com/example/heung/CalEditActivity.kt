package com.example.heung

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CalEditActivity : AppCompatActivity() {
    private lateinit var editCalTitle: EditText
    private lateinit var editCalLocation: EditText
    private lateinit var editCalStartTime: TextView
    private lateinit var editCalEndTime: TextView
    private lateinit var editCalMemo: EditText
    private lateinit var startTimeImageView: ImageView
    private lateinit var endTimeImageView: ImageView
    private var selectedStartTime: Calendar = Calendar.getInstance()
    private var selectedEndTime: Calendar = Calendar.getInstance()
    private lateinit var calSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calwrite)

        editCalTitle = findViewById(R.id.cal_title)
        editCalLocation = findViewById(R.id.cal_location)
        editCalStartTime = findViewById(R.id.startTime)
        editCalEndTime = findViewById(R.id.endTime)
        editCalMemo = findViewById(R.id.cal_memo)
        calSave = findViewById(R.id.cal_save)

        startTimeImageView = findViewById(R.id.iv_schedule_time_start)
        startTimeImageView.setOnClickListener {
            showTimePickerDialog(true)
        }

        endTimeImageView = findViewById(R.id.iv_schedule_time_end)
        endTimeImageView.setOnClickListener {
            showTimePickerDialog(false)
        }

        val calTitle = intent.getStringExtra("calTitle")
        val calLocation = intent.getStringExtra("calLocation")
        val calStartTime = intent.getLongExtra("calStartTime", 0)
        val calEndTime = intent.getLongExtra("calEndTime", 0)
        val calMemo = intent.getStringExtra("calMemo")
        val calId = intent.getStringExtra("calId")
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        // 기존 데이터를 EditText에 표시
        editCalTitle.setText(calTitle)
        editCalLocation.setText(calLocation)
        editCalStartTime.setText(formatTime(calStartTime))
        editCalEndTime.setText(formatTime(calEndTime))
        editCalMemo.setText(calMemo)

        calSave.setOnClickListener {
            val updatedTitle = editCalTitle.text.toString()
            val updatedLocation = editCalLocation.text.toString()
            val updatedStartTime = selectedStartTime.timeInMillis
            val updatedEndTime = selectedEndTime.timeInMillis
            val updatedMemo = editCalMemo.text.toString()

            // 데이터가 공백인지 확인하여 저장 버튼 활성화/비활성화
            val isDataValid = isDataValid(updatedTitle, updatedLocation, updatedStartTime, updatedEndTime)
            calSave.isEnabled = isDataValid

            if (isDataValid) {
                // 데이터 업데이트
                updateCalendarEvent(
                    calId,
                    updatedTitle,
                    updatedLocation,
                    updatedStartTime,
                    updatedEndTime,
                    updatedMemo
                )
            } else {
                Toast.makeText(this, "빈 칸을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 초기 버튼 상태 설정
        calSave.isEnabled = isDataValid(
            editCalTitle.text.toString(),
            editCalLocation.text.toString(),
            selectedStartTime.timeInMillis,
            selectedEndTime.timeInMillis
        )

        // EditText의 텍스트 변경 감지
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val isDataValid = isDataValid(
                    editCalTitle.text.toString(),
                    editCalLocation.text.toString(),
                    selectedStartTime.timeInMillis,
                    selectedEndTime.timeInMillis
                )
                calSave.isEnabled = isDataValid
            }
        }

        editCalTitle.addTextChangedListener(textWatcher)
        editCalLocation.addTextChangedListener(textWatcher)
        editCalStartTime.addTextChangedListener(textWatcher)
        editCalEndTime.addTextChangedListener(textWatcher)
        editCalMemo.addTextChangedListener(textWatcher)
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
        val timeFormatted = formatTime(timeInMillis)

        if (isStartTime) {
            editCalStartTime.text = timeFormatted
        } else {
            editCalEndTime.text = timeFormatted
        }
    }

    // 시간 형식을 변환하는 함수
    private fun formatTime(date: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }

    // 데이터 유효성 검사 함수
    private fun isDataValid(
        title: String,
        location: String,
        startTime: Long,
        endTime: Long
    ): Boolean {
        return title.isNotBlank() && location.isNotBlank() && startTime != 0L && endTime != 0L
    }

    // 캘린더 이벤트 업데이트 함수
    private fun updateCalendarEvent(
        calId: String?,
        title: String,
        location: String,
        startTime: Long,
        endTime: Long,
        memo: String
    ) {
        val collectionName = "Calendar"
        val collectionRef = FirebaseFirestore.getInstance().collection(collectionName)
        val query = collectionRef.whereEqualTo("cal_id", calId)
        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot.documents) {
                    documentSnapshot.reference.update(
                        "cal_title", title,
                        "cal_location", location,
                        "cal_start_time", startTime,
                        "cal_end_time", endTime,
                        "cal_memo", memo
                    )
                        .addOnSuccessListener {
                            Toast.makeText(this, "수정 성공했습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, CalActivity::class.java)
                            startActivity(intent)
                            finish()
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