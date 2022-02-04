package com.rprikhodko.habrareader.home.news.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}