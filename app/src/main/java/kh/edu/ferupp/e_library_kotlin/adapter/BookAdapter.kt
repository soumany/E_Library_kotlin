
package kh.edu.ferupp.e_library_kotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.R
import kh.edu.ferupp.e_library_kotlin.models.Book


class BookAdapter(private val books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int = books.size

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
        }
    }
}
