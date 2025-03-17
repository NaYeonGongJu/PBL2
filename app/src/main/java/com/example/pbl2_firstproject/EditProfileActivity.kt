package com.example.pbl2_firstproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()

        val editUsername: EditText = findViewById(R.id.editUsername)
        val editEmail: EditText = findViewById(R.id.editEmail)
        val editCarNumber: EditText = findViewById(R.id.editCarNumber)
        val editPhoneNumber: EditText = findViewById(R.id.editPhoneNumber)
        val editPassword: EditText = findViewById(R.id.editPassword)
        val saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val username = editUsername.text.toString()
            val email = editEmail.text.toString()
            val carNumber = editCarNumber.text.toString()
            val phoneNumber = editPhoneNumber.text.toString()
            val password = editPassword.text.toString()

            // 이메일 업데이트
            val user = auth.currentUser
            user?.updateEmail(email)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 비밀번호 업데이트
                    user.updatePassword(password).addOnCompleteListener { passwordTask ->
                        if (passwordTask.isSuccessful) {
                            // 사용자 이름 업데이트 (Firebase에서는 displayName을 업데이트하는 방법이 필요)
                            // 예: user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build())
                            Toast.makeText(this, "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            finish() // 수정 후 이전 화면으로 돌아가기
                        } else {
                            Toast.makeText(this, "비밀번호 수정 실패: ${passwordTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "이메일 수정 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            // 차번호와 전화번호는 Firebase Realtime Database 또는 Firestore에 저장해야 함
            // 예: saveCarAndPhoneNumberToDatabase(carNumber, phoneNumber)
        }
    }
}
