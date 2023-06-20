package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kakao.sdk.user.UserApiClient
import data.Likes
import data.Posts
import data.Users

class PostListActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var postList: MutableList<Posts>
    private var showPopularPosts = false // 인기 게시글 표시 여부를 나타내는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postlist)

        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        recyclerViewPosts = findViewById(R.id.post_Recycler)
        recyclerViewPosts.layoutManager = LinearLayoutManager(this)

        postList = mutableListOf() // postList 초기화
        postListAdapter = PostListAdapter(postList)
        recyclerViewPosts.adapter = postListAdapter

        firestore = FirebaseFirestore.getInstance() // Firebase 데이터베이스 참조 초기화

        // 게시글 목록 데이터 가져오기
        loadPosts()

        // 게시글 목록 클릭 이벤트 처리
        postListAdapter.setOnItemClickListener { position ->
            val clickedPost = postList[position]

            // 게시글 작성자의 닉네임 가져오기
            firestore.collection("Users")
                .document(clickedPost.user_id)
                .get()
                .addOnSuccessListener { userDocumentSnapshot ->
                    val user = userDocumentSnapshot.toObject(Users::class.java)
                    val nickname = user?.user_nickname ?: "알 수 없는 사용자"

                    // 인텐트 생성 및 데이터 전달
                    val intent = Intent(this, PostContActivity::class.java)
                    intent.putExtra("postId", clickedPost.post_id)
                    intent.putExtra("postTitle", clickedPost.post_title)
                    intent.putExtra("postContent", clickedPost.post_content)
                    intent.putExtra("postDate", clickedPost.post_date)
                    intent.putExtra("postAuthor", nickname)
                    intent.putExtra("userId", clickedPost.user_id)
                    startActivity(intent)
                }
        }

        // 게시글 작성 버튼 클릭 이벤트 처리
        val postCreate = findViewById<Button>(R.id.post_create)
        postCreate.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }

        // 인기 게시글 보기 버튼 클릭 이벤트 처리
        val postPopular = findViewById<Button>(R.id.post_popular)
        postPopular.setOnClickListener {
            showPopularPosts = !showPopularPosts // showPopularPosts 변수 값을 토글
            if (showPopularPosts) {
                // like_id가 5개 이상인 게시글 가져오기
                loadPopularPosts()
            } else {
                // 전체 게시글 가져오기
                loadPosts()
            }
        }
    }

    // 전체 게시글 가져오기
    private fun loadPosts() {
        firestore.collection("Posts")
            .orderBy("post_date", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) { // 에러 처리
                    return@addSnapshotListener
                }

                querySnapshot?.let {
                    val loadedPostList = mutableListOf<Posts>()
                    for (document in it.documents) {
                        val post = document.toObject(Posts::class.java)
                        post?.let {
                            loadedPostList.add(post)
                        }
                    }
                    postList.clear()
                    postList.addAll(loadedPostList)
                    postListAdapter.notifyDataSetChanged()

                    val post = findViewById<TextView>(R.id.post)
                    post.text = "게시글"

                    // 게시글 작성자의 닉네임 가져오기
                    loadPostAuthorsLikesAndComments()

                }
            }
    }

    // 게시글 작성자의 닉네임, 좋아요 수, 댓글 수 가져오기
    private fun loadPostAuthorsLikesAndComments() {
        val postIds = postList.map { it.post_id }
        val batchSize = 10
        val batches = postIds.chunked(batchSize)

        val userQueryPromises = mutableListOf<Task<QuerySnapshot>>()
        val likeQueryPromises = mutableListOf<Task<QuerySnapshot>>()
        val commentQueryPromises = mutableListOf<Task<QuerySnapshot>>()

        // 게시글 작성자의 닉네임 가져오기
        for (batch in batches) {
            val query = firestore.collection("Users")
                .whereIn("user_id", batch)
                .get()
            userQueryPromises.add(query)
        }

        Tasks.whenAllComplete(userQueryPromises)
            .addOnSuccessListener { userTasks ->
                val users = mutableListOf<Users>()
                val userMap = mutableMapOf<String, Users>()

                for (task in userTasks) {
                    if (task.isSuccessful) {
                        val snapshot = task.result as QuerySnapshot
                        val batchUsers = snapshot.toObjects(Users::class.java)
                        users.addAll(batchUsers)

                        for (user in batchUsers) {
                            userMap[user.user_id] = user
                        }
                    }
                }

                // 게시글 목록의 닉네임 업데이트
                for (i in 0 until postListAdapter.itemCount) {
                    val post = postList[i]
                    val user = userMap[post.user_id]
                    val holder = recyclerViewPosts.findViewHolderForAdapterPosition(i) as? PostListAdapter.PostViewHolder
                    val nicknameTextView = holder?.itemView?.findViewById<TextView>(R.id.post_nickname)
                    if (user != null) {
                        nicknameTextView?.text = user.user_nickname
                    } else {
                        firestore.collection("Users")
                            .document(post.user_id)
                            .get()
                            .addOnSuccessListener { userDocumentSnapshot ->
                                val user = userDocumentSnapshot.toObject(Users::class.java)
                                val nickname = user?.user_nickname ?: "알 수 없는 사용자"
                                nicknameTextView?.text = nickname
                            }
                    }
                }

                // 게시글의 좋아요 수 가져오기
                for (batch in batches) {
                    val query = firestore.collection("Likes")
                        .whereIn("post_id", batch)
                        .get()
                    likeQueryPromises.add(query)
                }

                Tasks.whenAllComplete(likeQueryPromises)
                    .addOnSuccessListener { likeTasks ->
                        val likes = mutableListOf<Likes>()
                        val likeMap = mutableMapOf<String, Likes>()

                        for (task in likeTasks) {
                            if (task.isSuccessful) {
                                val snapshot = task.result as QuerySnapshot
                                val batchLikes = snapshot.toObjects(Likes::class.java)
                                likes.addAll(batchLikes)

                                for (like in batchLikes) {
                                    likeMap[like.post_id] = like
                                }
                            }
                        }

                        // 게시글 목록의 좋아요 수 업데이트
                        for (i in 0 until postListAdapter.itemCount) {
                            val post = postList[i]
                            val like = likeMap[post.post_id]
                            val likeCount = like?.like ?: 0 // 해당 게시글에 대한 좋아요 수
                            val holder =
                                recyclerViewPosts.findViewHolderForAdapterPosition(i) as? PostListAdapter.PostViewHolder
                            holder?.itemView?.findViewById<TextView>(R.id.post_likes)?.text =
                                likeCount.toString()
                        }

                        // 게시글의 댓글 수와 대댓글 수 가져오기
                        for (batch in batches) {
                            val query = firestore.collection("Comments")
                                .whereIn("post_id", batch)
                                .get()
                            commentQueryPromises.add(query)
                        }

                        Tasks.whenAllComplete(commentQueryPromises)
                            .addOnSuccessListener { commentTasks ->
                                val commentsCountMap = mutableMapOf<String, Int>()

                                // 각 게시글의 댓글 수 계산
                                for (task in commentTasks) {
                                    if (task.isSuccessful) {
                                        val snapshot = task.result as QuerySnapshot
                                        for (document in snapshot.documents) {
                                            val postId = document.getString("post_id")
                                            if (postId != null) {
                                                if (commentsCountMap.containsKey(postId)) {
                                                    val count = commentsCountMap[postId] ?: 0
                                                    commentsCountMap[postId] = count + 1
                                                } else {
                                                    commentsCountMap[postId] = 1
                                                }
                                            }
                                        }
                                    }
                                }

                                // 각 게시글의 대댓글 수 계산 및 업데이트
                                for (i in 0 until postListAdapter.itemCount) {
                                    val post = postList[i]
                                    val commentsCount =
                                        commentsCountMap[post.post_id] ?: 0 // 해당 게시글에 대한 댓글 수

                                    firestore.collection("Comments")
                                        .whereEqualTo("post_id", post.post_id)
                                        .get()
                                        .addOnSuccessListener { commentsQuerySnapshot ->
                                            val repliesCountMap = mutableMapOf<String, Int>()
                                            var totalCount = commentsCount

                                            for (commentDocument in commentsQuerySnapshot.documents) {
                                                val commentId =
                                                    commentDocument.getString("comment_id")
                                                if (commentId != null) {
                                                    firestore.collection("Reply")
                                                        .whereEqualTo("comment_id", commentId)
                                                        .get()
                                                        .addOnSuccessListener { replyQuerySnapshot ->
                                                            val replyCount =
                                                                replyQuerySnapshot.size()
                                                            repliesCountMap[commentId] = replyCount

                                                            totalCount += replyCount

                                                            val holder =
                                                                recyclerViewPosts.findViewHolderForAdapterPosition(
                                                                    i
                                                                ) as? PostListAdapter.PostViewHolder
                                                            holder?.itemView?.findViewById<TextView>(
                                                                R.id.post_comment
                                                            )?.text = totalCount.toString()
                                                        }
                                                }
                                            }

                                            // 댓글이 하나도 없는 경우 0으로 표시
                                            if (commentsQuerySnapshot.isEmpty) {
                                                val holder =
                                                    recyclerViewPosts.findViewHolderForAdapterPosition(
                                                        i
                                                    ) as? PostListAdapter.PostViewHolder
                                                holder?.itemView?.findViewById<TextView>(R.id.post_comment)?.text =
                                                    "0"
                                            }
                                        }
                                }
                            }
                    }
            }
    }

    // 인기 게시글 가져오기
    private fun loadPopularPosts() {
        firestore.collection("Likes")
            .addSnapshotListener { likesSnapshot, likesException ->
                if (likesException != null) {
                    return@addSnapshotListener
                }

                val postIdList = mutableListOf<String>()
                likesSnapshot?.let {
                    postIdList.clear()
                    var count = 0
                    for (document in it.documents) {
                        if (count >= 10) {
                            break
                        }
                        val postId = document.getString("post_id")
                        val like = document.getString("like")?.toIntOrNull() ?: 0 // Parse like value as integer
                        if (like > 5) {
                            postId?.let {
                                postIdList.add(postId)
                                count++
                            }
                        }
                    }
                }

                if (postIdList.isNotEmpty()) {
                    firestore.collection("Posts")
                        .whereIn("post_id", postIdList)
                        .orderBy("post_date", Query.Direction.DESCENDING)
                        .addSnapshotListener { postsSnapshot, postsException ->
                            if (postsException != null) { // Error handling
                                return@addSnapshotListener
                            }

                            postsSnapshot?.let {
                                postList.clear()
                                for (document in it.documents) {
                                    val post = document.toObject(Posts::class.java)
                                    post?.let {
                                        postList.add(post)
                                    }
                                }
                                postListAdapter.notifyDataSetChanged()

                                val post = findViewById<TextView>(R.id.post)
                                post.text = "인기 게시글"

                                // 게시글 작성자의 닉네임 가져오기
                                loadPostAuthorsLikesAndComments()
                            }
                        }
                } else {
                    // 인기 게시글이 없을 경우 처리할 내용
                    val post = findViewById<TextView>(R.id.post)
                    post.text = "인기 게시글이 없습니다."
                    postList.clear()
                    postListAdapter.notifyDataSetChanged()
                }
            }
    }
}
