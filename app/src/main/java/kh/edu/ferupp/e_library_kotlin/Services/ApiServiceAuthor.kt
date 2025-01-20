package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.Author
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiServiceAuthor {
    @GET("/author/author")
    fun loadAuthorImage(@Header("Authorization") authorizationHeader:String): Call<List<Author>>
}