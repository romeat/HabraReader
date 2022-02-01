package com.rprikhodko.habrareader.author.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}