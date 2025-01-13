package kh.edu.ferupp.e_library_kotlin

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//       setContentView(R.layout.activity_login)
//       val signupClickable = findViewById<Button>(R.id.signupButton)
//       signupClickable.setOnClickListener {
//           val intent = Intent(this, SignupActivity::class.java)
//          startActivity(intent)
//       }
    }
}