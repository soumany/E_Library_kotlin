package kh.edu.ferupp.e_library_kotlin.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Home_Screen.BookDetailActivity
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderGridbooksBinding
import com.squareup.picasso.Picasso

class BookGridAdapter: ListAdapter<Book, BookGridAdapter.BookGridViewHolder>(BookGridDiffCallback()) {
    class BookGridViewHolder(private val binding:ViewHolderGridbooksBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBookGrid(bookGrid: Book){
            Picasso.get().load(bookGrid.book_image).into(binding.imageBookGrid)
            binding.txtTitleBookGrid.text = bookGrid.title
            binding.txtPriceBookGrid.text = bookGrid.price

            binding.imageBookGrid.setOnClickListener({v->
                val context = v.context
                val intentBookDetail = Intent(context, BookDetailActivity::class.java).apply {
                    putExtra("book_image_url", bookGrid.book_image)
                    putExtra("book_title", bookGrid.title)
                    putExtra("book_price", bookGrid.price)
                    putExtra("book_publisher", bookGrid.publisher)
                    putExtra("book_Category_Name", bookGrid.category.name)
                    putExtra("author_name", bookGrid.author.author_name)
                    putExtra("description", bookGrid.description)
                    putExtra("book_id", bookGrid.id)
                    putExtra("book_pdf", bookGrid.book_pdf)
                }
                context.startActivity(intentBookDetail)
            })
        }
    }

    class BookGridDiffCallback: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id === newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookGridViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderGridbooksBinding.inflate(inflater, parent, false)
        return BookGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookGridViewHolder, position: Int) {
        val bookGrid = getItem(position)
        holder.bindBookGrid(bookGrid)

    }
}