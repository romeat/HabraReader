package com.rprikhodko.habrareader.ui.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentPostBinding

class PostFragment: Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        binding.toProfile.setOnClickListener{ findNavController().navigate(R.id.profile)}
        binding.toComments.setOnClickListener{ findNavController().navigate(R.id.comment)}


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}