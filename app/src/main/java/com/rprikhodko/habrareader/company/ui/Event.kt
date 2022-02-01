package com.rprikhodko.habrareader.company.ui

sealed class Event {
    class NavigateToPost(val postId: Int) : Event()
}