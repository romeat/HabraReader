package com.rprikhodko.habrareader.categories.hubs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rprihodko.habrareader.common.R
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.Utils.Companion.withHttpsPrefix
import com.rprihodko.habrareader.common.dto.HubPreview
import com.rprikhodko.habrareader.databinding.HubItemBinding

class HubAdapter(private val onClickListener: OnClickListener) : PagingDataAdapter<HubPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

    class PostDiffCallback : DiffUtil.ItemCallback<HubPreview>() {
        override fun areItemsTheSame(oldItem: HubPreview, newItem: HubPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HubPreview, newItem: HubPreview): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (hub: HubPreview) -> Unit) {
        fun onClick(hub: HubPreview) = clickListener(hub)
    }

    class HubViewHolder(private val binding: HubItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HubPreview) {
            with(binding) {
                title.text = item.title
                description.text = item.description
                rating.text = item.statistics.rating.toString()
                subscribers.text = item.statistics.subscribersCount.toStringWithThousands
            }
            Glide.with(binding.avatar)
                .load(item.imageUrl.withHttpsPrefix)
                .into(binding.avatar)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val hub = getItem(position)
        hub?.let {
            holder.itemView.setOnClickListener {
                onClickListener.onClick(hub)
            }
            (holder as HubViewHolder).bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HubViewHolder(
            HubItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}