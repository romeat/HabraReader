package com.rprihodko.habrareader.author.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}