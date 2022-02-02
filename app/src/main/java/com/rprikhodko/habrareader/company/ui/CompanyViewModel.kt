package com.rprikhodko.habrareader.company.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.rprikhodko.habrareader.author.ui.Event
import com.rprikhodko.habrareader.common.CategoryPostsPagingSource
import com.rprihodko.habrareader.common.dto.PostPreview
import com.rprikhodko.habrareader.common.interfaces.GetPostsByCategoryUseCase
import com.rprikhodko.habrareader.common.interfaces.OnPostListener
import com.rprihodko.habrareader.common.dto.CompanyProfile
import com.rprikhodko.habrareader.company.domain.GetCompanyProfileUseCase
import com.rprikhodko.habrareader.di.CompanyCategory
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

class CompanyViewModel @AssistedInject constructor(
    private val companyUseCase: GetCompanyProfileUseCase,
    @CompanyCategory private val postsUseCase: GetPostsByCategoryUseCase,
    @Assisted private val companyAlias: String,
) : ViewModel(), OnPostListener {

    val posts: Flow<PagingData<PostPreview>> = newPager().flow.cachedIn(viewModelScope)

    private val _companyProfile = MutableStateFlow<CompanyProfileUiState>(CompanyProfileUiState.Loading)
    val companyProfile: StateFlow<CompanyProfileUiState> = _companyProfile

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun newPager(): Pager<Int, PostPreview> {
        return Pager(config = PagingConfig(10, enablePlaceholders = false),
            pagingSourceFactory = { newSource() })
    }

    private fun newSource(): PagingSource<Int, PostPreview> {
        return CategoryPostsPagingSource(postsUseCase, companyAlias)
    }

    init {
        viewModelScope.launch {
            try {
                handleResult(companyUseCase.invoke(companyAlias))
            }
            catch (e: Exception) {
                _companyProfile.value = CompanyProfileUiState.Error(e.message ?: "failed to load data")
            }
        }
    }

    private fun handleResult(response: Response<CompanyProfile>) {
        if(response.isSuccessful) {
            val result = response.body()!!
            _companyProfile.value = CompanyProfileUiState.Success(result)
        } else {
            _companyProfile.value = CompanyProfileUiState.Error(HttpException(response).message())
        }
    }

    override fun onPostClick(post: PostPreview) {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToPost(post.id))
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(companyAlias: String): CompanyViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            companyAlias: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(companyAlias) as T
            }
        }
    }
}