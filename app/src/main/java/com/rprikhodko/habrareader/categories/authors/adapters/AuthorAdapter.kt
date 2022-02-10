package com.rprikhodko.habrareader.categories.authors.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rprihodko.habrareader.common.dto.AuthorPreview
import com.rprikhodko.habrareader.R
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
                fullName.text = item.fullName?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT) }
                alias.text = MessageFormat.format("@{0}", item.alias)
                speciality.text = item.speciality ?: speciality.hint
                rating.text = item.rating.toString()
                score.text = item.scoreStats.score.toString()
            }
            Glide.with(binding.avatar)
                .load("https:".plus(item.avatarUrl))
                .transform(CenterInside(), RoundedCorners(10))
                .placeholder(R.drawable.ic_user_avatar_default_48)
                .into(binding.avatar)
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