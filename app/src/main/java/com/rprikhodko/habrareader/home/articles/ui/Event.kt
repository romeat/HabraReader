package com.rprikhodko.habrareader.home.articles.ui

sealed class Event {
    object RefreshAdapter: Event()
    class NavigateToPost(val postId: Int) : Event()
}