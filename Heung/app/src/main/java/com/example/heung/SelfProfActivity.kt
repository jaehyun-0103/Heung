package com.example.heung

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.core.app.ActivityCompat.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.ClientLibraryUtils.getPackageInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.common.util.Utility
import data.Posts
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SelfProfActivity : AppCompatActivity() {
    lateinit var edit_button: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelfProfAdapter
    private val COLLECTION_NAME = "Posts"

    //여기부터 하단바 관련 코드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selfprof)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        // Firebase 앱 초기화
        FirebaseApp.initializeApp(this)

        recyclerView = findViewById(R.id.self_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        initImageViewProfile()
        getPostsList()
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    // 현재 액티비티가 이미 MainActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == MainActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 메인화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_recruit -> {
                    // 현재 액티비티가 이미 RecruListActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 모집 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_map -> {
                    // 현재 액티비티가 이미 RentActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 지도 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_calendar -> {
                    // 현재 액티비티가 이미 CalActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 달력/일정 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    // 현재 액티비티가 이미 SelfProfActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 프로필 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_profile)?.isChecked = true//하단바 상태 유지
    }
// 여기까지 하단바 관련 코드 밑에부턴 기존코드

    private fun getPostsList() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Posts")
            .whereEqualTo("user_id", "jaehyun")
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

        // 닉네임 변경
        val nick_change: Button = findViewById<Button>(R.id.nick_change)
        val nickname: TextView = findViewById<TextView>(R.id.nickname)
        nick_change.setOnClickListener {
            val et = EditText(this)
            et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            et.gravity = Gravity.CENTER
            val builder = AlertDialog.Builder(this)
                .setTitle("변경할 닉네임을 입력하세요.(특수문자 사용불가)")
                .setView(et)
                .setPositiveButton("변경하기",
                    DialogInterface.OnClickListener { dialog, which ->
                        // Toast.makeText(this, et.text, Toast.LENGTH_SHORT).show()
                        val newNickname: String = et.text.toString()
                        nickname.text = newNickname
                    })
            builder.show()
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
                //
            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        // 가져올 컨텐츠들 중에서 Image 만을 가져온다.
        intent.type = "image/*"
        // 갤러리에서 이미지를 선택한 후, 프로필 이미지뷰를 수정하기 위해 갤러리에서 수행한 값을 받아오는 startActivityForeResult를 사용한다.
        startActivityForResult(intent, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리
        if (resultCode != Activity.RESULT_OK)
            return

        when (requestCode) {
            // 2000: 이미지 컨텐츠를 가져오는 액티비티를 수행한 후 실행되는 Activity 일 때만 수행하기 위해서
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
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1000
                )
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    private fun createDummyPosts(): List<Posts> {
        val postsList = mutableListOf<Posts>()
        // 임의의 4개의 게시글 생성
        for (i in 1..4) {
            val title = "게시글 $i"
            val content = "게시글 내용 $i"
            val post = Posts(title, content)
            postsList.add(post)
        }
        return postsList
    }
}
