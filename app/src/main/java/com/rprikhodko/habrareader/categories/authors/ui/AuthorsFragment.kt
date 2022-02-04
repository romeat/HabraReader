package com.rprikhodko.habrareader.categories.authors.ui

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
import com.rprikhodko.habrareader.R
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprikhodko.habrareader.categories.authors.adapters.AuthorAdapter
import com.rprikhodko.habrareader.databinding.FragmentAuthorsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthorsFragment : Fragment() {

    private val viewModel by viewModels<AuthorsViewModel>()
    private var _binding: FragmentAuthorsBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        AuthorAdapter(AuthorAdapter.OnClickListener{ author -> viewModel.onAuthorClick(author) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorsBinding.inflate(inflater, container, false)

        binding.authorsList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authors.collectLatest { value ->
                    adapter.submitData(value)
                }
            }
        }

        viewModel.eventsFlow.onEach {
            when(it) {
                is Event.NavigateToAuthor -> findNavController().navigate(
                    Uri.parse(ArgNames.AUTHOR_DEEP_LINK + it.authorAlias))
                    //R.id.profile, bundleOf(
                    //AUTHOR_ALIAS_ARG_NAME to it.authorAlias))
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}