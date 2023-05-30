package com.example.heung

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import data.Calendar
import java.util.*

class CalActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalAdapter
    private lateinit var calendar: MutableList<Calendar>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal)

        recyclerView = findViewById(R.id.cal_data)
        recyclerView.layoutManager = LinearLayoutManager(this)

        calendar = mutableListOf() // postList 초기화
        adapter = CalAdapter(calendar)
        recyclerView.adapter = adapter

        firestore = FirebaseFirestore.getInstance()

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val calWriteBtn = findViewById<Button>(R.id.calwriteBtn)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"

            // 작성 버튼 클릭 시
            calWriteBtn.setOnClickListener {
                val intent = Intent(this, CalWriteActivity::class.java)
                intent.putExtra("selectedDate", selectedDate)
                startActivity(intent)
            }

            firestore.collection("Calendar")
                .whereEqualTo("cal_date", selectedDate)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) { // 에러 처리
                        return@addSnapshotListener
                    }

                    querySnapshot?.let {
                        calendar.clear()
                        for (document in it.documents) {
                            val cal = document.toObject(Calendar::class.java)
                            cal?.let {
                                calendar.add(cal)
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

            // 게시글 목록 클릭 이벤트 처리
            adapter.setOnItemClickListener { position ->
                val clickedCal = calendar[position]

                // 인텐트 생성 및 데이터 전달
                val intent = Intent(this, CalDetailActivity::class.java)
                intent.putExtra("userId", clickedCal.user_id)
                intent.putExtra("selectedDate", selectedDate)
                intent.putExtra("calTitle", clickedCal.cal_title)
                intent.putExtra("calLocation", clickedCal.cal_location)
                intent.putExtra("calStartTime", clickedCal.cal_startDate)
                intent.putExtra("calEndTime", clickedCal.cal_endDate)
                intent.putExtra("calMemo", clickedCal.cal_memo)
                startActivity(intent)

            }
        }
    }
}