package kh.edu.ferupp.e_library_kotlin.Author_Screen

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Adapters.AuthorAdapter
import kh.edu.ferupp.e_library_kotlin.Adapters.BookAuthorAdapter
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceAuthorBook
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AuthorInformationActivity : AppCompatActivity() {
    private val MAX_LINES_COLLAPSED = 2
    private var isExpanded = false
    private lateinit var bookAuthorAdapter: BookAuthorAdapter
    private lateinit var recyclerView: RecyclerView
    private var accessToken: String? = null
    private var authorName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_information)

        // Fetch access token from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        accessToken = sharedPreferences.getString("access_token", null)

        if (!accessToken.isNullOrEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: $accessToken")
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(this, "Access token not available or empty", Toast.LENGTH_LONG).show()
        }

        // Initialize the adapter with ListAdapter
        bookAuthorAdapter = BookAuthorAdapter()

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.AuthorBookDisplay)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        recyclerView.adapter = bookAuthorAdapter


        // If access token is available, load banner images
        if (accessToken != null) {
            loadCategoryImage()
        }

        // Get intent extras
        val authorImageUrl = intent.getStringExtra("author_image_url")
        val authorGender = intent.getStringExtra("author_gender")
        authorName = intent.getStringExtra("author_name")
        val authorDesc = intent.getStringExtra("author_decs")

        // Set author image and details
        val imageView: ImageView = findViewById(R.id.imgAuthor)
        Picasso.get().load(authorImageUrl).into(imageView)

        val textViewGender: TextView = findViewById(R.id.txtAuthorGender)
        val textViewName: TextView = findViewById(R.id.txtAuthorName)
        val textViewDesc: TextView = findViewById(R.id.descriptionTextView)
        val showMoreTextView: TextView = findViewById(R.id.showMoreTextView)

        // Create and set styled text for author details
        setStyledText(textViewName, "Name: ", authorName)
        setStyledText(textViewGender, "Gender: ", authorGender)
        setStyledText(textViewDesc, "Description: ", authorDesc)
        textViewDesc.maxLines = MAX_LINES_COLLAPSED

        // Set click listener for "Show more" button
        showMoreTextView.setOnClickListener {
            if (isExpanded) {
                // Collapse the description
                textViewDesc.maxLines = MAX_LINES_COLLAPSED
                showMoreTextView.text = "Show more"
            } else {
                // Expand the description
                textViewDesc.maxLines = Integer.MAX_VALUE // Show all lines
                showMoreTextView.text = "Show less"
            }
            isExpanded = !isExpanded
        }
    }

    // Helper method to set styled text
    private fun setStyledText(textView: TextView, label: String, value: String?) {
        val spannableLabel = SpannableString(label)
        spannableLabel.setSpan(ForegroundColorSpan(Color.GREEN), 0, spannableLabel.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableLabel
        textView.append(value)
    }

    private fun loadCategoryImage(){
        val httpClient = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var apiServiceCategory = httpClient.create(ApiServiceAuthorBook::class.java)
        val task: Call<List<Book>> = apiServiceCategory.FilterAuthorBook("Bearer $accessToken")
        task.enqueue(object : Callback<List<Book>>{
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    val allBooks = response.body()
                    if (allBooks != null) {
                        // Filter books related to the current author
                        val filteredBooks = allBooks.filter { it.author.author_name == authorName }
                        bookAuthorAdapter.submitList(filteredBooks)
                    }
                } else {
                    Toast.makeText(this@AuthorInformationActivity, "Failed reload banner", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("Error", "Failed to reload banner: ${t.message}", t)
                Toast.makeText(this@AuthorInformationActivity, "Failed to reload banner", Toast.LENGTH_LONG).show()
            }

        })
    }
}
