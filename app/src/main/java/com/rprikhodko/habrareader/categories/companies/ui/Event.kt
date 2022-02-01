package com.rprikhodko.habrareader.categories.companies.ui

sealed class Event {
    class NavigateToCompany(val companyAlias: String) : Event()
}