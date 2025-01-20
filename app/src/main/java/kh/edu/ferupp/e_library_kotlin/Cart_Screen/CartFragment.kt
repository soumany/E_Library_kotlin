package kh.edu.ferupp.e_library_kotlin.Cart_Screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kh.edu.ferupp.e_library_kotlin.Adapters.CartAdapte
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.Modals.BookX
import kh.edu.ferupp.e_library_kotlin.Modals.CartDataBookItem
import kh.edu.ferupp.e_library_kotlin.Payment_Screen.PaymentActivity
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceGetCart
import kh.edu.ferupp.e_library_kotlin.databinding.FragmentCartBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapte
    var accessToken: String? = null
    var userId = 0 // Add this line
    private lateinit var btnPaynow: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val retrievedAccessToken = sharedPreferences.getString("access_token", null)
        userId = sharedPreferences.getInt("user_id", -1)

        if (!retrievedAccessToken.isNullOrEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: $retrievedAccessToken");

        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(requireContext(), "Access token not available or empty", Toast.LENGTH_LONG).show()
        }


        // Store the retrieved access token for later use
        accessToken = retrievedAccessToken;

        // If access token is available, load banner images
        if (accessToken != null) {
            loadCartImage()
        }

        binding.recycleViewCart.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            cartAdapter = CartAdapte(requireContext())
            adapter = cartAdapter
        }

        binding.btnPaynow.setOnClickListener{
            handlePayNowClick()
        }


        return binding.root
    }

    private fun loadCartImage(){
        val httpClient = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var apiServiceGetCart = httpClient.create(ApiServiceGetCart::class.java)
        val task: Call<List<CartDataBookItem>> = apiServiceGetCart.loadCartImage("Bearer $accessToken")

        task.enqueue(object : Callback<List<CartDataBookItem>> {
            override fun onResponse(call: Call<List<CartDataBookItem>>, response: Response<List<CartDataBookItem>>) {
                if (response.isSuccessful) {
                    val books: List<CartDataBookItem>? = response.body()
                    Log.d("CartFragment", "Books received: $books")
                    if (books != null) {
                        cartAdapter.submitList(books)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load cart items", Toast.LENGTH_LONG).show()
                    Log.e("CartFragment", "Failed to load cart items: " + response.message())
                }
            }

            override fun onFailure(call: Call<List<CartDataBookItem>>, t: Throwable?) {
                Toast.makeText(requireContext(), "Failed to load cart items", Toast.LENGTH_LONG).show()
                Log.e("CartFragment", "Failed to load cart items", t)
            }
        })
    }

    private fun handlePayNowClick() {
        val currentList = cartAdapter.currentList

        if (currentList.isEmpty()) {
            // Toast.makeText(context, "You need to buy first", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(), "You need to buy first", Toast.LENGTH_LONG).show()
        } else {
            val bookIds = ArrayList<Int>()
            val prices = ArrayList<Double>()
            for (book in currentList) {
                Log.d("CartFragment", "Cart Item ID: ${book.id}")
                Log.d("CartFragment", "Actual Book ID: ${book.book.id}")
                Log.d("CartFragment", "Price: ${book.book.price}")

                bookIds.add(book.book.id)
                prices.add(book.book.price.toDouble())
            }
            Log.d("CartFragment", "User ID: $userId")

            val intent = Intent(context, PaymentActivity::class.java).apply {
                putExtra("user_id", userId)
                putIntegerArrayListExtra("book_ids", bookIds)
                putExtra("prices", prices)
            }
            startActivity(intent)
        }
    }
}