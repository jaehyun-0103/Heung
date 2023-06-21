package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import data.Recruits
import data.Users


class RecruitListAdapter(private val recruitList: MutableList<Recruits>) :
    RecyclerView.Adapter<RecruitListAdapter.RecruitViewHolder>() {
    private lateinit var firestore: FirebaseFirestore
    // 아이템 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(recruit: Recruits)
    }

    // 아이템 클릭 리스너 프로퍼티 선언
    private var itemClickListener: OnItemClickListener? = null

    // 아이템 클릭 리스너 설정 함수
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

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

    inner class RecruitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.recruit_item_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.recruit_item_date)
        private val typeTextView: TextView = itemView.findViewById(R.id.recruit_item_type)
        private val nicknameTextView: TextView = itemView.findViewById(R.id.recruit_item_nickname)

        init {
            // 아이템 클릭 이벤트 처리
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(recruitList[position])
                }
            }
        }

        // 데이터 바인딩 함수
        fun bind(recruit: Recruits) {
            titleTextView.text = recruit.recruit_title
            dateTextView.text = recruit.recruit_date
            typeTextView.text = recruit.recruit_type

            firestore = FirebaseFirestore.getInstance()
            val usersCollection = firestore.collection("Users")
            usersCollection
                .whereEqualTo("user_id", recruit.user_id)
                .get()
                .addOnSuccessListener { userQuerySnapshot ->
                    if (!userQuerySnapshot.isEmpty) {
                        val userDocumentSnapshot = userQuerySnapshot.documents[0]
                        val user = userDocumentSnapshot.toObject(Users::class.java)
                        if (user != null) {
                            nicknameTextView.text = user.user_nickname
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // 실패 처리
                }
        }
    }
}