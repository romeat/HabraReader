package com.rprikhodko.habrareader.categories.hubs.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.categories.hubs.adapters.HubAdapter
import com.rprikhodko.habrareader.databinding.FragmentCategoryCommonBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HubsFragment : Fragment() {

    private val viewModel by viewModels<HubsViewModel>()
    private var _binding: FragmentCategoryCommonBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        HubAdapter(HubAdapter.OnClickListener{ hub -> viewModel.onHubClick(hub) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryCommonBinding.inflate(inflater, container, false)

        binding.itemList.adapter = adapter

        adapter.addLoadStateListener { state ->
            binding.progressBar.isVisible = state.refresh == LoadState.Loading
            binding.itemList.isVisible = state.refresh is LoadState.NotLoading
            binding.errorLabel.isVisible = state.refresh is LoadState.Error
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hubs.collectLatest { value ->
                    adapter.submitData(value)
                }
            }
        }

        viewModel.eventsFlow.onEach {
            when(it) {
                is Event.NavigateToHub -> findNavController().navigate(
                    Uri.parse(ArgNames.HUB_DEEP_LINK + it.hubAlias))
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}