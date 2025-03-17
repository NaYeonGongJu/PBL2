package com.example.pbl2_firstproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance() // Firebase 인스턴스 가져오기

        val registerButton: Button = findViewById(R.id.button2) // 회원가입 버튼 객체 생성
        registerButton.setOnClickListener {  // 버튼 클릭 시 이벤트
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.button) // 로그인 버튼 객체 생성
        loginButton.setOnClickListener { // 로그인 버튼 클릭 시 loginUser 함수 실행
            loginUser()
        }
    }

    private fun loginUser() { // XML에서 ID를 가져옴
        val email = findViewById<EditText>(R.id.EmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.Password).text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공 시
                    val user = auth.currentUser
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show() // 로그인 성공 메시지
                    // MyPageActivity로 이동
                    val intent = Intent(this, MyPageActivity::class.java)
                    startActivity(intent)
                    finish() // 로그인 화면 종료
                } else {

                    // 로그인 실패 시
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
