package com.rprikhodko.habrareader.home.news.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprihodko.habrareader.common.dto.PostPreview
import com.rprikhodko.habrareader.common.interfaces.OnPostListener
import com.rprikhodko.habrareader.home.news.NewsPagingSource
import com.rprikhodko.habrareader.home.news.domain.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCase: GetNewsUseCase
) : ViewModel(), OnPostListener {

    val news: Flow<PagingData<PostPreview>> = newPager().flow.cachedIn(viewModelScope)

    private fun newPager(): Pager<Int, PostPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, PostPreview> {
        return NewsPagingSource(useCase)
    }

    override fun onPostClick(post: PostPreview) {

    }
}