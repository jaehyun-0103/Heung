package com.example.heung

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import data.Comments
import data.Reply

class CommentsAdapter(private val postList: MutableList<Comments>
                      ,private val onReplyClickListener: OnReplyClickListener
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {
    private var onItemClickListener: ((position: Int) -> Unit)? = null // 아이템 클릭 이벤트를 위한 리스너

    // 각 아이템 뷰에 대한 ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_postcont, parent, false)
        return CommentViewHolder(view)
    }

    // 데이터를 ViewHolder에 바인딩
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comments = postList[position]
        holder.itemView.findViewById<TextView>(R.id.tv_comment).text = comments.comment
        holder.itemView.findViewById<TextView>(R.id.tv_date).text = comments.comment_date
        holder.itemView.findViewById<TextView>(R.id.tv_author).text = "닉네임"
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
        holder.btnReply.setOnClickListener {
            onReplyClickListener.onReplyClick(position)
        }

        val rvReplies = holder.itemView.findViewById<RecyclerView>(R.id.reply_recycler)
        val replyAdapter = ReplyAdapter(comments.replies)
        rvReplies.layoutManager = LinearLayoutManager(holder.itemView.context)
        rvReplies.adapter = replyAdapter

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("Reply")
            .whereEqualTo("comment_id", comments.comment_id)
            .orderBy("reply_date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { replySnapshot ->
                val replyList = mutableListOf<Reply>()
                for (replyDocument in replySnapshot.documents) {
                    val replyId = replyDocument.getString("reply_id")
                    val commentId = replyDocument.getString("comment_id")
                    val userId = replyDocument.getString("user_id")
                    val reply = replyDocument.getString("reply")
                    val replyDate = replyDocument.getString("reply_date")
                    val replyObject = Reply(
                        replyId,
                        commentId,
                        userId,
                        reply,
                        replyDate
                    )
                    replyList.add(replyObject)
                }
                comments.replies.clear()
                comments.replies.addAll(replyList)
                replyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // 대댓글 가져오기 실패
            }
    }

    // 리스트의 총 아이템 수 반환
    override fun getItemCount(): Int {
        return postList.size
    }

    // 각 아이템을 위한 ViewHolder 클래스
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnReply: Button = itemView.findViewById(R.id.btnReply)
        init {
            btnReply.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onReplyClickListener.onReplyClick(position)
                }
            }
        }
    }
    interface OnReplyClickListener {
        fun onReplyClick(position: Int)
    }
}