package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.Recruits

class RecruitListAdapter(private val recruitList: MutableList<Recruits>) :
    RecyclerView.Adapter<RecruitListAdapter.RecruitViewHolder>() {
    private var onItemClickListener: ((position: Int) -> Unit)? = null
    // 뷰 홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recruit, parent, false)
        return RecruitViewHolder(view)
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: RecruitViewHolder, position: Int) {
        val recruit = recruitList[position]
        holder.bind(recruit)
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int {
        return recruitList.size
    }

    // 아이템 클릭 리스너 설정
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class RecruitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 아이템에 표시될 뷰 요소들 선언
        private val titleTextView: TextView = itemView.findViewById(R.id.recruit_item_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.recruit_item_date)
        private val typeTextView: TextView = itemView.findViewById(R.id.recruit_item_type)

        // 데이터 바인딩 함수
        fun bind(recruit: Recruits) {
            titleTextView.text = recruit.recruit_title
            dateTextView.text = recruit.recruit_date
            typeTextView.text = recruit.recruit_type
        }
    }
}