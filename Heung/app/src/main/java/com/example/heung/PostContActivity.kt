package com.example.heung

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import data.Comments
import data.Reply
import java.text.SimpleDateFormat
import java.util.*

class PostContActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var comments: MutableList<Comments>
    private var selectedCommentId: String = ""

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postcont)

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvAuthor = findViewById<TextView>(R.id.tv_author)
        val tvDate = findViewById<TextView>(R.id.tv_date)

        val intent = intent
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        val postAuthor = intent.getStringExtra("postAuthor")
        val postId = intent.getStringExtra("postId")
        val postDate = intent.getStringExtra("postDate")

        tvTitle.text = postTitle
        tvContent.text = postContent
        tvAuthor.text = postAuthor
        tvDate.text = postDate

        comments = mutableListOf()
        adapter = CommentsAdapter(comments, object : CommentsAdapter.OnReplyClickListener {
            override fun onReplyClick(position: Int) {
                val comment = comments[position]
                selectedCommentId = comment.comment_id
                showReplyInputDialog(selectedCommentId)
            }
        })
        recyclerView = findViewById(R.id.content_photo_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        firestore = FirebaseFirestore.getInstance()

        val commentTextview = findViewById<EditText>(R.id.comment_textview)
        val btnSetting = findViewById<Button>(R.id.setting)
        btnSetting.setOnClickListener {
            showSettingPopup()
        }
        val btnCtv = findViewById<Button>(R.id.btn_ctv)

        btnCtv.setOnClickListener {
            val inputCont = commentTextview.text.toString()
            val commentId = UUID.randomUUID().toString()
            val userId = "user_id"
            val commentDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")

            val comment = hashMapOf(
                "comment_id" to commentId,
                "post_id" to postId,
                "user_id" to userId,
                "comment" to inputCont,
                "comment_date" to dateFormat.format(commentDate)
            )

            firestore.collection("Comments")
                .add(comment)
                .addOnSuccessListener {
                    val newComment = Comments(
                        commentId,
                        postId,
                        userId,
                        inputCont,
                        dateFormat.format(commentDate)
                    )
                    comments.add(newComment)
                    adapter.notifyDataSetChanged()
                    commentTextview.text.clear()

                    if (postId != null) {
                        updateCommentsCount(postId)
                    }
                }
                .addOnFailureListener { exception ->
                    // Failed to add comment to Firestore
                }
        }

        firestore.collection("Comments")
            .whereEqualTo("post_id", postId)
            .orderBy("comment_date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                comments.clear()
                for (document in querySnapshot.documents) {
                    val comment = document.toObject(Comments::class.java)
                    comment?.let {
                        comments.add(comment)
                        // 해당 댓글의 대댓글 가져오기
                    }
                }
                adapter.notifyDataSetChanged()

                // 댓글 수 업데이트
                if (postId != null) {
                    updateCommentsCount(postId)
                }
            }
            .addOnFailureListener { exception ->
                // 댓글 가져오기 실패
            }
    }

    private fun updateCommentsCount(postId: String) {
        firestore.collection("Comments")
            .whereEqualTo("post_id", postId)
            .get()
            .addOnSuccessListener { commentsQuerySnapshot ->
                val comments = commentsQuerySnapshot.toObjects(Comments::class.java)
                val commentIds = comments.map { it.comment_id }
                val tvCommentCnt = findViewById<TextView>(R.id.CommentCnt)

                if (commentIds.isNotEmpty()) {
                    firestore.collection("Reply")
                        .whereIn("comment_id", commentIds)
                        .get()
                        .addOnSuccessListener { repliesQuerySnapshot ->
                            val repliesCountMap = mutableMapOf<String, Int>()

                            for (commentId in commentIds) {
                                val repliesCount = repliesQuerySnapshot.documents.count { document ->
                                    document.getString("comment_id") == commentId
                                }
                                repliesCountMap[commentId] = repliesCount
                            }

                            var totalCommentCount = 0
                            var totalReplyCount = 0

                            for (comment in comments) {
                                val replyCount = repliesCountMap[comment.comment_id] ?: 0
                                comment.replyCount = replyCount

                                totalCommentCount++
                                totalReplyCount += replyCount
                            }

                            adapter.notifyDataSetChanged()

                            val totalCount = totalCommentCount + totalReplyCount
                            tvCommentCnt.text = totalCount.toString()
                        }
                } else {
                    for (comment in comments) {
                        comment.replyCount = 0
                    }

                    adapter.notifyDataSetChanged()

                    val totalCount = comments.size
                    tvCommentCnt.text = totalCount.toString()
                }
            }
    }

    @SuppressLint("MissingInflatedId")
    private fun showSettingPopup() {
        val postId = intent.getStringExtra("postId")
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")

        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.item_setting, null)

        val btnEdit = dialogView.findViewById<Button>(R.id.btnEdit)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnEdit.setOnClickListener {
            val intent = Intent(this, PostEditActivity::class.java)
            intent.putExtra("postId",postId)
            intent.putExtra("postTitle", postTitle)
            intent.putExtra("postContent", postContent)
            dialog.dismiss()
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            val collectionName = "Posts"

            val collectionRef = FirebaseFirestore.getInstance().collection(collectionName)
            val query = collectionRef.whereEqualTo("post_id", postId)

            query.get()
                .addOnSuccessListener { querySnapshot ->
                    for (documentSnapshot in querySnapshot.documents) {
                        documentSnapshot.reference.delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "삭제 성공했습니다.", Toast.LENGTH_SHORT).show()
                                // 메인 화면으로 이동
                                val intent = Intent(this, PostListActivity::class.java)
                                startActivity(intent)
                                finish() // 현재 액티비티 종료
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "삭제 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // 쿼리 실행 실패
                }
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }



    private fun showReplyInputDialog(commentId: String) {
        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.item_reply, null)
        val replyEditText = dialogView.findViewById<EditText>(R.id.edit_reply)
        val postButton = dialogView.findViewById<Button>(R.id.btn_reply)

        postButton.setOnClickListener {
            val inputReply = replyEditText.text.toString()
            saveReply(commentId, inputReply)
            dialog.dismiss()
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveReply(commentId: String, inputReply: String) {
        val firestore = FirebaseFirestore.getInstance()
        val replyId = UUID.randomUUID().toString()
        val userId = "user_id"
        val replyDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val postId = intent.getStringExtra("postId")

        val reply = hashMapOf(
            "reply_id" to replyId,
            "comment_id" to commentId,
            "user_id" to userId,
            "reply" to inputReply,
            "reply_date" to dateFormat.format(replyDate)
        )

        firestore.collection("Reply")
            .add(reply)
            .addOnSuccessListener {
                val newReply = Reply(
                    replyId,
                    commentId,
                    userId,
                    inputReply,
                    dateFormat.format(replyDate)
                )
                for (comment in comments) {
                    if (comment.comment_id == commentId) {
                        comment.replies.add(newReply)
                        break
                    }
                }

                adapter.notifyDataSetChanged()
                if (postId != null) {
                    updateCommentsCount(postId)
                }
            }
            .addOnFailureListener { exception ->
                // Firestore에 대댓글 추가 실패
            }
    }
}
