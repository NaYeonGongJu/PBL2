package com.example.pbl2_firstproject
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MyPageActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        auth = FirebaseAuth.getInstance()
        val userInfo: TextView = findViewById(R.id.userInfo)
        val editProfileButton: Button = findViewById(R.id.editProfileButton)

        // 현재 사용자 정보를 표시
        val user = auth.currentUser
        userInfo.text = "사용자 이름: ${user?.displayName}\n이메일: ${user?.email}"

        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}