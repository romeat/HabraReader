package com.rprikhodko.habrareader.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentHomeBinding
import com.rprikhodko.habrareader.home.adapters.*
import dagger.hilt.android.AndroidEntryPoint

class HomeFragment : Fragment()  {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val tabLayout = binding.tabLayout
        val viewPager = binding.pager

        viewPager.adapter = HomePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(getTabTitle(position))
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTabTitle(position: Int): Int {
        return when (position) {
            ARTICLES_PAGE_INDEX -> R.string.articles_tab_title
            NEWS_PAGE_INDEX -> R.string.news_tab_title
            else -> throw IndexOutOfBoundsException()
        }
    }
}