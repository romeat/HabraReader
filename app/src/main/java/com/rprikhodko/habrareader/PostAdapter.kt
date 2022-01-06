package com.rprikhodko.habrareader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rprikhodko.habrareader.data.dto.post.PostPreview
import com.rprikhodko.habrareader.databinding.PostItemBinding

class PostAdapter : ListAdapter<PostPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

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
        val plant = getItem(position)
        (holder as PostViewHolder).bind(plant)
    }

    class PostViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostPreview) {

            with(binding) {
                author.text = item.author.alias
                title.text = item.title

                val stats = item.stats
                votes.text = stats.votesCount.toString()
                comments.text = stats.commentsCount.toString()
                views.text = stats.readingCount.toString()
            }
        }
    }


    class PostDiffCallback : DiffUtil.ItemCallback<PostPreview>() {

        override fun areItemsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostPreview, newItem: PostPreview): Boolean {
            return oldItem == newItem
        }
    }
}