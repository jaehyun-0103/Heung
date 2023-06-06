package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.Reply

class ReplyAdapter(private val replyList: MutableList<Reply>) :
    RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    // 각 아이템 뷰에 대한 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyAdapter.ReplyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_postcomment, parent, false)
        return ReplyViewHolder(view)
    }

    // 데이터를 ViewHolder에 바인딩
    override fun onBindViewHolder(holder: ReplyAdapter.ReplyViewHolder, position: Int) {
        val reply = replyList[position]
        holder.itemView.findViewById<TextView>(R.id.tv_comment).text = reply.reply
        holder.itemView.findViewById<TextView>(R.id.tv_date).text = reply.reply_date
        holder.itemView.findViewById<TextView>(R.id.tv_author).text = reply.user_nickname
    }

    // 리스트의 총 아이템 수 반환
    override fun getItemCount(): Int {
        return replyList.size
    }

    // 각 아이템을 위한 ViewHolder 클래스
    inner class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
