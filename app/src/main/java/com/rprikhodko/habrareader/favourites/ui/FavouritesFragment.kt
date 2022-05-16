package com.rprikhodko.habrareader.favourites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rprihodko.habrareader.common.initRootToolbar
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
      private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        binding.toolbar.initRootToolbar(getString(R.string.title_favourites))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}