package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.models.DataSearchingItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiServiceSearch {
    @GET("/books/book/{title}")
    fun loadSearchingImage(@Header("Authorization") authorizationHeade: String,
                           @Path("title") title: String): Call<List<DataSearchingItem>>
}