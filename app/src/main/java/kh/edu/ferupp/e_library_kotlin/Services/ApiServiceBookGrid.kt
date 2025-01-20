package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.models.Book
import kh.edu.ferupp.e_library_kotlin.models.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServiceBookGrid {
    @GET("/books/category")
    fun getCategories(@Header("Authorization") authHeader: String): Call<List<Category>>
    @GET("/books/book")
    fun FilterByCategory(@Header("Authorization") authorizationHeader: String,
                         @Query("categoryName") categoryName: String
    ): Call<List<Book>>
}