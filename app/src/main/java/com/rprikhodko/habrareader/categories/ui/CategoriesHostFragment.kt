package com.rprikhodko.habrareader.categories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentCategoriesHostBinding

class CategoriesHostFragment : Fragment() {

    private var _binding : FragmentCategoriesHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesHostBinding.inflate(inflater, container, false)
        return binding.root
    }
}