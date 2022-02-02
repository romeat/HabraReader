package com.rprihodko.habrareader.post.ui

import com.rprihodko.habrareader.common.dto.PostPage

sealed class PostUiState {
    object Loading: PostUiState()
    data class Success(val post: PostPage): PostUiState()
    data class Error(val message: String): PostUiState()
}