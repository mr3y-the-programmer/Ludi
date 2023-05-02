package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.network.serialization.StringAsBooleanSerializer
import com.mr3y.ludi.core.network.serialization.StringAsDoubleSerializer
import com.mr3y.ludi.core.network.serialization.StringAsFloatSerializer
import com.mr3y.ludi.core.network.serialization.StringAsIntSerializer
import com.mr3y.ludi.core.network.serialization.StringAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheapSharkDeal(
    val internalName: String,
    val title: String,
    @SerialName("metacriticLink")
    val relativeMetacriticLink: String?,
    val dealID: String,
    @Serializable(with = StringAsIntSerializer::class)
    val storeID: Int,
    @Serializable(with = StringAsLongSerializer::class)
    val gameID: Long,
    @Serializable(with = StringAsFloatSerializer::class)
    val salePrice: Float,
    @Serializable(with = StringAsFloatSerializer::class)
    val normalPrice: Float,
    @Serializable(with = StringAsBooleanSerializer::class)
    val isOnSale: Boolean,
    @Serializable(with = StringAsDoubleSerializer::class)
    val savings: Double,
    @Serializable(with = StringAsIntSerializer::class)
    val metacriticScore: Int,
    val steamRatingText: String?,
    @Serializable(with = StringAsIntSerializer::class)
    val steamRatingPercent: Int,
    @Serializable(with = StringAsLongSerializer::class)
    val steamRatingCount: Long,
    @Serializable(with = StringAsLongSerializer::class)
    val steamAppID: Long?,
    @SerialName("releaseDate")
    val releaseDateTimestamp: Long,
    @SerialName("lastChange")
    val lastChangeTimestamp: Long,
    @Serializable(with = StringAsFloatSerializer::class)
    val dealRating: Float,
    @SerialName("thumb")
    val thumbnailUrl: String
)

fun CheapSharkDeal.toDeal(): Deal {
    return Deal(
        internalName = internalName,
        name = title,
        metacriticUrl = relativeMetacriticLink?.let { "https://www.metacritic.com$it" },
        dealID = dealID,
        storeID = storeID,
        gameID = gameID,
        salePriceInUsDollar = salePrice,
        normalPriceInUsDollar = normalPrice,
        isOnSale = isOnSale,
        savings = savings,
        metacriticScore = metacriticScore,
        steamRatingText = steamRatingText,
        steamRatingCount = steamRatingCount,
        steamRatingPercent = steamRatingPercent,
        steamAppID = steamAppID,
        releaseDate = releaseDateTimestamp.convertEpochSecondToZonedDateTime(),
        lastUpdate = lastChangeTimestamp.convertEpochSecondToZonedDateTime(),
        dealRating = dealRating,
        thumbnailUrl = thumbnailUrl
    )
}
