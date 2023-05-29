import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.heung.R

class RecruitContentActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    lateinit var titleTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var toggleStatus: ToggleButton
    lateinit var applyButton: Button
    lateinit var endDateTextView: TextView
    lateinit var contentTextView: TextView
    lateinit var participantsTextView: TextView
    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recrucont)

        initViews()
        setListeners()
        loadRecruitData()
    }

    private fun initViews() {
        firestore = FirebaseFirestore.getInstance()

        titleTextView = findViewById(R.id.text_recruit_title)
        dateTextView = findViewById(R.id.text_recruit_date)
        toggleStatus = findViewById(R.id.toggle_status)
        applyButton = findViewById(R.id.button_apply)
        endDateTextView = findViewById(R.id.text_recruit_endDate)
        contentTextView = findViewById(R.id.text_recruit_content)
        participantsTextView = findViewById(R.id.text_participants)
        backButton = findViewById(R.id.button_back)
    }

    private fun setListeners() {
        toggleStatus.setOnCheckedChangeListener { _, isChecked ->
            changeRecruitmentStatus(isChecked)
        }
        applyButton.setOnClickListener { applyRecruit() }
        backButton.setOnClickListener { finish() }
    }
    private fun loadRecruitData() {
        val recruitId = intent.getStringExtra("RECRUIT_ID") ?: return
        firestore.collection("Recruits")
            .document(recruitId)
            .get()
            .addOnSuccessListener { document ->
                titleTextView.text = document.getString("recruit_title")
                dateTextView.text = document.getString("recruit_date")
                contentTextView.text = document.getString("recruit_content")
                endDateTextView.text = document.getString("recruit_endDate")

                val isOpen = document.getBoolean("recruit_isOpen") ?: false
                toggleStatus.isChecked = !isOpen
                participantsTextView.visibility = if (isOpen) View.VISIBLE else View.GONE
                loadParticipants(recruitId)
            }
    }

    private fun changeRecruitmentStatus(isClosed: Boolean) {
        val recruitId = intent.getStringExtra("RECRUIT_ID") ?: return
        firestore.collection("Recruits").document(recruitId).update("recruit_isOpen", !isClosed)
            .addOnSuccessListener {
                participantsTextView.visibility = if (!isClosed) View.VISIBLE else View.GONE
            }
    }

    private fun applyRecruit() {
        // 기존의 참여 신청 코드를 작성하세요.
    }

    private fun loadParticipants(recruitId: String) {
        firestore.collection("Recruits")
            .document(recruitId)
            .collection("Participants")
            .get()
            .addOnSuccessListener { documents ->
                val names = mutableListOf<String>()
                for (document in documents) {
                    names.add(document["name"].toString())
                }
                participantsTextView.text = names.joinToString(", ")
            }
    }
}