package kh.edu.ferupp.e_library_kotlin.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import kh.edu.ferupp.e_library_kotlin.models.Book

interface BookApiService {
    @GET("books")
    suspend fun getBooks(): Response<List<Book>>
}