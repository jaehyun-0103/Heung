package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.kakao.sdk.user.UserApiClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logout = findViewById<Button>(R.id.logout)

        val textView = findViewById<TextView>(R.id.textView)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
                // 에러 처리 로직 추가
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                val userId = user.id.toString()
                // userId 변수에 사용자 식별자가 저장됩니다.

                // TextView에 식별자를 설정합니다.
                textView.text = userId
            }
        }

        val changeButton: Button = findViewById(R.id.button)

        changeButton.setOnClickListener {
            val intent = Intent(this, SelfProfActivity::class.java)
            startActivity(intent)
        }


        // 로그아웃 버튼 클릭 시
        logout.setOnClickListener {
            // 카카오 로그아웃 요청
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    // 로그아웃 실패
                    // 오류 처리 코드를 추가하세요.
                } else {
                    // 로그아웃 성공
                    // LoginActivity로 돌아가는 인텐트 생성
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }


            val btn_postlist = findViewById<Button>(R.id.btn_postlist)
            btn_postlist.setOnClickListener {
                val intent = Intent(this, PostListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}