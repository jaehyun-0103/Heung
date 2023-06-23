package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "사용자 인증 실패", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "사용자 인증 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        // 사용자 정보 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                // 프로필 정보 가져오기 성공
                userId = user.id.toString()
            }
        }

        val kakao_login_button = findViewById<ImageButton>(R.id.kakao_login_button) // 로그인 버튼
        val loginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                val TAG = "LoginActivity"
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                val TAG = "LoginActivity"
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                val intent = Intent(this, LogcheckActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        kakao_login_button.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        val TAG = "LoginActivity"
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        UserApiClient.instance.loginWithKakaoAccount(this, callback = loginCallback)
                    } else if (token != null) {
                        val TAG = "LoginActivity"
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = loginCallback)
            }
        }
    }
}