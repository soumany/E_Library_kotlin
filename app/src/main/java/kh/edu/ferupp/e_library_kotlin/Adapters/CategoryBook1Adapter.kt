package kh.edu.ferupp.e_library_kotlin.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Home_Screen.BookDetailActivity
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.databinding.ViewBannerCategorybook1Binding
import com.squareup.picasso.Picasso

class CategoryBook1Adapter: ListAdapter<Book, CategoryBook1Adapter.CategoryBook1ViewHolder>(CategoryBook1DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryBook1ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewBannerCategorybook1Binding.inflate(inflater, parent, false)
        return CategoryBook1ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryBook1ViewHolder, position: Int) {
        val categoryBook1 = getItem(position)
        holder.bindCategoryBook1(categoryBook1)
    }

    class CategoryBook1ViewHolder(private var binding: ViewBannerCategorybook1Binding): RecyclerView.ViewHolder(binding.root) {
        fun bindCategoryBook1(category1Book: Book){
            Picasso.get().load(category1Book.book_image).into(binding.bannerCategory)
            binding.txtPricateCategory1.text = category1Book.price
            binding.txtTitleCategory1.text = category1Book.title

            binding.bannerCategory.setOnClickListener({v ->
                val context = v.context
                val intentBookDetail = Intent(context, BookDetailActivity::class.java).apply {
                    putExtra("book_image_url", category1Book.book_image)
                    putExtra("book_title", category1Book.title)
                    putExtra("book_price", category1Book.price)
                    putExtra("book_publisher", category1Book.publisher)
                    putExtra("book_Category_Name", category1Book.category.name)
                    putExtra("author_name", category1Book.author.author_name)
                    putExtra("description", category1Book.description)
                    putExtra("book_id", category1Book.id)
                    putExtra("book_pdf", category1Book.book_pdf)
                }
                context.startActivity(intentBookDetail)

            })
        }

    }

    class CategoryBook1DiffCallback: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id === newItem.id
        }


    }
}