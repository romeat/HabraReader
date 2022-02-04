package com.rprikhodko.habrareader.categories.companies.ui

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
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprikhodko.habrareader.R
import com.rprikhodko.habrareader.categories.companies.adapters.CompanyAdapter
import com.rprikhodko.habrareader.databinding.FragmentCompaniesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompaniesFragment : Fragment() {

    private val viewModel by viewModels<CompaniesViewModel>()
    private var _binding: FragmentCompaniesBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CompanyAdapter(CompanyAdapter.OnClickListener{ company -> viewModel.onCompanyClick(company) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompaniesBinding.inflate(inflater, container, false)

        binding.companiesList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.companies.collectLatest { value ->
                    adapter.submitData(value)
                }
            }
        }

        viewModel.eventsFlow.onEach {
            when(it) {
                is Event.NavigateToCompany -> findNavController().navigate(
                    Uri.parse(ArgNames.COMPANY_DEEP_LINK + it.companyAlias))
                    //R.id.company, bundleOf(
                    //COMPANY_ALIAS_ARG_NAME to it.companyAlias))
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}