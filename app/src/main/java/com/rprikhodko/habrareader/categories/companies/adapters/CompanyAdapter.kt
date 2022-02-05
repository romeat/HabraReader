package com.rprikhodko.habrareader.categories.companies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.dto.CompanyPreview
import com.rprikhodko.habrareader.databinding.CompanyItemBinding

class CompanyAdapter(private val onClickListener: OnClickListener) : PagingDataAdapter<CompanyPreview, RecyclerView.ViewHolder>(PostDiffCallback()) {

    class PostDiffCallback : DiffUtil.ItemCallback<CompanyPreview>() {
        override fun areItemsTheSame(oldItem: CompanyPreview, newItem: CompanyPreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CompanyPreview, newItem: CompanyPreview): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (company: CompanyPreview) -> Unit) {
        fun onClick(company: CompanyPreview) = clickListener(company)
    }

    class CompanyViewHolder(private val binding: CompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CompanyPreview) {
            with(binding) {
                title.text = item.title
                rating.text = item.statistics.rating.toString()
                subscribers.text = item.statistics.subscribersCount.toStringWithThousands
                item.description?.let {
                    description.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    description.isVisible = true
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val company = getItem(position)
        company?.let {
            holder.itemView.setOnClickListener {
                onClickListener.onClick(company)
            }
            (holder as CompanyViewHolder).bind(it) }
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