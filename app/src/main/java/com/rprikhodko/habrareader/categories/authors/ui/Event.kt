package com.rprikhodko.habrareader.categories.authors.ui

sealed class Event {
    class NavigateToAuthor(val authorAlias: String) : Event()
}