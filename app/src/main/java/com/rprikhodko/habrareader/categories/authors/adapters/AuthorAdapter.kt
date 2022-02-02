package com.rprikhodko.habrareader.categories.authors.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rprihodko.habrareader.common.dto.AuthorPreview
import com.rprikhodko.habrareader.databinding.AuthorItemBinding
import java.text.MessageFormat

class AuthorAdapter(private val onClickListener: OnClickListener) : PagingDataAdapter<AuthorPreview, RecyclerView.ViewHolder>(
    PostDiffCallback()
) {

    class PostDiffCallback : DiffUtil.ItemCallback<AuthorPreview>() {
        override fun areItemsTheSame(oldItem: AuthorPreview, newItem: AuthorPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AuthorPreview, newItem: AuthorPreview): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (author: AuthorPreview) -> Unit) {
        fun onClick(author: AuthorPreview) = clickListener(author)
    }

    class AuthorViewHolder(private val binding: AuthorItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AuthorPreview) {
            with(binding) {
                fullName.text = item.fullName
                alias.text = MessageFormat.format("@{0}", item.alias)
                speciality.text = item.speciality ?: "Пользователь"
                rating.text = item.rating.toString()
                score.text = item.scoreStats.score.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val author = getItem(position)
        author?.let {
            holder.itemView.setOnClickListener {
                onClickListener.onClick(author)
            }
            (holder as AuthorViewHolder).bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AuthorViewHolder(
            AuthorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}