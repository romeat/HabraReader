package com.rprikhodko.habrareader.categories.hubs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprikhodko.habrareader.categories.hubs.HubsPagingSource
import com.rprikhodko.habrareader.categories.hubs.data.HubPreview
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HubsViewModel @Inject constructor(
    private val useCase: GetHubsUseCase
) : ViewModel() {

    val hubs: Flow<PagingData<HubPreview>> = newPager().flow.cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, HubPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, HubPreview> {
        return HubsPagingSource(useCase)
    }
}