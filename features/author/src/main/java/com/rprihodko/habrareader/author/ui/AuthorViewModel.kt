package com.rprihodko.habrareader.author.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprihodko.habrareader.common.CategoryPostsPagingSource
import com.rprihodko.habrareader.common.dto.AuthorProfile
import com.rprihodko.habrareader.author.domain.GetAuthorProfileUseCase
import com.rprihodko.habrareader.common.di.AuthorCategory
import com.rprihodko.habrareader.common.dto.PostPreview
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.common.interfaces.OnPostListener
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class AuthorViewModel @AssistedInject constructor(
    private val profileUseCase: GetAuthorProfileUseCase,
    @AuthorCategory private val postsUseCase: GetPostsByCategoryUseCase,
    @Assisted private val authorAlias: String,
) : ViewModel(), OnPostListener {

    val posts: Flow<PagingData<PostPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val _authorProfile = MutableStateFlow<AuthorProfileUiState>(AuthorProfileUiState.Loading)
    val authorProfile: StateFlow<AuthorProfileUiState> = _authorProfile

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun newPager(): Pager<Int, PostPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, PostPreview> {
        return CategoryPostsPagingSource(postsUseCase, authorAlias)
    }

    init {
        viewModelScope.launch {
            try {
                handleResult(profileUseCase.invoke(authorAlias))
            }
            catch (e: Exception) {
                _authorProfile.value = AuthorProfileUiState.Error(e.message ?: "failed to load data")
            }
        }
    }

    private fun handleResult(response: Response<AuthorProfile>) {
        if(response.isSuccessful) {
            val result = response.body()!!
            _authorProfile.value = AuthorProfileUiState.Success(result)
        } else {
            _authorProfile.value = AuthorProfileUiState.Error(HttpException(response).message())
        }
    }

    override fun onPostClick(post: PostPreview) {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToPost(post.id))
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(authorAlias: String): AuthorViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            authorAlias: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(authorAlias) as T
            }
        }
    }
}