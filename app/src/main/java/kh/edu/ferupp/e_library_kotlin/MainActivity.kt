package kh.edu.ferupp.e_library_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.models.Book
import kh.edu.ferupp.e_library_kotlin.adapter.BookAdapter
import kh.edu.ferupp.e_library_kotlin.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var bookAdapter: BookAdapter
    private val booksList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(booksList)
        recyclerView.adapter = bookAdapter

        // Fetch books from the API
        fetchBooks()
    }

    private fun fetchBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getBooks()
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    // Update UI on the main thread
                    withContext(Dispatchers.Main) {
                        booksList.clear()
                        booksList.addAll(books)
                        bookAdapter.notifyDataSetChanged()
                    }
                } else {
                    // Log error code
                    Log.e("API Error", "Error code: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to load books", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                Log.e("API Error", "HttpException: ${e.message()}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "API error: ${e.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API Error", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
