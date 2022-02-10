package com.rprihodko.habrareader.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rprihodko.habrareader.common.R
import com.rprihodko.habrareader.common.Utils
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.databinding.PostItemBinding
import com.rprihodko.habrareader.common.dto.PostPreview

class PostAdapter(private val onClickListener: OnClickListener) : PagingDataAdapter<PostPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = getItem(position)
        post?.let {
            holder.itemView.setOnClickListener {
                onClickListener.onClick(post)
            }
            (holder as PostViewHolder).bind(it)
        }
    }

    class PostViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostPreview) {
            with(binding) {
                author.text = item.author.alias
                title.text = item.title
                postDate.text = Utils.formatTime(item.timePublished)
                val stats = item.stats
                votes.text = stats.votesCount.toString()
                comments.text = stats.commentsCount.toString()
                views.text = stats.readingCount.toStringWithThousands
                bookmarked.text = stats.favoritesCount.toString()
            }
            Glide.with(binding.avatar)
                .load("https:".plus(item.author.avatarUrl))
                .transform(CenterInside(), RoundedCorners(10))
                .placeholder(R.drawable.ic_user_avatar_default_48)
                .into(binding.avatar)
        }
    }

    class OnClickListener(val clickListener: (post: PostPreview) -> Unit) {
        fun onClick(post: PostPreview) = clickListener(post)
    }

    class PostDiffCallback : DiffUtil.ItemCallback<PostPreview>() {

        override fun areItemsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
            return false //oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
            return false //oldItem == newItem
        }
    }
}