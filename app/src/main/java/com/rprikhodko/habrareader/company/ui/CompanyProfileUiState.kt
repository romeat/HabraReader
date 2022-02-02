package com.rprikhodko.habrareader.company.ui

import com.rprihodko.habrareader.common.dto.CompanyProfile

sealed class CompanyProfileUiState {
    object Loading: CompanyProfileUiState()
    data class Success(val profile: CompanyProfile): CompanyProfileUiState()
    data class Error(val message: String): CompanyProfileUiState()
}