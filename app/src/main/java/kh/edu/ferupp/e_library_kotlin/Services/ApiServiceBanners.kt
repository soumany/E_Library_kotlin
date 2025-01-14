package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.models.Book
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiServiceBanners {
    @GET("/books/book")
    fun loadBannerImage(@Header("Authorization") authorizationHeader: String): Call<List<Book>>
}