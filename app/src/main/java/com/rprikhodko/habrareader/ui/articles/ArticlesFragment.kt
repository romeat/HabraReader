package com.rprikhodko.habrareader.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentArticlesBinding

class ArticlesFragment : Fragment() {

    private val articlesViewModel by viewModels<ArticlesViewModel>()
    private var _binding: FragmentArticlesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)

        binding.toArticle.setOnClickListener{ findNavController().navigate(R.id.action_navigation_home_to_postFragment) }
        binding.toComments.setOnClickListener { findNavController().navigate(R.id.action_navigation_home_to_commentsFragment) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}