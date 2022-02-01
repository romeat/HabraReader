package com.rprikhodko.habrareader.post.ui

import com.rprikhodko.habrareader.post.data.PostPage

sealed class PostUiState {
    object Loading: PostUiState()
    data class Success(val post: PostPage): PostUiState()
    data class Error(val message: String): PostUiState()
}