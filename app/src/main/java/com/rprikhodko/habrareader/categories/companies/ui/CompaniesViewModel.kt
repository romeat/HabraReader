package com.rprikhodko.habrareader.categories.companies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprikhodko.habrareader.categories.companies.CompaniesPagingSource
import com.rprikhodko.habrareader.categories.companies.data.CompanyPreview
import com.rprikhodko.habrareader.categories.companies.domain.GetCompaniesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CompaniesViewModel @Inject constructor(
    private val useCase: GetCompaniesUseCase
) : ViewModel() {

    val companies: Flow<PagingData<CompanyPreview>> = newPager().flow.cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, CompanyPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, CompanyPreview> {
        return CompaniesPagingSource(useCase)
    }
}