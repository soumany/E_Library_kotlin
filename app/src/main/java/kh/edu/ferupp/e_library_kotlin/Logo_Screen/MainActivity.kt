package kh.edu.ferupp.e_library_kotlin.Logo_Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.SignIn_Screen.SignInActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        },5000)
    }
}

