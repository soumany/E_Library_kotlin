package kh.edu.ferupp.e_library_kotlin.Search_Screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kh.edu.ferupp.e_library_kotlin.Adapters.SearchingAdapter
import kh.edu.ferupp.e_library_kotlin.Modals.DataSearchingItem
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceSearch
import kh.edu.ferupp.e_library_kotlin.databinding.FragmentFavoriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var searchingAdapter: SearchingAdapter
    private lateinit var apiServiceSearch: ApiServiceSearch
    private var accessToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        // Fetch access token from SharedPreferences
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val retrievedAccessToken = sharedPreferences.getString("access_token", null)

        if (!retrievedAccessToken.isNullOrEmpty()) {
            // Log the retrieved access token
            Log.d("AccessToken", "Retrieved Access Token: $retrievedAccessToken")
            // Store the retrieved access token for later use
            accessToken = retrievedAccessToken
        } else {
            // Handle scenario where access token is not available or empty
            Toast.makeText(requireContext(), "Access token not available or empty", Toast.LENGTH_LONG).show()
        }

        setupRecyclerView()
        setupSearchView()
        return binding.root
    }

    private fun setupRecyclerView(){
        // Create an empty list initially
        val searchResults: List<DataSearchingItem> = ArrayList()
        searchingAdapter = SearchingAdapter(searchResults, context)
        binding.recycleViewSearch.layoutManager = LinearLayoutManager(context)
        binding.recycleViewSearch.adapter = searchingAdapter
    }

    private fun setupSearchView(){
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)) {
                    query?.let { searchBook(it) }
                } else {
                    Toast.makeText(requireContext(), "Please enter a title to search", Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    // Clear the search results when the query is empty
                    searchingAdapter.updateData(ArrayList())
                }
                return false
            }

        })
    }

    private fun searchBook(title: String){
        if (accessToken != null){
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            apiServiceSearch = retrofit.create(ApiServiceSearch::class.java)
            apiServiceSearch.loadSearchingImage("Bearer $accessToken", title).enqueue(object : Callback<List<DataSearchingItem>> {
                override fun onResponse(call: Call<List<DataSearchingItem>>, response: Response<List<DataSearchingItem>>) {
                    if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                        searchingAdapter.updateData(response.body()!!);
                    } else {
                        Toast.makeText(requireContext(), "Title not found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<DataSearchingItem>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_LONG).show()
                }
            })
        }else{
            Toast.makeText(requireContext(), "Access token not available or empty", Toast.LENGTH_LONG).show()
        }
    }
}