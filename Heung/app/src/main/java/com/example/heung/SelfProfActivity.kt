package com.example.heung

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import data.Posts
import data.Users


class SelfProfActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var postListAdapter: SelfProfAdapter
    private lateinit var postsList: MutableList<Posts>

    private lateinit var edit_button: ImageView
    private var currentNickname: String = ""
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selfprof)


        val nickname: TextView = findViewById<TextView>(R.id.nickname)
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        FirebaseApp.initializeApp(this)

        postsList = mutableListOf() // postList 초기화
        firestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.self_recycler)
        postListAdapter = SelfProfAdapter(postsList)
        recyclerView.adapter = postListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        postListAdapter.setOnItemClickListener { position ->
            val clickedPost = postsList[position]

            // 게시글 작성자의 닉네임 가져오기
            firestore.collection("Users")
                .document(clickedPost.user_id)
                .get()
                .addOnSuccessListener { userDocumentSnapshot ->
                    val user = userDocumentSnapshot.toObject(Users::class.java)
                    val nickname = user?.user_nickname ?: "닉네임"

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


        loadUserPosts()

        initBottomNavigation()

        val settingButton: ImageButton = findViewById(R.id.setting)
        settingButton.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        //닉네임 userId식별자로 넣기(기본값)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                userId = user.id.toString()

                val db = FirebaseFirestore.getInstance()
                val usersCollection = db.collection("Users")
                usersCollection
                    .whereEqualTo("user_id", userId)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val documentSnapshot = querySnapshot.documents[0]
                            val user = documentSnapshot.toObject(Users::class.java)
                            currentNickname = user?.user_nickname ?: ""
                            nickname.text = currentNickname
                        }
                    }
            }
        }

        // 닉네임 변경 버튼 클릭 이벤트 처리
        val nickChangeButton: Button = findViewById(R.id.nick_change)
        nickChangeButton.setOnClickListener {
            val et = EditText(this)
            et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            et.gravity = Gravity.CENTER

            val builder = AlertDialog.Builder(this)
                .setTitle("변경할 닉네임을 입력하세요.(특수문자 사용불가)")
                .setView(et)
                .setPositiveButton("변경하기") { dialog, which ->
                    val newNickname: String = et.text.toString()

                    // Firebase Firestore 업데이트
                    val db = FirebaseFirestore.getInstance()
                    val usersCollection = db.collection("Users")

                    // 사용자 정보 가져오기
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            // 사용자 정보 가져오기 실패
                        } else if (user != null) {
                            val userId = user.id.toString()
                            // 중복 여부 확인
                            usersCollection
                                .whereEqualTo("user_nickname", newNickname)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    val isNicknameDuplicate = !querySnapshot.isEmpty

                                    if (isNicknameDuplicate) {
                                        // 중복된 닉네임이 존재하는 경우
                                        Toast.makeText(this, "중복된 닉네임입니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        // 중복된 닉네임이 존재하지 않는 경우 Users 컬렉션에서 해당 user_id의 문서를 찾아 닉네임 업데이트
                                        usersCollection
                                            .whereEqualTo("user_id", userId)
                                            .get()
                                            .addOnSuccessListener { querySnapshot ->
                                                if (!querySnapshot.isEmpty) {
                                                    val documentSnapshot =
                                                        querySnapshot.documents[0]
                                                    val documentId = documentSnapshot.id
                                                    val updates = hashMapOf<String, Any>(
                                                        "user_nickname" to newNickname
                                                    )
                                                    // 문서 업데이트
                                                    usersCollection.document(documentId)
                                                        .update(updates)
                                                        .addOnSuccessListener {
                                                            nickname.text = newNickname
                                                        }
                                                        .addOnFailureListener { e ->
                                                            // 업데이트 실패
                                                        }
                                                } else {
                                                    // 해당 user_id의 문서를 찾을 수 없는 경우
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                // 데이터 가져오기 실패
                                            }
                                    }
                                }
                        }
                    }
                }
            builder.show()
        }
    }

    private fun initBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    if (javaClass.name == MainActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_recruit -> {
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_map -> {
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_calendar -> {
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_profile)?.isChecked = true
    }


    private fun loadUserPosts() {
        val firestore = FirebaseFirestore.getInstance()
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                userId = user.id.toString()
                firestore.collection("Posts")
                    .whereEqualTo("user_id", userId) // user_id 필드와 userId 값이 일치하는 게시글만 가져오도록 쿼리 수정
                    .orderBy("post_date", Query.Direction.DESCENDING)
                    .addSnapshotListener { querySnapshot, exception ->
                        if (exception != null) { // 에러 처리
                            return@addSnapshotListener
                        }
                        querySnapshot?.let {
                            postsList.clear()
                            for (document in it.documents) {
                                val post = document.toObject(Posts::class.java)
                                post?.let {
                                    postsList.add(post)
                                }
                            }
                            postListAdapter.notifyDataSetChanged()
                        }
                    }
            }
        }

    }

}