package com.rprikhodko.habrareader.categories.hubs.ui

sealed class Event {
    class NavigateToHub(val hubAlias: String) : Event()
}