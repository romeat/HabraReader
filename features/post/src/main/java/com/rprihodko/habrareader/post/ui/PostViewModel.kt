package com.rprihodko.habrareader.post.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.post.domain.GetPostUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

const val IMAGE_CUSTOM_TAG = "habra-img"

class PostViewModel @AssistedInject constructor(
    @Assisted private val postId: Int,
    private val useCase: GetPostUseCase
) : ViewModel() {

    private val _postData = MutableStateFlow<PostUiState>(PostUiState.Loading)
    val postData: StateFlow<PostUiState> = _postData

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private var authorAlias = ""

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

    fun onAuthorClick() {
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToAuthor(authorAlias))
        }
    }

    private fun handleResult(response: Response<PostPage>) {
        if(response.isSuccessful) {
            val result = response.body()!!
            authorAlias = result.author.alias
            _postData.value = PostUiState.Success(result.copy(content = replaceTags(result.content)))
        } else {
            _postData.value = PostUiState.Error(HttpException(response).message())
        }
    }

    private fun replaceTags(content: String): String {

        // TODO StringBuilder replace does not work for some reason
        /*
        val sb = StringBuilder(content)
        sb.replace("<img ".toRegex(), "<$IMAGE_CUSTOM_TAG>")
        sb.replace("/><figcaption>".toRegex(), "</$IMAGE_CUSTOM_TAG><figcaption>")
        sb.replace("data-blurred=\"true\"/>".toRegex(), "</$IMAGE_CUSTOM_TAG>")
        return sb.toString()
         */

        with(content) {
            return this
                .replace("<img ", "<$IMAGE_CUSTOM_TAG>")
                .replace("/><figcaption>", "</$IMAGE_CUSTOM_TAG><figcaption>")
                .replace("data-blurred=\"true\"/>", "</$IMAGE_CUSTOM_TAG>")
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

sealed class Event {
    class NavigateToAuthor(val authorAlias: String) : Event()
}