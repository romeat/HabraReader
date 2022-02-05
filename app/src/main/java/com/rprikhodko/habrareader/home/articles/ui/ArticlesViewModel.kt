package com.rprikhodko.habrareader.home.articles.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprikhodko.habrareader.home.articles.domain.PostsRepository
import com.rprihodko.habrareader.common.dto.PostPreview
import com.rprihodko.habrareader.common.network.Period
import com.rprihodko.habrareader.common.network.Rating
import com.rprihodko.habrareader.common.network.SortBy
import com.rprihodko.habrareader.common.interfaces.OnPostListener
import com.rprikhodko.habrareader.home.articles.ArticlesPagingSource
import com.rprikhodko.habrareader.home.articles.ui.Event.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repository: PostsRepository
) : ViewModel(), OnPostListener {

    val filtersState: LiveData<FiltersViewState>
        get() = _filtersState
    private val _filtersState = MutableLiveData<FiltersViewState>().apply {
        value = FiltersViewState(
            toggleButton = false,
            toggleButtonText = "",
            sortBy = SortBy.Rating,
            sortByVisible = false,
            rating = Rating.AnyRating,
            ratingVisible = false,
            period = Period.Daily,
            periodVisible = false,
        )
    }

    val posts: Flow<PagingData<PostPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private lateinit var pagingSource: ArticlesPagingSource

    private fun newPager(): Pager<Int, PostPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, PostPreview> {
        pagingSource = ArticlesPagingSource(repository,
            ArticlesPagingSource.RepositoryParams(_filtersState.value!!.sortBy, _filtersState.value!!.period, _filtersState.value!!.rating))
            return pagingSource
    }

    fun onSortByRadio(sortBy: SortBy) {
        _filtersState.value = _filtersState.value!!.copy(
            sortBy = sortBy,
            ratingVisible = when(sortBy) {
                SortBy.Rating -> true
                SortBy.Period -> false
            },
            periodVisible = when(sortBy) {
                SortBy.Rating -> false
                SortBy.Period -> true
            }
        )
        onFiltersValueChange()
    }

    fun onRatingRadio(rating: Rating) {
        _filtersState.value = _filtersState.value!!.copy(rating = rating)
        onFiltersValueChange()
    }

    fun onPeriodRadio(period: Period) {
        _filtersState.value = _filtersState.value!!.copy(period = period)
        onFiltersValueChange()
    }

    fun onFiltersToggle() {
        if(_filtersState.value!!.toggleButton) {
            _filtersState.value = _filtersState.value!!.copy(
                sortByVisible = false,
                periodVisible = false,
                ratingVisible = false,
                toggleButton = false
            )
        } else {
            _filtersState.value = _filtersState.value!!.copy(
                sortByVisible = true,
                ratingVisible = when(_filtersState.value!!.sortBy) {
                    SortBy.Rating -> true
                    SortBy.Period -> false
                },
                periodVisible = when(_filtersState.value!!.sortBy) {
                    SortBy.Rating -> false
                    SortBy.Period -> true
                },
                toggleButton = true
            )
        }
    }

    private fun onFiltersValueChange() {
        viewModelScope.launch {
            eventChannel.send(RefreshAdapter) // actually just scrolls recyclerview to top (to page 1)
            delay(100) // let scroll complete (may be unnecessary)
            // reload page 1 but with new filter params
            // (if you just call invalidate() after N scrolled pages, source will reload all pages from page N back to page 1)
            pagingSource.invalidate()
        }
    }

    data class FiltersViewState(
        val toggleButton: Boolean,
        val toggleButtonText: String,
        val sortBy: SortBy,
        val sortByVisible: Boolean,
        val rating: Rating,
        val ratingVisible: Boolean,
        val period: Period,
        val periodVisible: Boolean,
    )

    override fun onPostClick(post: PostPreview) {
        viewModelScope.launch {
            eventChannel.send(NavigateToPost(post.id))
        }
    }
}