package com.rprikhodko.habrareader.hub.ui

import com.rprihodko.habrareader.common.dto.HubProfile

sealed class HubProfileUiState {
    object Loading: HubProfileUiState()
    data class Success(val hub: HubProfile): HubProfileUiState()
    data class Error(val message: String): HubProfileUiState()
}