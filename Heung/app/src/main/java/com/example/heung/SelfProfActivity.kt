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
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import data.Posts
import data.Users


class SelfProfActivity : AppCompatActivity() {
    lateinit var edit_button: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelfProfAdapter
    private var currentNickname: String = ""
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selfprof)

        val nickname: TextView = findViewById<TextView>(R.id.nickname)
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        // Firebase 앱 초기화
        FirebaseApp.initializeApp(this)

        recyclerView = findViewById(R.id.self_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        initImageViewProfile()

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                this.userId = user.id.toString()
                getPostsList()
            }
        }

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
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_recruit -> {
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_map -> {
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_calendar -> {
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_profile)?.isChecked = true
    }

    private fun getPostsList() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Posts")
            .whereEqualTo("user_id", userId)
            .get()
            .addOnSuccessListener { documents ->
                val postsList = mutableListOf<Posts>()

                for (document in documents) {
                    val postId = document.id
                    val userId = document.getString("user_id") ?: ""
                    val title = document.getString("post_title") ?: ""
                    val content = document.getString("post_content") ?: ""
                    val date = document.getString("post_date") ?: ""
                    val post = Posts(postId, userId, title, content, date)
                    postsList.add(post)
                }

                adapter = SelfProfAdapter(postsList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // 오류 처리
            }
    }

    private fun initImageViewProfile() {
        edit_button = findViewById(R.id.btn_edit)
        edit_button.setOnClickListener {
            when {
                // 갤러리 접근 권한이 있는 경우
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                -> {
                    navigateGallery()
                }

                // 갤러리 접근 권한이 없는 경우 & 교육용 팝업을 보여줘야 하는 경우
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    showPermissionContextPopup()
                }

                // 권한 요청 하기(requestPermissions) -> 갤러리 접근(onRequestPermissionResult)
                else -> requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }

        }
    }

    // 권한 요청 승인 이후 실행되는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    navigateGallery()
                else
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {

            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 컨텐츠들 중에서 Image 만을 가져옴
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위함
            2000 -> {
                val selectedImageUri: Uri? = data?.data
                if (selectedImageUri != null) {
                    edit_button.setImageURI(selectedImageUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("프로필 이미지를 바꾸기 위해서는 갤러리 접근 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }
}