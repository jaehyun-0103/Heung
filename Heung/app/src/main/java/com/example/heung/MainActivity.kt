package com.example.heung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_postlist = findViewById<Button>(R.id.btn_postlist)
        btn_postlist.setOnClickListener {
            val intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
        }

        val btn_perform = findViewById<Button>(R.id.btn_perform)
        btn_perform.setOnClickListener {
            val intent = Intent(this, CalActivity::class.java)
            startActivity(intent)
        }
    }
}