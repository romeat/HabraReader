package com.rprikhodko.habrareader.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentCommentsBinding

class CommentsFragment: Fragment() {
    private var _binding: FragmentCommentsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        binding.toProfile.setOnClickListener{ findNavController().navigate(R.id.profile) }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}