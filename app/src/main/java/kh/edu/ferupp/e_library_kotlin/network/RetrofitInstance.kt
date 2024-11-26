package kh.edu.ferupp.e_library_kotlin.network
import kh.edu.ferupp.e_library_kotlin.models.Book
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


// Retrofit Instance
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:5000/api/"  // Use this in emulator

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// ApiService Interface
interface ApiService {
    @GET("books")
    suspend fun getBooks(): Response<List<Book>>

    // Other API methods for POST, PUT, DELETE, etc.
}
