package com.rprikhodko.habrareader.home.news.ui

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
import com.rprihodko.habrareader.common.adapters.PostAdapter
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment(){

    private val newsViewModel by viewModels<NewsViewModel>()
    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PostAdapter(PostAdapter.OnClickListener{ post -> newsViewModel.onPostClick(post) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.posts.postList.adapter = adapter

        adapter.addLoadStateListener { state ->
            binding.posts.progressBar.isVisible = state.refresh == LoadState.Loading
            binding.posts.postList.isVisible = state.refresh is LoadState.NotLoading
            binding.posts.errorLabel.isVisible = state.refresh is LoadState.Error
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.news.collectLatest{ value ->
                    adapter.submitData(value)
                }
            }
        }

        newsViewModel.eventsFlow
            .onEach {
                when(it) {
                    is Event.NavigateToPost -> findNavController().navigate(
                        R.id.post, bundleOf(ArgNames.POST_ID_ARG_NAME to it.postId))
                        // for some unknown reason deep link does not work from home navigation stack
                        //Uri.parse(ArgNames.POST_DEEP_LINK + it.postId))
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}