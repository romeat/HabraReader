package com.rprihodko.habrareader.author.ui

import com.rprihodko.habrareader.common.dto.AuthorProfile

sealed class AuthorProfileUiState {
    object Loading: AuthorProfileUiState()
    data class Success(val profile: AuthorProfile): AuthorProfileUiState()
    data class Error(val message: String): AuthorProfileUiState()
}