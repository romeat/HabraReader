package com.rprihodko.habrareader.hub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprihodko.habrareader.common.CategoryPostsPagingSource
import com.rprihodko.habrareader.common.di.HubCategory
import com.rprihodko.habrareader.common.dto.PostPreview
import com.rprihodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprihodko.habrareader.common.interfaces.OnPostListener
import com.rprihodko.habrareader.common.dto.HubProfile
import com.rprihodko.habrareader.hub.domain.GetHubProfileUseCase
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

class HubViewModel  @AssistedInject constructor(
    private val hubUseCase: GetHubProfileUseCase,
    @HubCategory private val postsUseCase: GetPostsByCategoryUseCase,
    @Assisted private val hubAlias: String,
) : ViewModel(), OnPostListener {

    val posts: Flow<PagingData<PostPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val _hubProfile = MutableStateFlow<HubProfileUiState>(HubProfileUiState.Loading)
    val hubProfile: StateFlow<HubProfileUiState> = _hubProfile

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun newPager(): Pager<Int, PostPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, PostPreview> {
        return CategoryPostsPagingSource(postsUseCase, hubAlias)
    }

    init {
        viewModelScope.launch {
            try {
                handleResult(hubUseCase.invoke(hubAlias))
            }
            catch (e: Exception) {
                _hubProfile.value = HubProfileUiState.Error(e.message ?: "failed to load data")
            }
        }
    }

    private fun handleResult(response: Response<HubProfile>) {
        if(response.isSuccessful) {
            val result = response.body()!!
            _hubProfile.value = HubProfileUiState.Success(result)
        } else {
            _hubProfile.value = HubProfileUiState.Error(HttpException(response).message())
        }
    }

    override fun onPostClick(post: PostPreview) {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToPost(post.id))
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(hubAlias: String): HubViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            hubAlias: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(hubAlias) as T
            }
        }
    }
}

sealed class HubProfileUiState {
    object Loading: HubProfileUiState()
    data class Success(val hub: HubProfile): HubProfileUiState()
    data class Error(val message: String): HubProfileUiState()
}

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}