package kh.edu.ferupp.e_library_kotlin.Home_Screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Adapters.BookGridAdapter
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceBookGrid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GridBooksActivity : AppCompatActivity() {
    private lateinit var accessToken: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryName: String
    private lateinit var bookGridAdapter: BookGridAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_books)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        accessToken = sharedPreferences.getString("access_token", null).toString()
        //fetch category name from the intent
        var intent: Intent = getIntent()
        categoryName = intent.getStringExtra("categoryName").toString()

        if (accessToken != null && !accessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: " + accessToken);
        } else {
            Toast.makeText(this, "Access token not available or empty", Toast.LENGTH_LONG).show();
        }

        // Initialize the adapter with ListAdapter
        bookGridAdapter = BookGridAdapter()

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycleViewBookGrid)
        var gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = bookGridAdapter

        // If access token is available, load banner images
        if (accessToken != null) {
            FilterCategoryBook()
        }
    }

    private fun FilterCategoryBook(){
        val httpClient = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var apiServiceBookGrid = httpClient.create(ApiServiceBookGrid::class.java)
        val task: Call<List<Book>> = apiServiceBookGrid.FilterByCategory(
            "Bearer $accessToken", categoryName
        )
        task.enqueue(object : Callback<List<Book>>{
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    val bookGridDisplayList: List<Book>? = response.body()

                    // Filter the list based on category name
                    val filteredList: MutableList<Book> = mutableListOf()
                    bookGridDisplayList?.let {
                        for (book in it) {
                            if (book.category?.name == categoryName) {
                                filteredList.add(book)
                            }
                        }
                    }

                    // Update adapter data with the filtered list of books
                    bookGridAdapter.submitList(filteredList)
                }  else {
                    Log.d("Fail Author", "Failed to reload banner")
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("Fail Author", "Failed to reload banner", t);
            }
        })
    }
}