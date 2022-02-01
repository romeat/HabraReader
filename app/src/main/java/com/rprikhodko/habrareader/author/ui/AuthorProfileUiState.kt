package com.rprikhodko.habrareader.author.ui

import com.rprikhodko.habrareader.author.data.AuthorProfile

sealed class AuthorProfileUiState {
    object Loading: AuthorProfileUiState()
    data class Success(val profile: AuthorProfile): AuthorProfileUiState()
    data class Error(val message: String): AuthorProfileUiState()
}