package com.rprihodko.habrareader.author.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rprihodko.habrareader.author.R
import com.rprihodko.habrareader.author.databinding.FragmentAuthorBinding
import com.rprihodko.habrareader.common.dto.AuthorProfile
import com.rprihodko.habrareader.common.adapters.PostAdapter
import com.rprihodko.habrareader.common.navigation.ArgNames
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorBinding.inflate(inflater, container, false)

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
            userSpeciality.text = profile.speciality ?: "Пользователь"
            postsCount.text = profile.counterStats.postCount.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}