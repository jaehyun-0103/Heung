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
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import data.Posts
import data.Users
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SelfProfActivity : AppCompatActivity() {
    lateinit var edit_button: ImageView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelfProfAdapter
    private val COLLECTION_NAME = "Posts"
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



        getPostsList()


        // 로그아웃 버튼 클릭 시

        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            // 카카오 로그아웃 요청
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    // 로그아웃 실패
                    // 오류 처리 코드를 추가하세요.
                } else {
                    // 로그아웃 성공
                    // LoginActivity로 돌아가는 인텐트 생성
                    val intent = Intent(this@SelfProfActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }

        }
        // 탈퇴 버튼 클릭 이벤트 처리
        val dropoutButton: Button = findViewById(R.id.btn_quit)
        dropoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setTitle("탈퇴하기")
                .setMessage("정말 탈퇴하시겠습니까?")
                .setPositiveButton("네") { dialog, which ->
                    // 탈퇴 처리 로직
                    // 탈퇴를 수행하는 코드를 여기에 작성하세요.
                    // 예를 들어, 사용자 데이터 삭제 등을 수행합니다.

                    // 사용자 데이터 삭제 코드
                    // 데이터베이스에서 사용자 정보를 삭제하는 등의 작업을 수행합니다.
                    // 삭제가 성공적으로 이루어졌을 경우, 로그아웃을 수행합니다.

                    // 사용자 데이터 삭제 후 로그아웃
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            // 탈퇴 실패 처리
                        } else {
                            // 탈퇴 성공 및 로그아웃
                            UserApiClient.instance.logout { logoutError ->
                                if (logoutError != null) {
                                    // 로그아웃 실패 처리
                                } else {
                                    // 로그아웃 성공
                                    // 추가적인 작업을 수행하세요.
                                }
                            }
                        }
                    }
                }
                .setNegativeButton("아니요", null)

            builder.show()
        }

        //닉네임 userId식별자로 넣기(기본값)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
                // 에러 처리 로직 추가
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val userId = user.id.toString()
                // userId 변수에 사용자 식별자가 저장됩니다.

                // TextView에 식별자를 설정합니다.
                nickname.text = userId

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
                            // 에러 처리 로직 추가
                        } else if (user != null) {
                            // 사용자 정보 가져오기 성공
                            val userId = user.id.toString()

                            // Users 컬렉션에서 해당 user_id의 문서를 찾아 닉네임 업데이트
                            usersCollection
                                .whereEqualTo("user_id", userId)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (!querySnapshot.isEmpty) {
                                        val documentSnapshot = querySnapshot.documents[0]
                                        val documentId = documentSnapshot.id

                                        // 업데이트할 필드와 값 설정
                                        val updates = hashMapOf<String, Any>(
                                            "user_nickname" to newNickname
                                        )

                                        // 문서 업데이트
                                        usersCollection.document(documentId)
                                            .update(updates)
                                            .addOnSuccessListener {
                                                // 업데이트 성공
                                                // 성공적으로 업데이트되었을 때 수행할 작업을 추가하세요.
                                                // TextView에 변경된 닉네임 표시
                                                nickname.text = newNickname
                                            }
                                            .addOnFailureListener { e ->
                                                // 업데이트 실패
                                                // 업데이트 실패 시 수행할 작업을 추가하세요.
                                            }
                                    } else {
                                        // 해당 user_id의 문서를 찾을 수 없음
                                        // 처리할 작업을 추가하세요.
                                    }
                                }
                                .addOnFailureListener { e ->
                                    // 데이터 가져오기 실패
                                    // 실패 처리 작업을 추가하세요.
                                }
                        }
                    }
                }

            builder.show()
        }
    }





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


    }

    private fun updateUserData(userId: String, nickname: String) {
        val database = Firebase.database
        val usersRef = database.getReference("users")

        val userData = Users(userId, nickname)
        usersRef.child(userId).setValue(userData)
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
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }


}