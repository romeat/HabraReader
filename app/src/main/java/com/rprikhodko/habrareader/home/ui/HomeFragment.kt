package com.rprikhodko.habrareader.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.rprikhodko.habrareader.databinding.FragmentHomeBinding
import com.rprikhodko.habrareader.home.ARTICLES_PAGE_INDEX
import com.rprikhodko.habrareader.home.HomePagerAdapter
import com.rprikhodko.habrareader.home.NEWS_PAGE_INDEX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel>()
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
            tab.text = getTabTitle(position)
        }.attach()


        //homeViewModel.getMainPage()

        /*
        binding.toArticle.setOnClickListener{ findNavController().navigate(R.id.action_navigation_home_to_postFragment) }
        binding.toComments.setOnClickListener { findNavController().navigate(R.id.action_navigation_home_to_commentsFragment) }


         */
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            ARTICLES_PAGE_INDEX -> "СТАТЬИ"
            NEWS_PAGE_INDEX -> "НОВОСТИ"
            else -> null
        }
    }
}