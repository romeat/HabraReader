package com.rprihodko.habrareader.post.ui

import android.content.res.Resources
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.post.domain.GetPostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

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
            // replace fucking img tags before posting success
            //_postData.value = PostUiState.Success(result)//(result.copy(content = regexer(result.content)))
            _postData.value = PostUiState.Success(result.copy(content = replacer(result.content)))
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

    private fun replacer(content: String): String {
        var nc = content.replace("<img ", "<habra-img>")
        nc = nc.replace("/><figcaption>", "</habra-img><figcaption>")
        return nc.replace("data-blurred=\"true\"/>", "</habra-img>")
    }
}