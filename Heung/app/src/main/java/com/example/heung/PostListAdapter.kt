package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.Posts

class PostListAdapter(private val postList: MutableList<Posts>) :
    RecyclerView.Adapter<PostListAdapter.PostViewHolder>() {
    private var onItemClickListener: ((position: Int) -> Unit)? = null // 아이템 클릭 이벤트를 위한 리스너

    // 각 아이템 뷰에 대한 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_postlist, parent, false)
        return PostViewHolder(view)
    }

    // 데이터를 ViewHolder에 바인딩
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        holder.itemView.findViewById<TextView>(R.id.post_title).text = post.post_title
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
        holder.itemView.findViewById<TextView>(R.id.post_date).text = " | ${post.post_date}"
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    // 리스트의 총 아이템 수 반환
    override fun getItemCount(): Int {
        return postList.size
    }

    // 아이템 클릭 리스너 설정
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClickListener = listener
    }

    // 각 아이템을 위한 ViewHolder 클래스
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}