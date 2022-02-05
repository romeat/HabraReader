package com.rprihodko.habrareader.hub.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rprihodko.habrareader.common.adapters.PostAdapter
import com.rprihodko.habrareader.common.dto.HubProfile
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprihodko.habrareader.hub.databinding.FragmentHubBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HubFragment : Fragment() {

    val args: HubFragmentArgs by navArgs()

    @Inject
    lateinit var hubViewModelFactory: HubViewModel.AssistedFactory

    private val viewModel: HubViewModel by viewModels {
        HubViewModel.provideFactory(hubViewModelFactory, args.hubAlias)
    }

    private var _binding: FragmentHubBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PostAdapter(PostAdapter.OnClickListener { post -> viewModel.onPostClick(post) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHubBinding.inflate(inflater, container, false)

        binding.postList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.posts.collectLatest{ value ->
                    adapter.submitData(value)
                }
            }
        }

        viewModel.eventsFlow
            .onEach {
                when(it) {
                    is Event.NavigateToPost -> findNavController().navigate(
                        Uri.parse(ArgNames.POST_DEEP_LINK + it.postId))
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hubProfile.collect { uiState ->
                    when(uiState) {
                        is HubProfileUiState.Error -> Unit
                        is HubProfileUiState.Loading -> Unit
                        is HubProfileUiState.Success -> showData(uiState.hub)
                    }
                }
            }
        }
        return binding.root
    }

    private fun showData(hubProfile: HubProfile) {
        with(binding) {
            rating.text = hubProfile.statistics.rating.toString()
            hubTitle.text = hubProfile.titleHtml
            hubDescription.text = hubProfile.descriptionHtml
        }
    }
}