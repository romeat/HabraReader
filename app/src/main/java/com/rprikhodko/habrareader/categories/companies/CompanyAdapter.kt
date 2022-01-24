package com.rprikhodko.habrareader.categories.companies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rprikhodko.habrareader.Utils.Companion.toStringWithThousands
import com.rprikhodko.habrareader.categories.companies.data.CompanyPreview
import com.rprikhodko.habrareader.databinding.CompanyItemBinding

class CompanyAdapter : PagingDataAdapter<CompanyPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

    class PostDiffCallback : DiffUtil.ItemCallback<CompanyPreview>() {
        override fun areItemsTheSame(oldItem: CompanyPreview, newItem: CompanyPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CompanyPreview, newItem: CompanyPreview): Boolean {
            return oldItem == newItem
        }
    }

    class CompanyViewHolder(private val binding: CompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CompanyPreview) {
            with(binding) {
                title.text = item.title
                description.text = item.description
                rating.text = item.statistics.rating.toString()
                subscribers.text = item.statistics.subscribersCount.toStringWithThousands
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val company = getItem(position)
        company?.let { (holder as CompanyViewHolder).bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CompanyViewHolder(
            CompanyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}