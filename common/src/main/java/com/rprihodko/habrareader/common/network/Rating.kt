package com.rprihodko.habrareader.common.network

enum class Rating(val intValue: Int?) {
    AnyRating(null),
    ZeroPlus(0),
    TenPlus(10),
    TwentyFivePlus(25)
}