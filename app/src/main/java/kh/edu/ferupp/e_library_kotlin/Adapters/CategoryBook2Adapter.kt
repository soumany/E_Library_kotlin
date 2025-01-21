package kh.edu.ferupp.e_library_kotlin.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Home_Screen.BookDetailActivity
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderCategorybook2Binding
import com.squareup.picasso.Picasso

class CategoryBook2Adapter: ListAdapter<Book, CategoryBook2Adapter.CategoryBook2ViewHolder>(CategoryBook1DiffCallback()) {
    class CategoryBook2ViewHolder(private var binding: ViewHolderCategorybook2Binding): RecyclerView.ViewHolder(binding.root) {
        fun bindCategoryBook2(category2book: Book){
            Picasso.get().load(category2book.book_image).into(binding.bannerCategory2)
            binding.txtPricateCategory2.text = category2book.price
            binding.txtTitleCategory2.text = category2book.title

            binding.bannerCategory2.setOnClickListener({v->
                val context = v.context
                val intentDookDetail = Intent(context, BookDetailActivity::class.java).apply {
                    putExtra("book_image_url", category2book.book_image)
                    putExtra("book_title", category2book.title)
                    putExtra("book_price", category2book.price)
                    putExtra("book_publisher", category2book.publisher)
                    putExtra("book_Category_Name", category2book.category.name)
                    putExtra("author_name", category2book.author.author_name)
                    putExtra("description", category2book.description)
                    putExtra("book_id", category2book.id)
                    putExtra("book_pdf", category2book.book_pdf)
                }
                context.startActivity(intentDookDetail)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryBook2ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderCategorybook2Binding.inflate(inflater, parent, false)
        return CategoryBook2ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryBook2ViewHolder, position: Int) {
        val categoryBook2 = getItem(position)
        holder.bindCategoryBook2(categoryBook2)
    }
}