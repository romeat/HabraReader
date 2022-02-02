package com.rprikhodko.habrareader.categories.authors.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprihodko.habrareader.common.dto.AuthorPreview
import com.rprikhodko.habrareader.categories.authors.AuthorsPagingSource
import com.rprikhodko.habrareader.categories.authors.OnAuthorListener
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorsViewModel @Inject constructor(
    private val useCase: GetAuthorsUseCase
) : ViewModel(), OnAuthorListener {

    val authors: Flow<PagingData<AuthorPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun newPager(): Pager<Int, AuthorPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, AuthorPreview> {
        return AuthorsPagingSource(useCase)
    }

    override fun onAuthorClick(author: AuthorPreview) {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToAuthor(author.alias))
        }
    }
}