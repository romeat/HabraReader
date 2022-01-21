package com.rprikhodko.habrareader.common.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rprikhodko.habrareader.Utils
import com.rprikhodko.habrareader.common.data.dto.PostPreview
import com.rprikhodko.habrareader.databinding.PostItemBinding

class PostAdapter : PagingDataAdapter<PostPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

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
        post?.let { (holder as PostViewHolder).bind(it) }
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
                views.text = stats.readingCount.toString()
            }
        }
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