package com.rprikhodko.habrareader.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentHomeHostBinding

class HomeHostFragment  : Fragment() {

    private var _binding : FragmentHomeHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeHostBinding.inflate(inflater, container, false)
        return binding.root
    }
}