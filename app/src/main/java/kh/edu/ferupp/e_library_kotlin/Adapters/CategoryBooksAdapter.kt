package kh.edu.ferupp.e_library_kotlin.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Home_Screen.BookDetailActivity
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderCategoryBookBinding
import com.squareup.picasso.Picasso

class CategoryBooksAdapter : ListAdapter<Book, CategoryBooksAdapter.ViewHolderCategoryBook>(CategoryBookDiffCallback()){
    class ViewHolderCategoryBook(private val binding:ViewHolderCategoryBookBinding):
    RecyclerView.ViewHolder(binding.root){
        fun bindCategoryBooks(categoryBooks: Book){
            Picasso.get().load(categoryBooks.book_image).into(binding.bookAuthorImage)
            binding.txtBookAuthorTitle.text = categoryBooks.title

            binding.bookAuthorImage.setOnClickListener{v->
                var context = v.context
                var intentCategoryBooks = Intent(context, BookDetailActivity::class.java).apply {
                    putExtra("book_image_url", categoryBooks.book_image)
                    putExtra("book_title", categoryBooks.title)
                    putExtra("book_price", categoryBooks.price)
                    putExtra("book_publisher", categoryBooks.publisher)
                    putExtra("book_Category_Name", categoryBooks.category.name)
                    putExtra("author_name", categoryBooks.author.author_name)
                    putExtra("description", categoryBooks.description)
                    putExtra("book_id", categoryBooks.id)
                    putExtra("book_pdf", categoryBooks.book_pdf)
                }

                context.startActivity(intentCategoryBooks)
            }
        }

    }

    class CategoryBookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id === newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategoryBook {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderCategoryBookBinding.inflate(inflater, parent, false)
        return ViewHolderCategoryBook(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderCategoryBook, position: Int) {
        val categoryBook = getItem(position)
        holder.bindCategoryBooks(categoryBook)
    }
}