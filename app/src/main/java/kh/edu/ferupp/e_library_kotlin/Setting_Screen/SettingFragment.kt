package kh.edu.ferupp.e_library_kotlin.Setting_Screen
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.SignIn_Screen.SignInActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.POST

class SettingFragment : Fragment() {

    companion object {
        private const val BASE_URL = "http://10.0.2.2:5000/"
    }

    interface YourApiInterface {
        @POST("authorization/logout")
        fun logout(@Header("Authorization") authorization: String): Call<JsonObject>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_setting, container, false)

        // button signOut
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
//        var btnYourBook: Button = view.findViewById(R.id.btnYourBook)

//        btnYourBook.setOnClickListener({
//            var IntentYourBook = Intent(context, YourBookActivity::class.java)
//            startActivity(IntentYourBook)
//        })

//        val btnYourbook: Button = view.findViewById(R.id.btnYourbook)
        val textViewUsername: TextView = view.findViewById(R.id.textViewUsername)
        val username = activity?.intent?.getStringExtra("username")

        if (username != null) {
            textViewUsername.text = "Welcome, $username"
        } else {
            textViewUsername.text = "Welcome"
        }

        // set click listener for the logout button
        btnLogout.setOnClickListener {
            logout()
        }

        return view
    }

    // Method to handle logout
    private fun logout() {
        // Retrieve access_token from SharedPreferences
        val prefs: SharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val accessToken = prefs.getString("access_token", "")

        // Create Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Initialize API interface
        val api = retrofit.create(YourApiInterface::class.java)

        // Call the logout endpoint
        val call = api.logout("Bearer $accessToken")
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    // Handle successful logout response
                    // Navigate to MainActivity
                    val intent = Intent(activity, SignInActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    // Toast success
//                    ToastUtil.showCustomToast(context, "Logout successful", true)
                    Toast.makeText(context, "Sign-in successful", Toast.LENGTH_LONG).show()
                } else {
                    // Handle error
                    Toast.makeText(context, "LogOut Failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Handle failure
//                ToastUtil.showCustomToast(context, "Logout failed", false)
                Toast.makeText(context, "LogOut Failed", Toast.LENGTH_LONG).show()
            }
        })
    }
}