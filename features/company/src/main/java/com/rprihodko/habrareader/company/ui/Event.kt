package com.rprihodko.habrareader.company.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}