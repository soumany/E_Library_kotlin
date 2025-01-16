package kh.edu.ferupp.e_library_kotlin.SignIn_Screen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import  kh.edu.ferupp.e_library_kotlin.R
import  kh.edu.ferupp.e_library_kotlin.SignUp_Screen.SignUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignInActivity : AppCompatActivity() {
    // initialize the variables
    private lateinit var btnSignUp: Button
    private lateinit var btnSignIn: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        //initialize the id of btnSignUp
        btnSignUp = findViewById(R.id.btnSignUp)
        btnSignIn = findViewById(R.id.btnSignIn)

        //go to SignUp screen
        btnSignUp.setOnClickListener({
            //intent to SignUp Screen
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        })

        //when user click on button signIn
        btnSignIn.setOnClickListener({
            authenticateUser()
        })
    }

    private fun authenticateUser(){
        val edtUsername = findViewById<EditText>(R.id.editUsername)
        val edtPassword = findViewById<EditText>(R.id.editTextPassword)

        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        // check if the username and password is Empty or not
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Username or Password cannot be empty", Toast.LENGTH_LONG).show()
            return
        }
        Log.d("SignIn", "Username: $username, Password: $password")

        val request = AuthRequest(username, password)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // Base URL without the endpoint
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.login(request)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    val accessToken = authResponse?.accessToken
                    val userId = authResponse?.userId ?: 0

                    //ToastUtil.showCustomToast(this@MainActivity, "Sign-in successful", true)
                    val layoutInflater = layoutInflater
                    val customToastLayout = layoutInflater.inflate(R.layout.custome_right_toast, null)

                    val toast = Toast(this@SignInActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = customToastLayout
                    toast.show()
                    Log.d("SignIn", "Access Token: $accessToken")
                    Log.d("SignIn", "User ID: $userId")

                    //Store the access token in SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("access_token", accessToken)
                    //editor.putInt("user_id", userId)
                    editor.apply()

                    val intentSuccess = Intent(this@SignInActivity, MainActivityButtomnavScreen::class.java)
                    intentSuccess.putExtra("username", username) // Pass the username to MainActivityHomeScreen
                    startActivity(intentSuccess)
                } else {
//                    Toast.makeText(this@SignInActivity, "Failed to sign in", Toast.LENGTH_LONG).show()
                    val layoutInflater = layoutInflater
                    val customToastLayout = layoutInflater.inflate(R.layout.custome_wrong_toast, null)

                    val toast = Toast(this@SignInActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = customToastLayout
                    toast.show()
                    Log.e("SignIn", "Failed to sign in: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Toast.makeText(this@SignInActivity, "Failed to sign in", Toast.LENGTH_LONG).show()
                val layoutInflater = layoutInflater
                val customToastLayout = layoutInflater.inflate(R.layout.custome_wrong_toast, null)

                val toast = Toast(this@SignInActivity)
                toast.duration = Toast.LENGTH_LONG
                toast.view = customToastLayout
                toast.show()
                Log.e("SignIn", "Failed to sign in", t)
            }
        })
    }

    data class AuthRequest(val username: String, val password: String)

    data class AuthResponse(val access_token: String, val user_id: Int) {
        val accessToken: String
            get() = access_token

        val userId: Int
            get() = user_id
    }

    interface ApiService {
        @POST("authorization/login") // Endpoint only
        fun login(@Body request: AuthRequest): Call<AuthResponse>
    }
}