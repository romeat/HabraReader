package com.rprikhodko.habrareader.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rprikhodko.habrareader.ui.articles.ArticlesFragment
import com.rprikhodko.habrareader.ui.news.NewsFragment

const val ARTICLES_PAGE_INDEX = 0
const val NEWS_PAGE_INDEX = 1

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment)  {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        ARTICLES_PAGE_INDEX to { ArticlesFragment() },
        NEWS_PAGE_INDEX to { NewsFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}