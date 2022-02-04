package com.rprihodko.habrareader.company.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rprihodko.habrareader.common.adapters.PostAdapter
import com.rprihodko.habrareader.common.dto.CompanyProfile
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprihodko.habrareader.company.R
import com.rprihodko.habrareader.company.databinding.FragmentCompanyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompanyFragment : Fragment() {

    private val args: CompanyFragmentArgs by navArgs()

    @Inject
    lateinit var companyViewModelFactory: CompanyViewModel.AssistedFactory

    private val viewModel: CompanyViewModel by viewModels {
        CompanyViewModel.provideFactory(companyViewModelFactory, args.companyAlias)
    }

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PostAdapter(PostAdapter.OnClickListener { post -> viewModel.onPostClick(post) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)

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
                        //R.id.post,
                        //bundleOf(POST_ID_ARG_NAME to it.postId))
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.companyProfile.collect { uiState ->
                    when(uiState) {
                        is CompanyProfileUiState.Error -> Unit
                        is CompanyProfileUiState.Loading -> Unit
                        is CompanyProfileUiState.Success -> showData(uiState.profile)
                    }
                }
            }
        }
        return binding.root
    }

    private fun showData(profile: CompanyProfile) {
        with(binding) {
            rating.text = profile.statistics.rating.toString()
            subsCount.text = profile.statistics.subscribersCount.toString()
            companyName.text = profile.titleHtml
            profile.descriptionHtml?.let {
                companyDescription.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT);
                companyDescription.isVisible = true
            }
            profile.siteUrl?.let {
                companySite.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT);
                companySite.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}