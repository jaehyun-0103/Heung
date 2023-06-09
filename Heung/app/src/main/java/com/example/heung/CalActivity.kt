package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import data.Calendar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalAdapter
    private lateinit var calendar: MutableList<Calendar>
    private lateinit var calendarView: MaterialCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal)

        recyclerView = findViewById(R.id.cal_data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        calendar = mutableListOf()
        adapter = CalAdapter(calendar)
        recyclerView.adapter = adapter
        firestore = FirebaseFirestore.getInstance()

        calendarView = findViewById(R.id.calendarView)

        // 하단바 아이템 선택 이벤트 처리
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    if (this::class.java.canonicalName == MainActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.nav_recruit -> {
                    if (this::class.java.canonicalName == MainActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, RecruListActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.nav_map -> {
                    if (this::class.java.canonicalName == RentActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, RentActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.nav_calendar -> {
                    if (this::class.java.canonicalName == CalActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, CalActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    if (this::class.java.canonicalName == SelfProfActivity::class.java.canonicalName) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    startActivity(Intent(this, SelfProfActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_calendar)?.isChecked = true

        calendarView.setSelectedDate(CalendarDay.today());
        // 달력에 점으로 날짜 표시
        decorateCalendar()

        val calWriteBtn = findViewById<ImageButton>(R.id.calwriteBtn)
        calendarView.setOnDateChangedListener { widget, date, selected ->
            val year = date.year
            val month = date.month + 1
            val dayOfMonth = date.day
            val selectedDate = String.format("%04d-%02d-%02d", year, month, dayOfMonth)

            calWriteBtn.setOnClickListener {
                val intent = Intent(this, CalWriteActivity::class.java)
                intent.putExtra("selectedDate", selectedDate)
                startActivity(intent)
                overridePendingTransition(R.transition.slide_up, R.transition.fade_out)
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
                overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left)
            }
        }
    }

    private fun decorateCalendar() {
        firestore.collection("Calendar")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val datesWithEvents = ArrayList<CalendarDay>()
                for (document in querySnapshot.documents) {
                    val calDate = document.getString("cal_date")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = dateFormat.parse(calDate)
                    val calendarDay = CalendarDay.from(date)
                    datesWithEvents.add(calendarDay)
                }
                val decorator = EventDecorator(datesWithEvents)
                calendarView.addDecorator(decorator)
            }
            .addOnFailureListener { exception ->
                // 실패 처리
            }
    }

    inner class EventDecorator(private val dates: List<CalendarDay>) : DayViewDecorator {
        private val color = resources.getColor(R.color.teal_200)
        private val radius = 5f

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(radius, color))
        }
    }
}