package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

data class Deal(
    val internalName: String,
    val name: String,
    val metacriticUrl: String?,
    val dealID: String,
    val storeID: Int,
    val gameID: Long,
    val salePriceInUsDollar: Float,
    val normalPriceInUsDollar: Float,
    val isOnSale: Boolean,
    val savings: Double,
    val metacriticScore: Int,
    val steamRatingText: String?,
    val steamRatingPercent: Int,
    val steamRatingCount: Long,
    val steamAppID: Long?,
    val releaseDate: ZonedDateTime,
    val lastUpdate: ZonedDateTime,
    val dealRating: Float,
    val thumbnailUrl: String
)
