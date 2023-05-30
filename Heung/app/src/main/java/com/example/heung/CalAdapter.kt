package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class CalAdapter(private val calendar: MutableList<Calendar>) :
    RecyclerView.Adapter<CalAdapter.CalViewHolder>() {
    private var onItemClickListener: ((position: Int) -> Unit)? = null // 아이템 클릭 이벤트를 위한 리스너

    // 각 아이템 뷰에 대한 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalAdapter.CalViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_callist, parent, false)
        return CalAdapter.CalViewHolder(view)
    }

    // 데이터를 ViewHolder에 바인딩
    override fun onBindViewHolder(holder: CalAdapter.CalViewHolder, position: Int) {
        val calendars = calendar[position]
        holder.itemView.findViewById<TextView>(R.id.performName).text = calendars.cal_title

        val startDate = convertToFormattedDate(calendars.cal_startDate)
        val endDate = convertToFormattedDate(calendars.cal_endDate)

        holder.itemView.findViewById<TextView>(R.id.performStart).text = startDate
        holder.itemView.findViewById<TextView>(R.id.performEnd).text = endDate

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    // 리스트의 총 아이템 수 반환
    override fun getItemCount(): Int {
        return calendar.size
    }

    // 아이템 클릭 리스너 설정
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClickListener = listener
    }

    // 각 아이템을 위한 ViewHolder 클래스
    class CalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    // 날짜 형식을 변환하는 함수
    private fun convertToFormattedDate(date: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = date

        val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }

}