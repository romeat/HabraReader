package com.rprihodko.habrareader.author.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rprihodko.habrareader.author.R
import com.rprihodko.habrareader.author.databinding.FragmentAuthorBinding
import com.rprihodko.habrareader.common.Utils.Companion.withHttpsPrefix
import com.rprihodko.habrareader.common.dto.AuthorProfile
import com.rprihodko.habrareader.common.adapters.PostAdapter
import com.rprihodko.habrareader.common.initDefault
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprihodko.habrareader.common.setBackHandlerOnCreate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.MessageFormat
import javax.inject.Inject

@AndroidEntryPoint
class AuthorFragment : Fragment() {

    private val args: AuthorFragmentArgs by navArgs()

    @Inject
    lateinit var authorViewModelFactory: AuthorViewModel.AssistedFactory

    private val viewModel: AuthorViewModel by viewModels {
        AuthorViewModel.provideFactory(authorViewModelFactory, args.authorAlias)
    }

    private var _binding: FragmentAuthorBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PostAdapter(PostAdapter.OnClickListener { post -> viewModel.onPostClick(post) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackHandlerOnCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorBinding.inflate(inflater, container, false)
        binding.myToolbar.initDefault(args.authorAlias) { findNavController().navigateUp() }
        binding.posts.postList.adapter = adapter

        adapter.addLoadStateListener { state ->
            binding.posts.progressBar.isVisible = state.refresh == LoadState.Loading
            binding.posts.postList.isVisible = state.refresh is LoadState.NotLoading
            binding.posts.errorLabel.isVisible = state.refresh is LoadState.Error
        }

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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authorProfile.collect { uiState ->
                    when(uiState) {
                        is AuthorProfileUiState.Error -> Unit
                        is AuthorProfileUiState.Loading -> Unit
                        is AuthorProfileUiState.Success -> showData(uiState.profile)
                    }
                }
            }
        }
        return binding.root
    }

    private fun showData(profile: AuthorProfile) {
        with(binding) {
            rating.text = profile.rating.toString()
            karma.text = profile.scoreStats.score.toString()
            userAlias.text = MessageFormat.format("@{0}", profile.alias)
            userSpeciality.text = profile.speciality ?: getString(R.string.user_default_label)
            postsCount.text = profile.counterStats.postCount.toString()
        }
        Glide.with(binding.avatar)
            .load(profile.avatarUrl?.withHttpsPrefix)
            .transform(CenterInside(), RoundedCorners(10))
            .placeholder(com.rprihodko.habrareader.common.R.drawable.ic_user_avatar_default_48)
            .into(binding.avatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}