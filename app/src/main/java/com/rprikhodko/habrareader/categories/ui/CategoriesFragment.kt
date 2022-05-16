package com.rprikhodko.habrareader.categories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.rprihodko.habrareader.common.initRootToolbar
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.categories.adapters.*
import com.rprikhodko.habrareader.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        binding.toolbar.initRootToolbar(getString(R.string.title_categories))
        val viewPager = binding.pager
        viewPager.adapter = CategoriesPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.setText(getTabTitle(position))
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTabTitle(position: Int): Int {
        return when (position) {
            HUBS_PAGE_INDEX -> R.string.hubs_tab_title
            AUTHORS_PAGE_INDEX -> R.string.authors_tab_title
            COMPANIES_PAGE_INDEX -> R.string.companies_tab_title
            else -> throw IndexOutOfBoundsException()
        }
    }
}