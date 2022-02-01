package com.rprikhodko.habrareader.post.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rprikhodko.habrareader.post.data.PostPage
import com.rprikhodko.habrareader.post.domain.GetPostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

//@HiltViewModel
class PostViewModel @AssistedInject constructor(
    @Assisted private val postId: Int,
    private val useCase: GetPostUseCase
) : ViewModel() {

    private val _postData = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val postData: StateFlow<PostUiState> = _postData

    init {
        viewModelScope.launch {
            try {
                handleResult(useCase.invoke(postId))
            }
            catch (e: Exception) {
                _postData.value = PostUiState.Error(e.message ?: "failed to load data")
            }
        }
    }

    private fun handleResult(response: Response<PostPage>) {
        if(response.isSuccessful) {
            val result = response.body()!!
            _postData.value = PostUiState.Success(result)
        } else {
            _postData.value = PostUiState.Error(HttpException(response).message())
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(postId: Int): PostViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            postId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(postId) as T
            }
        }
    }
}