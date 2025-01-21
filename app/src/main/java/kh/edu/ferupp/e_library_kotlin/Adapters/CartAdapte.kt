package kh.edu.ferupp.e_library_kotlin.Adapters

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Cart_Screen.CartFragment
import kh.edu.ferupp.e_library_kotlin.Modals.CartDataBookItem
import kh.edu.ferupp.e_library_kotlin.Services.ApiServiceDeleteCart
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderCartBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CartAdapte(private val context: Context) : ListAdapter<CartDataBookItem, CartAdapte.CartViewHolder>(CartDiffCallback()) {

    private var progressDialog: ProgressDialog? = null
    private var isDeleting = false

    inner class CartViewHolder(private val binding: ViewHolderCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var quantity: Int = 0

        init {
            binding.btnIncrease.setOnClickListener { increaseQuantity() }
            binding.btnDecrease.setOnClickListener { decreaseQuantity() }
            binding.delete.setOnClickListener {
                deleteItem(adapterPosition)
            }
        }

        fun bindCart(cart: CartDataBookItem) {
            Picasso.get().load(cart.book.book_image).into(binding.CartBookImage)
            binding.txtTitleCart.text = cart.book.title
            binding.txtPriceCart.text = cart.book.price
            binding.txtQuantity.text = quantity.toString()
        }

        private fun increaseQuantity() {
            quantity++
            binding.txtQuantity.text = quantity.toString()
        }

        private fun decreaseQuantity() {
            if (quantity > 0) {
                quantity--
                binding.txtQuantity.text = quantity.toString()
            }
        }

        private fun deleteItem(position: Int) {
            if (position != RecyclerView.NO_POSITION) {
                val book = getItem(position)
                val bookId = book.id

                // Show loading indicator
                showProgressDialog()

                // Delete the item after a delay of 2 seconds
                deleteItemWithDelay(bookId, position)
            }
        }

        private fun deleteItemWithDelay(bookId: Int, position: Int) {
            Handler().postDelayed({
                deleteCartItemFromServer(bookId, position)
            }, 2000)
        }

        private fun showProgressDialog() {
            progressDialog = ProgressDialog(context)
            progressDialog?.apply {
                setMessage("Deleting item...")
                setCancelable(false)
                show()
            }
        }

        private fun deleteCartItemFromServer(itemId: Int, position: Int) {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("access_token", null)

            if (!accessToken.isNullOrEmpty()) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiServiceDeleteCart::class.java)
                val call: Call<Void> = apiService.deleteCartItem("Bearer $accessToken", itemId)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        dismissProgressDialog()
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_LONG).show()
                            // Remove the item from the list
                            val updatedList = currentList.toMutableList()
                            updatedList.removeAt(position)
                            submitList(updatedList)
                        } else {
                            Toast.makeText(context, "Failed to delete item from server", Toast.LENGTH_SHORT).show()
                            Log.e("CartAdapter", "Failed to delete item from server: " + response.message())
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        dismissProgressDialog()
                        Toast.makeText(context, "Network error. Failed to delete item from server", Toast.LENGTH_SHORT).show()
                        Log.e("CartAdapter", "Failed to delete item from server", t)
                    }
                })
            } else {
                Toast.makeText(context, "Access token not available or empty", Toast.LENGTH_LONG).show()
            }
        }

        private fun dismissProgressDialog() {
            progressDialog?.takeIf { it.isShowing }?.dismiss()
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartDataBookItem>() {
        override fun areItemsTheSame(oldItem: CartDataBookItem, newItem: CartDataBookItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CartDataBookItem, newItem: CartDataBookItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = getItem(position)
        holder.bindCart(cart)
    }
}
