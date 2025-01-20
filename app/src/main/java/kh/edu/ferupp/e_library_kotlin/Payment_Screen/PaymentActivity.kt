package kh.edu.ferupp.e_library_kotlin.Payment_Screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kh.edu.ferupp.e_library_kotlin.Modals.PaymentCart
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.Services.ApiServicePayment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PaymentActivity : AppCompatActivity() {
    private lateinit var apiServicePayment: ApiServicePayment
    private var accessToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Retrieve user_id, book_ids, and prices from intent
        val userId = intent.getIntExtra("user_id", 0)
        val bookIds = intent.getIntegerArrayListExtra("book_ids")
        val prices = intent.getSerializableExtra("prices") as? ArrayList<Double>

        // Log userId and book details to Logcat
        Log.d("PaymentActivity", "User ID: $userId")
        if (bookIds != null && prices != null) {
            for (i in bookIds.indices) {
                Log.d("PaymentActivity", "Actual Book ID: ${bookIds[i]}")
                Log.d("PaymentActivity", "Price: ${prices[i]}")
            }
        }

        // Initialize Retrofit with base URL and Gson converter factory
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Initialize ApiServicePayment with Retrofit instance
        apiServicePayment = retrofit.create(ApiServicePayment::class.java)
        // Fetch access token from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        accessToken = sharedPreferences.getString("access_token", "")

        // Find views
        val cardNumber = findViewById<EditText>(R.id.CardNumber)
        val cardHolderName = findViewById<EditText>(R.id.CardHolderNumber)
        val expirationDate = findViewById<EditText>(R.id.ExpirationCard)
        val cvv = findViewById<EditText>(R.id.Cvv)
        val btnDone = findViewById<Button>(R.id.btnDone)

        // Set input type to number and add InputFilter to restrict input to numbers only
        cardNumber.inputType = InputType.TYPE_CLASS_NUMBER
        cardHolderName.inputType = InputType.TYPE_CLASS_NUMBER
        cvv.inputType = InputType.TYPE_CLASS_NUMBER
        expirationDate.inputType = InputType.TYPE_CLASS_NUMBER

        // Add input filters to restrict input to numbers only and show a toast if input is invalid
        val numberFilter = InputFilter { source: CharSequence, _, _, _, _, _ ->
            for (i in source.indices) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Fields must be numbers only", Toast.LENGTH_LONG).show()
                    return@InputFilter ""
                }
            }
            null
        }
        cardNumber.filters = arrayOf(numberFilter)
        cardHolderName.filters = arrayOf(numberFilter)
        cvv.filters = arrayOf(numberFilter)
        expirationDate.filters = arrayOf(numberFilter)

        // Set click listener for Done button
        btnDone.setOnClickListener {
            val cardNumberText = cardNumber.text.toString().trim()
            val cardHolderNameText = cardHolderName.text.toString().trim()
            val expirationDateText = expirationDate.text.toString().trim()
            val cvvText = cvv.text.toString().trim()

            // Validate input
            if (cardNumberText.isEmpty() || cardHolderNameText.isEmpty() || expirationDateText.isEmpty() || cvvText.isEmpty()) {
                Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_LONG).show()
            } else {
                // Create PaymentCart object
                var paymentCart = PaymentCart(userId,
                    bookIds?.get(0) ?: 0, cardNumberText, cardHolderNameText,
                    expirationDateText, cvvText, prices?.get(0) ?: 0.0)

                // Call makePayment method
                makePayment(paymentCart)
            }
        }

    }

    // Method to make payment
    private fun makePayment(paymentCart: PaymentCart) {
        // Create authorization header
        val authorizationHeader = "Bearer $accessToken"

        // Call the API using Retrofit
        val call = apiServicePayment.makePayment(authorizationHeader, paymentCart)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle success
                    Toast.makeText(this@PaymentActivity, "Payment successful", Toast.LENGTH_LONG).show()
                    finish() // Finish the activity after successful payment
                } else {
                    // Handle failure
                    Toast.makeText(this@PaymentActivity, "Payment failed", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Log the error
                Log.e("PaymentActivity", "Error making payment: ${t.message}")

                // Handle failure
                Toast.makeText(this@PaymentActivity, "Payment failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}