package com.rprihodko.habrareader.hub.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}