package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.models.CartdataItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiServicePostCartBook {
    @POST("/events/cart")
    fun AddCartBook(@Header("Authorization") authorizationHeader: String,
                    @Body addToCart: CartdataItem
    ): Call<CartdataItem>
}