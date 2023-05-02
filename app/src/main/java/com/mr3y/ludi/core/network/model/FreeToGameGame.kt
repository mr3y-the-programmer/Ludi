package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.FreeGame
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FreeToGameGame(
    val id: Long,
    val title: String,
    @SerialName("thumbnail")
    val thumbnailUrl: String,
    @SerialName("short_description")
    val description: String,
    @SerialName("game_url")
    val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("freetogame_profile_url")
    val gameProfileUrl: String
)

fun FreeToGameGame.toFreeGame(): FreeGame {
    return FreeGame(
        id,
        title,
        thumbnailUrl,
        description,
        gameUrl,
        genre,
        platform,
        publisher,
        developer,
        releaseDate.toZonedDate(),
        gameProfileUrl
    )
}
