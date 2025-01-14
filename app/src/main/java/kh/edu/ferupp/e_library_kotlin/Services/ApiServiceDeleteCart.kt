package kh.edu.ferupp.e_library_kotlin.Services

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiServiceDeleteCart {
    @DELETE("events/cart/{id}")
    fun deleteCartItem(@Header("Authorization") authorizationHeader: String,
                       @Path("id") itemId: Int): Call<Void>
}