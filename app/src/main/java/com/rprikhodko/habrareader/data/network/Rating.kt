package com.rprikhodko.habrareader.data.network

enum class Rating(val intValue: Int?) {
    AnyRating(null),
    ZeroPlus(0),
    TenPlus(10),
    TwentyFivePlus(25)
}