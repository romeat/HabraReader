package com.rprikhodko.habrareader.hub.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}