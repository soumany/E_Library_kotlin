package kh.edu.ferupp.e_library_kotlin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kh.edu.ferupp.e_library_kotlin.adapter.BookAdapter
import kh.edu.ferupp.e_library_kotlin.databinding.ActivityMainBinding
import kh.edu.ferupp.e_library_kotlin.models.Book
import kh.edu.ferupp.e_library_kotlin.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private val booksList = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        setupRecyclerView()

        // Fetch books from the API
        fetchBooks()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(booksList)
        binding.recyclerView.adapter = bookAdapter
    }

    private fun fetchBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getBooks()
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        booksList.clear()
                        booksList.addAll(books)
                        bookAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("API Error", "Error code: ${response.code()}")
                    showToastOnMainThread("Failed to load books")
                }
            } catch (e: HttpException) {
                Log.e("API Error", "HttpException: ${e.message}")
                showToastOnMainThread("API error: ${e.message}")
            } catch (e: Exception) {
                Log.e("API Error", "Exception: ${e.message}")
                showToastOnMainThread("Network error")
            }
        }
    }

    private suspend fun showToastOnMainThread(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
