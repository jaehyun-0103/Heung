package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FirebaseFirestore
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

            // 인텐트 생성 및 데이터 전달
            val intent = Intent(this, PostContActivity::class.java)
            intent.putExtra("postId", clickedPost.post_id)
            // RecyclerView의 클릭 이벤트 리스너 내에서
            intent.putExtra("postTitle", clickedPost.post_title)
            intent.putExtra("postContent", clickedPost.post_content)
            intent.putExtra("postDate",clickedPost.post_date) // 이거 추가함 기억하자
            intent.putExtra("postAuthor", clickedPost.user_id) // 얘도 추가함, 원래 nickname가져와야함
            startActivity(intent)
        }

        // 게시글 작성 버튼 클릭 이벤트 처리
        val postCreate = findViewById<Button>(R.id.post_create)
        postCreate.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            startActivity(intent)
        }

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val userId = user.id.toString()
            }
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

        // 게시글 작성자의 닉네임 가져오기
        firestore.collection("Users")
            .whereIn("user_id", postList.map { it.user_id })
            .get()
            .addOnSuccessListener { userQuerySnapshot ->
                val users = userQuerySnapshot.toObjects(Users::class.java)
                val userMap = users.associateBy { it.user_id }

                // 게시글 목록의 닉네임 및 좋아요 수 업데이트
                for (i in 0 until postListAdapter.itemCount) {
                    val post = postList[i]
                    val user = userMap[post.user_id]
                    val nickname = user?.user_nickname ?: "알 수 없는 사용자"
                    val holder =
                        recyclerViewPosts.findViewHolderForAdapterPosition(i) as? PostListAdapter.PostViewHolder
                    holder?.itemView?.findViewById<TextView>(R.id.post_nickname)?.text = nickname
                }
            }

        // 게시글의 좋아요 수 가져오기
        firestore.collection("Likes")
            .whereIn("post_id", postIds)
            .get()
            .addOnSuccessListener { likesQuerySnapshot ->
                val likes = likesQuerySnapshot.toObjects(Likes::class.java)
                val likeMap = likes.associateBy { it.post_id }

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
            }

        // 게시글의 댓글 수 가져오기
        firestore.collection("Comments")
            .whereIn("post_id", postIds)
            .get()
            .addOnSuccessListener { commentsQuerySnapshot ->
                val commentsCountMap = mutableMapOf<String, Int>()

                // 각 게시물의 댓글 수 계산
                for (document in commentsQuerySnapshot.documents) {
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

                // 게시글 목록의 댓글 수 업데이트
                for (i in 0 until postListAdapter.itemCount) {
                    val post = postList[i]
                    val commentsCount = commentsCountMap[post.post_id] ?: 0 // 해당 게시글에 대한 댓글 수

                    val holder =
                        recyclerViewPosts.findViewHolderForAdapterPosition(i) as? PostListAdapter.PostViewHolder
                    holder?.itemView?.findViewById<TextView>(R.id.post_comment)?.text =
                        commentsCount.toString()
                }
            }
    }

    // 인기 게시글 가져오기
    private fun loadPopularPosts() {
        firestore.collection("Likes")
            .orderBy("like")
            .startAt(5)
            .addSnapshotListener { likesSnapshot, likesException ->
                if (likesException != null) {
                    return@addSnapshotListener
                }

                val likedUserIds = mutableListOf<String>()
                likesSnapshot?.let {
                    likedUserIds.clear()
                    for (document in it.documents) {
                        val likedUserId = document.getString("user_id")
                        likedUserId?.let {
                            likedUserIds.add(likedUserId)
                        }
                    }
                }

                firestore.collection("Posts")
                    .whereIn("user_id", likedUserIds)
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
            }
    }
}
