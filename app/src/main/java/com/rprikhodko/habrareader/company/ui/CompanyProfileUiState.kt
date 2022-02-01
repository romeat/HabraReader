package com.rprikhodko.habrareader.company.ui

import com.rprikhodko.habrareader.company.data.CompanyProfile

sealed class CompanyProfileUiState {
    object Loading: CompanyProfileUiState()
    data class Success(val profile: CompanyProfile): CompanyProfileUiState()
    data class Error(val message: String): CompanyProfileUiState()
}