package com.example.heung

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        // 하단바 아이템 선택 이벤트 처리
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    if (this::class.java.canonicalName == MainActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_recruit -> {
                    if(this::class.java.canonicalName == MainActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, RecruListActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_map -> {
                    if (this::class.java.canonicalName == RentActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, RentActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_calendar -> {
                    if (this::class.java.canonicalName == CalActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, CalActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    if (this::class.java.canonicalName == SelfProfActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, SelfProfActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_calendar)?.isChecked = true//하단바 상태 유지

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"

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
                intent.putExtra("calId", clickedCal.cal_id)
                startActivity(intent)
            }
        }
    }
}
