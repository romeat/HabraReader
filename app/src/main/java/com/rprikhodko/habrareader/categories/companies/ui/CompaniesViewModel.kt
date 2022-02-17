package com.rprikhodko.habrareader.categories.companies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprikhodko.habrareader.categories.companies.CompaniesPagingSource
import com.rprikhodko.habrareader.categories.companies.OnCompanyListener
import com.rprihodko.habrareader.common.dto.CompanyPreview
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompaniesViewModel @Inject constructor(
    private val useCase: GetCompaniesUseCase
) : ViewModel(), OnCompanyListener {

    val companies: Flow<PagingData<CompanyPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun newPager(): Pager<Int, CompanyPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, CompanyPreview> {
        return CompaniesPagingSource(useCase)
    }

    override fun onCompanyClick(company: CompanyPreview) {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToCompany(company.alias))
        }
    }
}

sealed class Event {
    class NavigateToCompany(val companyAlias: String) : Event()
}