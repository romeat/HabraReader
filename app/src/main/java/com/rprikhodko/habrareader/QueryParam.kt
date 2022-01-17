package com.rprikhodko.habrareader

import com.rprikhodko.habrareader.data.network.Period
import com.rprikhodko.habrareader.data.network.Rating
import com.rprikhodko.habrareader.data.network.SortBy

data class QueryParam(
    val sortBy: SortBy = SortBy.Period,
    val rating: Rating = Rating.AnyRating,
    val period: Period = Period.Daily
)