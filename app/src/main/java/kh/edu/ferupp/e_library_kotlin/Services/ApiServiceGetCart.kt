package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.models.Book
import kh.edu.ferupp.e_library_kotlin.models.BookX
import kh.edu.ferupp.e_library_kotlin.models.CartDataBookItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiServiceGetCart {
    @GET("/events/cart")
    fun loadCartImage(@Header("Authorization") authorizationHeader: String): Call<List<CartDataBookItem>>
}