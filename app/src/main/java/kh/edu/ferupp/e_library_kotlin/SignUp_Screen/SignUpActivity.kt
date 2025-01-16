package  kh.edu.ferupp.e_library_kotlin.SignUp_Screen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import  kh.edu.ferupp.e_library_kotlin.R
import  kh.edu.ferupp.e_library_kotlin.SignIn_Screen.SignInActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class SignUpActivity : AppCompatActivity() {
    private lateinit var btnSignUp : Button
    private lateinit var edtEmailSignUp: EditText
    private lateinit var edtPasswordSignUp: EditText
    private lateinit var edtGenderSignUp: EditText
    private lateinit var edtUsernameSignUp: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        //initalize the button id
        val backToSignIn = findViewById<Button>(R.id.backToSignIn)
        btnSignUp = findViewById(R.id.btnSignUp)
        edtEmailSignUp = findViewById(R.id.edtEmailSignUp)
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp)
        edtGenderSignUp = findViewById(R.id.edtGenderSignUp)
        edtUsernameSignUp = findViewById(R.id.edtUsernameSignUp)


        //go back to SignIn screen
        backToSignIn.setOnClickListener({
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        })

        //when button signUp Click
        btnSignUp.setOnClickListener({
            authenticateUserSignUp()
        })
    }

    private fun authenticateUserSignUp() {
        val username = edtUsernameSignUp.text.toString().trim()
        val email = edtEmailSignUp.text.toString().trim()
        val password = edtPasswordSignUp.text.toString().trim()
        val gender = edtGenderSignUp.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "All fields cannot be empty", Toast.LENGTH_LONG).show()
            return
        }
        if (!email.contains("@") || !email.contains(".") || !email.contains("com")) {
            Toast.makeText(this, "Invalid emial address", Toast.LENGTH_LONG).show()
            return
        }
        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("SignUp", "Username: $username, email: $email, password: $password, gender: $gender")

        val request = AuthRequest(username, email, password, gender)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // Base URL without the endpoint
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.register(request)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    val accessToken = authResponse?.accessToken
                    val userId = authResponse?.user?.id ?: 0
//                  Toast.makeText(this@SignUpActivity, "Sign-up successful", Toast.LENGTH_LONG).show()
                    val layoutInflater = layoutInflater
                    val customToastLayout = layoutInflater.inflate(R.layout.custome_right_signup, null)

                    val toast = Toast(this@SignUpActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = customToastLayout
                    toast.show()

                    Log.d("SignUp", "Access Token: $accessToken")
//                    Log.d("SignUp", "User ID: $userId")

                    // Store the access token in SharedPreferences
                    sharedPreferences.edit().apply {
                        putString("access_token", accessToken)
                        putInt("user_id", userId)
                        apply()
                    }

                    // Retrieve the access token from SharedPreferences
                    val storedToken = sharedPreferences.getString("access_token", null)
                    Log.d("SignUp", "Stored Access Token: $storedToken")

                    val intentSuccess = Intent(this@SignUpActivity, MainActivityButtomnavScreen::class.java)
                    intentSuccess.putExtra("username", username)
                    startActivity(intentSuccess)
                    finish()
                } else {
//                    Toast.makeText(this@SignUpActivity, "Failed to Sign-up", Toast.LENGTH_LONG).show()
                    val layoutInflater = layoutInflater
                    val customToastLayout = layoutInflater.inflate(R.layout.custome_wrong_signup, null)

                    val toast = Toast(this@SignUpActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = customToastLayout
                    toast.show()
                    Log.e("SignUp", "Failed to sign up: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Toast.makeText(this@SignUpActivity, "Failed to sign-up", Toast.LENGTH_LONG).show()
                val layoutInflater = layoutInflater
                val customToastLayout = layoutInflater.inflate(R.layout.custome_wrong_signup, null)

                val toast = Toast(this@SignUpActivity)
                toast.duration = Toast.LENGTH_LONG
                toast.view = customToastLayout
                toast.show()
                Log.e("SignUp", "Failed to sign up", t)
            }
        })
    }

    //static class of AuthRequest
    data class AuthRequest(var username: String, var email: String, var password: String, var gender: String)

    //static class of AuthResponse
    data class AuthResponse(val access_token: String, val user: User) {
        val accessToken: String
            get() = access_token

        data class User(val id: Int)
    }

    interface ApiService {
        @POST("authorization/register")
        fun register(@Body request: AuthRequest): Call<AuthResponse>
    }

}