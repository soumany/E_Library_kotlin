package edu.rupp.firstite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kh.edu.ferupp.e_library_kotlin.Modals.Book
import kh.edu.ferupp.e_library_kotlin.databinding.ViewHolderBannerBinding
import com.squareup.picasso.Picasso

class BannerAdapter : ListAdapter<Book, BannerAdapter.BannerViewHolder>(BannerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderBannerBinding.inflate(inflater, parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = getItem(position)
        holder.bindBanner(banner)
    }

    class BannerViewHolder(private val binding: ViewHolderBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindBanner(banner: Book) {
            Picasso.get().load(banner.book_image).into(binding.bannerImage)
            binding.txtTitle.text = banner.title
        }
    }

    private class BannerDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
