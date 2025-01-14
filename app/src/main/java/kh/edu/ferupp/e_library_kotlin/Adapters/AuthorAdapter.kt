package kh.edu.ferupp.e_library_kotlin.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Author_Screen.AuthorInformationActivity
import kh.edu.ferupp.e_library_kotlin.Modals.Author
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderAuthorBinding
import com.squareup.picasso.Picasso

class AuthorAdapter: ListAdapter<Author, AuthorAdapter.AuthorViewHolder>(AuthorDiffCallback()) {
    class AuthorViewHolder(private val binding: ViewHolderAuthorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindAuthorImage(authorImage: Author){
            Picasso.get().load(authorImage.author_image).into(binding.authorImage)
            binding.txtAuthorName.text = authorImage.author_name

            binding.authorImage.setOnClickListener { v ->
                val context = v.context
                val intentAuthorAdapter =
                    Intent(context, AuthorInformationActivity::class.java).apply {
                        putExtra("author_id", authorImage.id)
                        putExtra("author_image_url", authorImage.author_image)
                        putExtra("author_gender", authorImage.gender)
                        putExtra("author_name", authorImage.author_name)
                        putExtra("author_decs", authorImage.author_decs)
                    }
                // Log the intent extras for debugging
                Log.d("AuthorAdapter", "Author ID: ${authorImage.id}")
                Log.d("AuthorAdapter", "Author Image URL: ${authorImage.author_image}")
                Log.d("AuthorAdapter", "Author Gender: ${authorImage.gender}")
                Log.d("AuthorAdapter", "Author Name: ${authorImage.author_name}")
                Log.d("AuthorAdapter", "Author Description: ${authorImage.author_decs}")

                try {
                    context.startActivity(intentAuthorAdapter)
                    Log.d("AuthorAdapter", "Started AuthorInformationActivity successfully")
                } catch (e: Exception) {
                    Log.d("AuthorAdapter", "Error starting AuthorInformationActivity", e)
                }
            }
        }
    }

    class AuthorDiffCallback : DiffUtil.ItemCallback<Author>() {
        override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean {
            return oldItem.id === newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderAuthorBinding.inflate(inflater, parent, false)
        return AuthorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val authorImage = getItem(position)
        holder.bindAuthorImage(authorImage)
    }
}