package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

sealed interface Game {
    val id: Long
}

data class FreeGame(
    override val id: Long,
    val title: String,
    val thumbnailUrl: String,
    val description: String,
    val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    val releaseDate: ZonedDateTime,
    val gameProfileUrl: String
) : Game

data class RichInfoGamesPage(
    val count: Long,
    val nextPageUrl: String?,
    val previousPageUrl: String?,
    val games: List<RichInfoGame>
)

data class RichInfoGame(
    override val id: Long,
    val slug: String?,
    val name: String,
    val releaseDate: ZonedDateTime?,
    val toBeAnnounced: Boolean?,
    val imageUrl: String,
    val rating: Float,
    val metaCriticScore: Int?,
    val playtime: Int?,
    val suggestionsCount: Int,
    val platformsInfo: List<PlatformInfo>?,
    val storesInfo: List<StoreInfo>?,
    val tags: List<GameTag>,
    val screenshots: List<GameScreenshot>,
    val genres: List<GameGenre>
) : Game

data class PlatformInfo(
    val id: Int,
    val name: String,
    val slug: String,
    val imageUrl: String?,
    val yearEnd: Int?,
    val yearStart: Int?,
    val gamesCount: Long?,
    val imageBackground: String?,
    val releaseDate: ZonedDateTime?,
    val gameRequirementsInEnglish: GameRequirements?
)

data class GameRequirements(
    val minimum: MarkupText,
    val recommended: MarkupText
)

data class StoreInfo(
    val id: Int,
    val gameIdOnStore: Long?,
    val name: String,
    val slug: String?,
    val domain: String?,
    val gamesCount: Long?,
    val imageUrl: String?
)

data class GameTag(
    val id: Int,
    val name: String,
    val slug: String?,
    val language: String,
    val gamesCount: Long?,
    val imageUrl: String
)

data class GameScreenshot(val id: Long, val imageUrl: String)

data class GameGenre(
    val id: Int,
    val name: String,
    val slug: String?,
    val gamesCount: Long?,
    val imageUrl: String?
)
