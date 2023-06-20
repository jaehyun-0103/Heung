package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import java.text.SimpleDateFormat
import java.util.*

class CalDetailActivity : AppCompatActivity() {
    private lateinit var calTitleTextView: TextView
    private lateinit var calLocationTextView: TextView
    private lateinit var calStartTimeTextView: TextView
    private lateinit var calEndTimeTextView: TextView
    private lateinit var calMemoTextView: TextView
    private lateinit var DateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caldetail)

        val userId = intent.getStringExtra("userId")
        val calTitle = intent.getStringExtra("calTitle")
        val calLocation = intent.getStringExtra("calLocation")
        val calStartTime = intent.getLongExtra("calStartTime", 0)
        val calEndTime = intent.getLongExtra("calEndTime", 0)
        val calMemo = intent.getStringExtra("calMemo")
        val selectedDate = intent.getStringExtra("selectedDate")
        val calId = intent.getStringExtra("calId")

        calTitleTextView = findViewById(R.id.performName)
        calLocationTextView = findViewById(R.id.performLocation)
        calStartTimeTextView = findViewById(R.id.startTime)
        calEndTimeTextView = findViewById(R.id.endTime)
        calMemoTextView = findViewById(R.id.performMemo)
        DateTextView = findViewById(R.id.Date)

        // 데이터를 TextView에 표시
        calTitleTextView.text = calTitle
        calLocationTextView.text = calLocation
        calStartTimeTextView.text = formatTime(calStartTime)
        calEndTimeTextView.text = formatTime(calEndTime)
        calMemoTextView.text = calMemo
        DateTextView.text = selectedDate

        val btnBack = findViewById<ImageButton>(R.id.diro)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // 수정 버튼 클릭 리스너 설정
        val editButton = findViewById<Button>(R.id.calEdit)
        editButton.visibility = View.GONE

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                val calUserId = user.id.toString() // 사용자 ID

                if (userId == calUserId) {
                    editButton.visibility = View.VISIBLE
                }
                editButton.setOnClickListener {
                    val intent = Intent(this, CalEditActivity::class.java)
                    intent.putExtra("calTitle", calTitle)
                    intent.putExtra("calLocation", calLocation)
                    intent.putExtra("calStartTime", calStartTime)
                    intent.putExtra("calEndTime", calEndTime)
                    intent.putExtra("calMemo", calMemo)
                    intent.putExtra("selectedDate", selectedDate)
                    intent.putExtra("calId", calId)
                    startActivity(intent)
                }
            }
        }


        // 삭제 버튼 초기 상태를 숨김으로 설정
        val deleteButton = findViewById<Button>(R.id.calDelect)
        deleteButton.visibility = View.GONE
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                // 프로필 정보 가져오기 실패
            } else if (user != null) {
                val calUserId = user.id.toString() // 사용자 ID
                if (userId == calUserId) { // user_id가 동일한 경우에는 삭제 버튼을 보이지 않음
                    deleteButton.visibility = View.VISIBLE
                }
                deleteButton.setOnClickListener {
                    val collectionName = "Calendar"
                    val collectionRef = FirebaseFirestore.getInstance().collection(collectionName)
                    val query = collectionRef.whereEqualTo("cal_id", calId)
                    query.get()
                        .addOnSuccessListener { querySnapshot ->
                            for (documentSnapshot in querySnapshot.documents) {
                                documentSnapshot.reference.delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "삭제 성공했습니다.", Toast.LENGTH_SHORT)
                                            .show() // 메인 화면으로 이동
                                        val intent = Intent(this, CalActivity::class.java)
                                        startActivity(intent)
                                        finish() // 현재 액티비티 종료
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "삭제 실패했습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            // 쿼리 실행 실패
                        }
                }
            }
        }
    }

    // 시간 형식을 변환하는 함수
    private fun formatTime(date: Long): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = date

        val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
        return outputFormat.format(calendar.time)
    }
}