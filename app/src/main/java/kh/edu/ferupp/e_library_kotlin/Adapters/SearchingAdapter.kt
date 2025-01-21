package kh.edu.ferupp.e_library_kotlin.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kh.edu.ferupp.e_library_kotlin.Home_Screen.BookDetailActivity
import kh.edu.ferupp.e_library_kotlin.Modals.DataSearchingItem
import kh.edu.ferupp.e_library_kotlin.R

class SearchingAdapter(
    private var searchResults: List<DataSearchingItem>,
    private val context: Context?
) : RecyclerView.Adapter<SearchingAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImageView: ImageView = itemView.findViewById(R.id.imageSearching)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    fun updateData(newResults: List<DataSearchingItem>) {
        this.searchResults = newResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searching = searchResults[position]
        holder.titleTextView.text = searching.title
        Glide.with(holder.bookImageView.context).load(searching.book_image).into(holder.bookImageView)

        holder.bookImageView.setOnClickListener { v ->
            val context = v.context
            val intentBookSearch = Intent(context, BookDetailActivity::class.java).apply {
                putExtra("book_image_url", searching.book_image)
                putExtra("book_title", searching.title)
                putExtra("book_price", searching.price)
                putExtra("book_publisher", searching.publisher)
                putExtra("book_Category_Name", searching.category.name)
                putExtra("author_name", searching.author.author_name)
                putExtra("author_Decs", searching.author.author_decs)
                putExtra("book_id", searching.id)
                putExtra("book_pdf", searching.book_pdf)
            }
            context.startActivity(intentBookSearch)
        }
    }
}