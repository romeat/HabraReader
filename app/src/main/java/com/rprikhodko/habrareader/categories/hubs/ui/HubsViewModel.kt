package com.rprikhodko.habrareader.categories.hubs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprihodko.habrareader.common.dto.HubPreview
import com.rprikhodko.habrareader.categories.hubs.HubsPagingSource
import com.rprikhodko.habrareader.categories.hubs.OnHubListener
import com.rprikhodko.habrareader.categories.hubs.domain.GetHubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HubsViewModel @Inject constructor(
    private val useCase: GetHubsUseCase
) : ViewModel(), OnHubListener {

    val hubs: Flow<PagingData<HubPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun newPager(): Pager<Int, HubPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, HubPreview> {
        return HubsPagingSource(useCase)
    }

    override fun onHubClick(hub: HubPreview) {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToHub(hub.alias))
        }
    }
}

sealed class Event {
    class NavigateToHub(val hubAlias: String) : Event()
}