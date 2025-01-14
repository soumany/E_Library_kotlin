package kh.edu.ferupp.e_library_kotlin.Author_Screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kh.edu.ferupp.e_library_kotlin.Adapters.AuthorAdapter
import kh.edu.ferupp.e_library_kotlin.Adapters.CategoryBook1Adapter
import kh.edu.ferupp.e_library_kotlin.Modals.Author
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceAuthor
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceBanners
import kh.edu.ferupp.e_library_kotlin.databinding.FragmentAuthorBinding
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderAuthorBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthorFragment : Fragment() {
    private lateinit var accessToken: String
    private lateinit var binding: FragmentAuthorBinding
    private lateinit var authorAdapter: AuthorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAuthorBinding.inflate(inflater, container, false)
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val retrievedAccessToken: String? = sharedPreferences.getString("access_token", null)

        if (retrievedAccessToken != null && !retrievedAccessToken.isEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: $retrievedAccessToken");
            // Store the retrieved access token for later use
            accessToken = retrievedAccessToken
        }else{
            Toast.makeText(requireContext(), "Access token not available or empty", Toast.LENGTH_LONG).show();
        }

        binding.recycleViewAuthor.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            authorAdapter = AuthorAdapter()
            adapter = authorAdapter
        }

        // Store the retrieved access token for later use
        accessToken = retrievedAccessToken.toString()

        //Call all function to display
        if (!accessToken.isNullOrEmpty()) {
            loadAuthorImage()

        } else {
            Toast.makeText(requireContext(), "Access token not available or empty", Toast.LENGTH_LONG).show();
        }

        return binding.root
    }

    private fun loadAuthorImage(){
        val httpClient = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiServiceAuthor = httpClient.create(ApiServiceAuthor::class.java)
        val task = apiServiceAuthor.loadAuthorImage("Bearer $accessToken")
        task.enqueue(object : Callback<List<Author>> {
            override fun onResponse(call: Call<List<Author>>, response: Response<List<Author>>) {
                if (response.isSuccessful) {
                    authorAdapter.submitList(response.body())
                } else {
                    Toast.makeText(requireContext(), "Failed reload banner", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Author>>, t: Throwable) {
                Toast.makeText(requireContext(), t.message ?: "Unknown error", Toast.LENGTH_LONG).show()
            }
        })
    }
}