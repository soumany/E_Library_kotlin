package kh.edu.ferupp.e_library_kotlin.Home_Screen

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Adapters.CategoryBooksAdapter
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.Modals.CartdataItem
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceCategoryBooks
import kh.edu.ferupp.e_library_kotlin.Services.ApiServicePostCartBook
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BookDetailActivity : AppCompatActivity() {
    private val MAX_LINES_COLLAPSED = 5
    private var isExpanded = false
    private val addedBookIds = HashSet<Int>()
    private lateinit var categoryBooksAdapter: CategoryBooksAdapter
    private lateinit var recyclerView: RecyclerView
    private var isItemAddedToCart = false
    private lateinit var accessToken: String
    private var categoryName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val bookPdf: String? = intent.getStringExtra("book_pdf")
        val addToFavoritesButton: Button = findViewById(R.id.btnAddToFavorites)
        val btnAddToCart: Button = findViewById(R.id.btnAddToCart)

        //button add to cart
        btnAddToCart.setOnClickListener({
            AddCartBook()
        })

        //click and go to PDF screen
        addToFavoritesButton.setOnClickListener({
            // Log the value of bookPdf just before starting BookPdfActivity
            Log.d("BookDetailActivity", "PDF URL: " + bookPdf);

            // Create an Intent to navigate to FavoritesActivity
            var intentPDF = Intent(this@BookDetailActivity, BookPdfActivity::class.java)

            // Pass the book PDF URL to the BookPdfActivity
            intentPDF.putExtra("book_pdf_url", bookPdf)

            // Start the FavoritesActivity
            startActivity(intentPDF)
        })

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val retrievedAccessToken = sharedPreferences.getString("access_token", null)

        if (retrievedAccessToken != null && retrievedAccessToken.isNotEmpty()) {
            Log.d("AccessToken", "Retrieved Access Token: $retrievedAccessToken")
            accessToken = retrievedAccessToken
        } else {
            Toast.makeText(this, "Access token not available or empty", Toast.LENGTH_LONG).show()
        }

        // Initialize the adapter with ListAdapter
        categoryBooksAdapter = CategoryBooksAdapter()

        // set up the recycleview
        recyclerView = findViewById(R.id.CategoryBookDisplay)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        recyclerView.adapter = categoryBooksAdapter

        // If access token is available, load banner images
        if(accessToken != null){
            loadCategoryBookImage()
        }

        categoryName = intent.getStringExtra("book_Category_Name")

        val bookImageUrl = intent.getStringExtra("book_image_url")
        val bookTitle = intent.getStringExtra("book_title")
        val bookPrice = intent.getStringExtra("book_price")
        val bookPublisher = intent.getStringExtra("book_publisher")
        val bookCategoryName = intent.getStringExtra("book_Category_Name")
        val authorName = intent.getStringExtra("author_name")
        val authorDecs = intent.getStringExtra("description")
        val bookId = intent.getIntExtra("book_id", 0)

        Log.d("SignIn", "Book ID: $bookId")
        Log.d("SignIn", "Book Pdf: $bookPdf")

        if ("free".equals(bookPrice, ignoreCase = true)) {
            findViewById<Button>(R.id.btnAddToFavorites).visibility = View.VISIBLE
        } else {
            findViewById<Button>(R.id.btnAddToCart).visibility = View.VISIBLE
        }

        val imageView = findViewById<ImageView>(R.id.BookImageView)
        val textView = findViewById<TextView>(R.id.txtTitleDetail)
        val textViewPrice = findViewById<TextView>(R.id.txtPriceCategoryGeneral)
        val textViewPublisher = findViewById<TextView>(R.id.txtpublisherCategoryGeneral)
        Picasso.get().load(bookImageUrl).into(imageView)

        val showMoreTextView = findViewById<TextView>(R.id.showMoreTextView)

        val spannableTitleLabel = SpannableString("Title: ").apply {
            setSpan(ForegroundColorSpan(Color.BLACK), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textView.text = spannableTitleLabel
        textView.append(bookTitle)

        val spannablePriceLabel = SpannableString("Price: ").apply {
            setSpan(ForegroundColorSpan(Color.BLACK), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textViewPrice.text = spannablePriceLabel
        textViewPrice.append(bookPrice)

        val spannablePublisherLabel = SpannableString("Publisher: ").apply {
            setSpan(ForegroundColorSpan(Color.BLACK), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textViewPublisher.text = spannablePublisherLabel
        textViewPublisher.append(bookPublisher)

        val textViewBookCategoryName = findViewById<TextView>(R.id.BookCategoryName)
        textViewBookCategoryName.text = "Category Name: $bookCategoryName"

        val textViewAuthorName = findViewById<TextView>(R.id.BookAuthorName)
        textViewAuthorName.text = "Author Name: $authorName"

        val textViewAuthorDecs = findViewById<TextView>(R.id.BookAuthorDecs)
        textViewAuthorDecs.text = "Description: $authorDecs"
        textViewAuthorDecs.maxLines = MAX_LINES_COLLAPSED

        showMoreTextView.setOnClickListener {
            if (isExpanded) {
                textViewAuthorDecs.maxLines = MAX_LINES_COLLAPSED
                showMoreTextView.text = "Show more"
            } else {
                textViewAuthorDecs.maxLines = Integer.MAX_VALUE
                showMoreTextView.text = "Show less"
            }
            isExpanded = !isExpanded
        }
    }

    private fun AddCartBook(){

        // Check if the item is already added to the cart using bookId
        val bookId = intent.getIntExtra("book_id", 0)
        if (addedBookIds.contains(bookId)) {
//            Toast.makeText(this, "Item is already in the cart", Toast.LENGTH_SHORT).show()
            val layoutInflater = layoutInflater
            val customToastLayout = layoutInflater.inflate(R.layout.custome_double_cart, null)

            val toast = Toast(this@BookDetailActivity)
            toast.duration = Toast.LENGTH_LONG
            toast.view = customToastLayout
            toast.show()
            return  // Exit the method to avoid adding the item again
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create ApiServiceCartBook instance
        var apiServicePostCartBook = retrofit.create(ApiServicePostCartBook::class.java)


        // Fetch book details from intent
        //int bookId = getIntent().getIntExtra("book_id", 0);
        val userId = intent.getIntExtra("user_id", 0) // Implement this method to get the user ID
        val quantity = 1 // You can set the quantity as per your requirements

        // Create an instance of AddCartBook and set user_id, book_id, and quantity
        var addToCart = CartdataItem(
            book_id = bookId,
            user_id = userId,
            quantity = quantity
        )

        // Make the POST request with the access token in the header and AddCartBook instance in the request body
        apiServicePostCartBook.AddCartBook("Bearer $accessToken", addToCart)
            .enqueue(object : Callback<CartdataItem?> {
                override fun onResponse(call: Call<CartdataItem?>?, response: Response<CartdataItem?>) {
                    if (response.isSuccessful()) {
                        // Handle successful response
//                    Toast.makeText(BookDetailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this@BookDetailActivity, "Successful", Toast.LENGTH_LONG).show()
                        val layoutInflater = layoutInflater
                        val customToastLayout = layoutInflater.inflate(R.layout.custome_right_detailbook, null)

                        val toast = Toast(this@BookDetailActivity)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = customToastLayout
                        toast.show()
                        isItemAddedToCart = true

                        // Add the bookId to the HashSet
                        addedBookIds.add(bookId)
                    } else {
                        // Handle unsuccessful response
//                    Log.e("AddToCart", "Failed to add book to cart: " + response.message());
//                        Toast.makeText(
//                            this@BookDetailActivity,
//                            "Failed to add book to cart",
//                            Toast.LENGTH_LONG
//                        ).show()
                        val layoutInflater = layoutInflater
                        val customToastLayout = layoutInflater.inflate(R.layout.custome_wrong_detailbook, null)

                        val toast = Toast(this@BookDetailActivity)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = customToastLayout
                        toast.show()
                    }
                }

                override fun onFailure(call: Call<CartdataItem?>?, t: Throwable?) {
                    // Handle failure
//                Log.e("AddToCart", "Failed to add book to cart", t);
                    val layoutInflater = layoutInflater
                    val customToastLayout = layoutInflater.inflate(R.layout.custome_wrong_detailbook, null)

                    val toast = Toast(this@BookDetailActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = customToastLayout
                    toast.show()
                }
            })
    }

    // function display category depend on book that are clicking
    private fun loadCategoryBookImage(){
        val httpClient = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var apiServiceCategoryBook = httpClient.create(ApiServiceCategoryBooks::class.java)
        val task: Call<List<Book>> = apiServiceCategoryBook.loadCategoryBookImage("Bearer $accessToken")
        task.enqueue(object : Callback<List<Book>>{
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if(response.isSuccessful){
                    val categoryBooks = response.body()
                    if(categoryBooks != null){
                        //Filter book categories
                        val filteredCategoryBook = categoryBooks.filter { it.category.name == categoryName }
                        categoryBooksAdapter.submitList(filteredCategoryBook)
                    }else {
                        Toast.makeText(this@BookDetailActivity, "Failed reload banner", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("Error", "Failed to reload banner: ${t.message}", t)
                Toast.makeText(this@BookDetailActivity, "Failed to reload banner", Toast.LENGTH_LONG).show()
            }

        })
    }
}