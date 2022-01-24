package com.rprikhodko.habrareader.categories

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rprikhodko.habrareader.categories.authors.ui.AuthorsFragment
import com.rprikhodko.habrareader.categories.companies.ui.CompaniesFragment
import com.rprikhodko.habrareader.categories.hubs.ui.HubsFragment

const val HUBS_PAGE_INDEX = 0
const val AUTHORS_PAGE_INDEX = 1
const val COMPANIES_PAGE_INDEX = 2

class CategoriesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment)  {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HUBS_PAGE_INDEX to { HubsFragment() },
        AUTHORS_PAGE_INDEX to { AuthorsFragment() },
        COMPANIES_PAGE_INDEX to { CompaniesFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}