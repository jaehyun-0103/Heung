package com.example.heung

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.sdk.user.UserApiClient
import data.Comments
import data.Likes
import data.Reply
import java.text.SimpleDateFormat
import java.util.*

class PostContActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var comments: MutableList<Comments>
    private var selectedCommentId: String = ""
    private lateinit var btnLike: ImageView
    private lateinit var tvLikeCount: TextView

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
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val userId = user.id.toString()

                val btnCtv = findViewById<Button>(R.id.btn_ctv)
                btnCtv.setOnClickListener {
                    val inputCont = commentTextview.text.toString()
                    val commentId = UUID.randomUUID().toString()
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
                            // 실패 처리
                        }
                }
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

        btnLike = findViewById(R.id.like_btn)
        tvLikeCount = findViewById(R.id.likeCnt)

        // 해당 게시글의 좋아요 수 가져오기
        firestore.collection("Likes")
            .document(postId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val likes = documentSnapshot.toObject(Likes::class.java)
                if (likes != null) {
                    val currentLikeCountStr = likes.like ?: "0"
                    val currentLikeCount = currentLikeCountStr.toInt()

                    // 좋아요 수 텍스트뷰 업데이트
                    tvLikeCount.text = currentLikeCount.toString()
                } else {
                    // Likes 컬렉션에 해당 postId에 대한 문서가 없는 경우
                    // 좋아요 수 텍스트뷰 업데이트
                    tvLikeCount.text = "0"
                }
            }
            .addOnFailureListener { exception ->
                // 게시글 가져오기 실패
            }

        btnLike.setOnClickListener {
            // 좋아요 버튼이 클릭되었을 때 실행될 코드

            // 해당 게시글의 좋아요 여부를 가져옴
            val likesRef = firestore.collection("Likes").document(postId)
            likesRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val likes = documentSnapshot.toObject(Likes::class.java)
                    if (likes != null) {
                        // 좋아요가 이미 존재하는 경우
                        btnLike.isEnabled = false // 좋아요 버튼 비활성화
                        Toast.makeText(this, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()

                    } else {
                        // 좋아요가 존재하지 않는 경우
                        val newLikeCount = tvLikeCount.text.toString().toInt() + 1

                        // 게시글의 좋아요 수 업데이트
                        val likeData = hashMapOf(
                            "post_id" to postId,
                            "like" to newLikeCount.toString()
                        )

                        firestore.collection("Likes")
                            .document(postId)
                            .set(likeData)
                            .addOnSuccessListener {
                                // 좋아요 수 업데이트 성공
                                tvLikeCount.text = newLikeCount.toString()

                                // 좋아요 버튼 비활성화
                                btnLike.isEnabled = false
                                Toast.makeText(this, "좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                // 좋아요 수 업데이트 실패
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // 좋아요 여부 확인 실패
                }
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