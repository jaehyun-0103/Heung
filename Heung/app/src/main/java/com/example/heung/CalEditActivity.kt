package com.example.heung

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
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

    private val firestore = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calwrite)

        editCalTitle = findViewById(R.id.cal_title)
        editCalLocation = findViewById(R.id.cal_location)
        editCalStartTime = findViewById(R.id.startTime)
        editCalEndTime = findViewById(R.id.endTime)
        editCalMemo = findViewById(R.id.cal_memo)

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
        var calSave = findViewById<Button>(R.id.cal_save)

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

            // 데이터 업데이트
            updateCalendarEvent(calId, updatedTitle, updatedLocation, updatedStartTime, updatedEndTime, updatedMemo)
        }
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
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = date

        val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }

    // 시간을 파싱하는 함수
    private fun parseTime(time: String): Long {
        val inputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        val calendar = java.util.Calendar.getInstance()
        val date = inputFormat.parse(time)
        date?.let {
            calendar.time = date
            return calendar.timeInMillis
        }
        return 0
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
