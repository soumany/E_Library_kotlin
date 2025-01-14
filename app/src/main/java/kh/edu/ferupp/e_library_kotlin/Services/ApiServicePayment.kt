package kh.edu.ferupp.e_library_kotlin.Services

import kh.edu.ferupp.e_library_kotlin.models.PaymentCart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header

interface ApiServicePayment {
    @POST("/events/payment")
    fun makePayment(@Header("Authorization") authorizationHeader: String, @Body paymentCart: PaymentCart): Call<Void>
}