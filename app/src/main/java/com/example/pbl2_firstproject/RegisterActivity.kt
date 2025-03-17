package com.example.pbl2_firstproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Firebase를 사용하는 권한
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance() // Firebase에서 인스턴스를 가져올 것이다!
        firestore = FirebaseFirestore.getInstance()

        val registerButton: Button = findViewById(R.id.buttonRegister) // 회원가입 버튼 객체 생성

        registerButton.setOnClickListener { // 눌렀을 때 registerUser 함수를 쓸 것이다!
            registerUser()
        }
    }

    private fun registerUser() {
        val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
        val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber).text.toString()
        val carNumber = findViewById<EditText>(R.id.editTextCarNumber).text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // 사용자 프로필 업데이트
                    user?.updateProfile(UserProfileChangeRequest.Builder()
                        .setDisplayName(username) // 사용자 이름 설정
                        .build())?.addOnCompleteListener { profileUpdateTask ->
                        if (profileUpdateTask.isSuccessful) {
                            // Firestore에 사용자 세부 정보 저장
                            saveUserData(username, email, phoneNumber, carNumber)
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this, "프로필 업데이트 실패: ${profileUpdateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(username: String, email: String, phoneNumber: String, carNumber: String) {
        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "carNumber" to carNumber
        )

        firestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("RegisterActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "문서 추가 오류", e)
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
