package com.example.heung

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import java.util.*

class Setting : AppCompatActivity() {
    private val sharedPreferencesKey = "language_preference"
    private val languagePreferenceKey = "selected_language"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val koreanButton = findViewById<Button>(R.id.button_korean)
        val englishButton = findViewById<Button>(R.id.button_english)



        koreanButton.setOnClickListener {
            saveLanguagePreference("korean")
            // 한국어 선택 시 처리
            setLocale("ko") // 한국어 리소스 사용 설정
            recreate() // 액티비티 재생성하여 변경된 언어 적용
        }

        englishButton.setOnClickListener {
            saveLanguagePreference("english")
            // 영어 선택 시 처리
            setLocale("en") // 영어 리소스 사용 설정
            recreate() // 액티비티 재생성하여 변경된 언어 적용
        }


        // 로그아웃 버튼 클릭 시
        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            // 카카오 로그아웃 요청
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    // 로그아웃 실패
                } else {
                    // 로그아웃 성공
                    val intent = Intent(this@Setting, LoginActivity::class.java)
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
                    // 사용자 정보 가져오기
                    UserApiClient.instance.me { user, _ ->
                        if (user != null) {
                            val userId = user.id.toString()

                            // 카카오 탈퇴 수행
                            UserApiClient.instance.unlink { error ->
                                if (error != null) {
                                    // 탈퇴 실패 처리
                                } else {
                                    val db = FirebaseFirestore.getInstance()
                                    val usersCollection = db.collection("Users")

                                    // userId와 일치하는 문서를 찾아서 삭제
                                    usersCollection
                                        .whereEqualTo("user_id", userId)
                                        .get()
                                        .addOnSuccessListener { querySnapshot ->
                                            if (!querySnapshot.isEmpty) {
                                                val documentSnapshot = querySnapshot.documents[0]
                                                val documentId = documentSnapshot.id

                                                // 문서 삭제
                                                usersCollection
                                                    .document(documentId)
                                                    .delete()
                                                    .addOnSuccessListener {
                                                        val intent = Intent(this@Setting, LoginActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                    .addOnFailureListener { e ->
                                                        // 문서 삭제 실패
                                                    }
                                            } else {
                                                // 해당 userId의 문서를 찾을 수 없음
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
                .setNegativeButton("아니요", null)
            builder.show()
        }
    }

    private fun setLocale(languageCode: String) {
        val resources = resources
        val configuration = resources.configuration
        val locale = Locale(languageCode)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    // 언어 설정 저장 메서드
    private fun saveLanguagePreference(language: String) {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(languagePreferenceKey, language)
        editor.apply()
    }

    fun toggleDarkMode(view: android.view.View) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun setDefaultMode(view: android.view.View) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
