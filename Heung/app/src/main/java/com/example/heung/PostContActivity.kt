package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postcont)

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvAuthor = findViewById<TextView>(R.id.tv_author)
        val tvDate = findViewById<TextView>(R.id.tv_date)
        val commentTextview = findViewById<EditText>(R.id.comment_textview)

        val intent = intent
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")
        val postAuthor = intent.getStringExtra("postAuthor")
        val postId = intent.getStringExtra("postId")
        val postDate = intent.getStringExtra("postDate")
        val userId = intent.getStringExtra("userId")

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

        val btnBack = findViewById<ImageButton>(R.id.dirogagi)
        btnBack.setOnClickListener {
            finish()
        }

        val btnSetting = findViewById<ImageButton>(R.id.setting)
        btnSetting.visibility = View.GONE
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                val currentUserId = user.id.toString() // 사용자 ID
                if (userId == currentUserId) {
                    btnSetting.visibility = View.VISIBLE
                    btnSetting.setOnClickListener {
                        showSettingPopup()
                    }
                }
            }

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    // 프로필 정보 가져오기 실패
                } else if (user != null) {
                    // 프로필 정보 가져오기 성공
                    val userId = user.id.toString()

                    // Users 컬렉션에서 해당 user_id의 사용자 닉네임 가져오기
                    val usersCollection = firestore.collection("Users")
                    val userQuery = usersCollection.whereEqualTo("user_id", userId)

                    userQuery.get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                val userDocument = querySnapshot.documents[0]
                                val btnCtv = findViewById<Button>(R.id.btn_ctv)
                                btnCtv.setOnClickListener {
                                    val inputCont = commentTextview.text.toString()
                                    val commentId = UUID.randomUUID().toString()
                                    val commentDate = Date()
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    val comment = hashMapOf(
                                        "comment_id" to commentId,
                                        "post_id" to postId,
                                        "user_id" to userId, // user_id 필드를 사용하여 댓글 작성자를 식별
                                        "comment" to inputCont,
                                        "comment_date" to dateFormat.format(commentDate)
                                    )

                                    firestore.collection("Comments")
                                        .add(comment)
                                        .addOnSuccessListener { documentReference ->
                                            val newComment = Comments(
                                                commentId,
                                                postId,
                                                userId, // user_id를 사용하여 댓글 작성자를 식별
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
                        .addOnFailureListener { exception ->
                            // 실패 처리
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

                        tvLikeCount.text = currentLikeCount.toString()
                    } else { // Likes 컬렉션에 해당 postId에 대한 문서가 없는 경우
                        tvLikeCount.text = "0"
                    }
                }
                .addOnFailureListener { exception ->
                    // 게시글 가져오기 실패
                }

            btnLike.setOnClickListener {
                // 현재 사용자의 ID
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        // 프로필 정보 가져오기 실패
                    } else if (user != null) {
                        val userId = user.id.toString() // 사용자 ID
                        val likesRef = firestore.collection("Likes").document(postId)
                        likesRef.get()
                            .addOnSuccessListener { documentSnapshot ->
                                val likes = documentSnapshot.toObject(Likes::class.java)

                                if (likes != null) { // 좋아요가 이미 존재
                                    val likedUserIds = likes.userIds
                                    if (likedUserIds.contains(userId)) { // 현재 사용자가 이미 좋아요를 누른 경우
                                        btnLike.isEnabled = false // 좋아요 버튼 비활성화
                                        Toast.makeText(this, "이미 좋아요를 눌렀습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                } else {
                                    // 좋아요 추가
                                    val newLikeCount = tvLikeCount.text.toString().toInt() + 1
                                    // 좋아요 정보 업데이트
                                    val likeData = hashMapOf(
                                        "post_id" to postId,
                                        "like" to newLikeCount.toString(),
                                        "userIds" to listOf(userId)
                                    )
                                    likesRef
                                        .set(likeData)
                                        .addOnSuccessListener {
                                            tvLikeCount.text = newLikeCount.toString()

                                            // 좋아요 버튼 비활성화
                                            btnLike.isEnabled = false
                                            Toast.makeText(this, "좋아요를 눌렀습니다.", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        .addOnFailureListener { exception ->
                                            // 좋아요 수 업데이트 실패
                                        }
                                }
                            }
                    }
                }
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
                                val repliesCount =
                                    repliesQuerySnapshot.documents.count { document ->
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

    private fun showSettingPopup() {
        val postId = intent.getStringExtra("postId")
        val postTitle = intent.getStringExtra("postTitle")
        val postContent = intent.getStringExtra("postContent")

        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.item_setting, null)

        val btnEdit = dialogView.findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            val intent = Intent(this, PostEditActivity::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("postTitle", postTitle)
            intent.putExtra("postContent", postContent)
            dialog.dismiss()
            startActivity(intent)
        }

        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
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

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
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

    private fun saveReply(commentId: String, inputReply: String) {
        val firestore = FirebaseFirestore.getInstance()
        val replyId = UUID.randomUUID().toString()
        val postId = intent.getStringExtra("postId")

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                val userId = user.id.toString()

                // Users 컬렉션에서 해당 user_id의 사용자 닉네임 가져오기
                val usersCollection = firestore.collection("Users")
                val userQuery = usersCollection.whereEqualTo("user_id", userId)

                userQuery.get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userDocument = querySnapshot.documents[0]
                            val nickname = userDocument.getString("user_nickname")
                            val replyDate = Date()
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

                            val reply = hashMapOf(
                                "reply_id" to replyId,
                                "comment_id" to commentId,
                                "user_id" to userId,
                                "reply" to inputReply,
                                "reply_date" to dateFormat.format(replyDate)
                            )

                            firestore.collection("Reply")
                                .add(reply)
                                .addOnSuccessListener { documentReference ->
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
            }
        }
    }
}