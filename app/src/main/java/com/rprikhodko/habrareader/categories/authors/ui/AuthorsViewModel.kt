package com.rprikhodko.habrareader.categories.authors.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprikhodko.habrareader.categories.authors.AuthorsPagingSource
import com.rprikhodko.habrareader.categories.authors.data.AuthorPreview
import com.rprikhodko.habrareader.categories.authors.domain.GetAuthorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AuthorsViewModel @Inject constructor(
    private val useCase: GetAuthorsUseCase
) : ViewModel() {

    val authors: Flow<PagingData<AuthorPreview>> = newPager().flow.cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, AuthorPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, AuthorPreview> {
        return AuthorsPagingSource(useCase)
    }
}