package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.GameRequirements
import com.mr3y.ludi.core.model.GameScreenshot
import com.mr3y.ludi.core.model.GameTag
import com.mr3y.ludi.core.model.MarkupText
import com.mr3y.ludi.core.model.PlatformInfo
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.core.model.RichInfoGamesPage
import com.mr3y.ludi.core.model.StoreInfo
import com.mr3y.ludi.core.network.serialization.RAWGPlatformSerializer
import com.mr3y.ludi.core.network.serialization.RAWGStoreSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RAWGPage(
    val count: Long,
    @SerialName("next")
    val nextPageUrl: String?,
    @SerialName("previous")
    val previousPageUrl: String?,
    val results: List<RAWGShallowGame>
)

@Serializable
data class RAWGShallowGame(
    val id: Long?,
    val slug: String?,
    val name: String?,
    @SerialName("released")
    val releaseDate: String?,
    @SerialName("tba")
    val toBeAnnounced: Boolean?,
    @SerialName("background_image")
    val imageUrl: String?,
    val rating: Float,
    @SerialName("metacritic")
    val metaCriticScore: Int?,
    val playtime: Int?,
    @SerialName("suggestions_count")
    val suggestionsCount: Int?,
    val platforms: List<RAWGPlatformInfo>?,
    val stores: List<RAWGStoreInfo>?,
    val tags: List<RAWGGameTag>,
    @SerialName("short_screenshots")
    val screenshots: List<RAWGGameScreenshot>,
    val genres: List<RAWGGameGenre>
)

@Serializable
data class RAWGGameRate(
    val id: Long,
    val title: String,
    val count: Long,
    val percent: Float
)

@Serializable
data class AddedByStatus(
    @SerialName("yet")
    val notAddedYet: Long,
    val owned: Long,
    val beaten: Long,
    val toplay: Long,
    val dropped: Long,
    val playing: Long
)

@Serializable
data class RAWGGameTag(
    val id: Int?,
    val name: String,
    val slug: String?,
    val language: String?,
    @SerialName("games_count")
    val gamesCount: Long?,
    @SerialName("image_background")
    val imageUrl: String?
)

@Serializable
data class RAWGGameScreenshot(
    val id: Long?,
    @SerialName("image")
    val imageUrl: String?
)

@Serializable
data class RAWGGameGenre(
    val id: Int?,
    val name: String,
    val slug: String?,
    @SerialName("games_count")
    val gamesCount: Long? = null,
    @SerialName("image_background")
    val imageUrl: String? = null
)

@Serializable(with = RAWGPlatformSerializer::class)
sealed class RAWGPlatformInfo {
    abstract val platform: RAWGPlatformProperties
}

@Serializable
data class ShallowRAWGPlatformInfo(
    override val platform: RAWGPlatformProperties
) : RAWGPlatformInfo()

@Serializable
data class DetailedRAWGPlatformInfo(
    override val platform: RAWGPlatformProperties,
    @SerialName("released_at")
    val releaseDate: String?,
    @SerialName("requirements_en")
    val requirementsInEnglish: RAWGPlatformRequirements?
) : RAWGPlatformInfo()

@Serializable
data class RAWGPlatformRequirements(
    val minimum: String,
    val recommended: String
)

@Serializable
data class RAWGPlatformProperties(
    val id: Int,
    val name: String,
    val slug: String,
    @SerialName("image")
    val imageUrl: String? = null,
    @SerialName("year_end")
    val yearEnd: Int? = null,
    @SerialName("year_start")
    val yearStart: Int? = null,
    @SerialName("games_count")
    val gamesCount: Long? = null,
    @SerialName("image_background")
    val imageBackground: String? = null
)

@Serializable(with = RAWGStoreSerializer::class)
sealed class RAWGStoreInfo {
    abstract val store: RAWGStoreProperties
}

@Serializable
data class ShallowRAWGStoreInfo(override val store: RAWGStoreProperties) : RAWGStoreInfo()

@Serializable
data class ShallowRAWGStoreInfoWithId(val id: Long, override val store: RAWGStoreProperties) : RAWGStoreInfo()

@Serializable
data class RAWGStoreProperties(
    val id: Int?,
    val name: String,
    val slug: String?,
    val domain: String? = null,
    @SerialName("games_count")
    val gamesCount: Long? = null,
    @SerialName("image_background")
    val imageUrl: String? = null
)

fun RAWGShallowGame.toRichInfoGame(): RichInfoGame? {
    return RichInfoGame(
        id = id ?: return null,
        name = name ?: return null,
        slug = slug,
        releaseDate = releaseDate?.toZonedDate(),
        toBeAnnounced = toBeAnnounced,
        imageUrl = imageUrl ?: return null,
        rating = rating,
        metaCriticScore = metaCriticScore,
        playtime = playtime,
        suggestionsCount = suggestionsCount ?: return null,
        platformsInfo = platforms?.map { platformInfo ->
            val properties = platformInfo.platform
            when (platformInfo) {
                is ShallowRAWGPlatformInfo -> {
                    PlatformInfo(
                        id = properties.id,
                        name = properties.name,
                        slug = properties.slug,
                        imageUrl = properties.imageUrl,
                        yearStart = properties.yearStart,
                        yearEnd = properties.yearEnd,
                        gamesCount = properties.gamesCount,
                        imageBackground = properties.imageBackground,
                        releaseDate = null,
                        gameRequirementsInEnglish = null
                    )
                }
                is DetailedRAWGPlatformInfo -> {
                    PlatformInfo(
                        id = properties.id,
                        name = properties.name,
                        slug = properties.slug,
                        imageUrl = properties.imageUrl,
                        yearStart = properties.yearStart,
                        yearEnd = properties.yearEnd,
                        gamesCount = properties.gamesCount,
                        imageBackground = properties.imageBackground,
                        releaseDate = platformInfo.releaseDate?.toZonedDate(),
                        gameRequirementsInEnglish = platformInfo.requirementsInEnglish?.let { requirements ->
                            GameRequirements(
                                minimum = MarkupText(requirements.minimum),
                                recommended = MarkupText(requirements.recommended)
                            )
                        }
                    )
                }
            }
        },
        storesInfo = stores?.map { storeInfo ->
            val properties = storeInfo.store
            when (storeInfo) {
                is ShallowRAWGStoreInfo -> {
                    StoreInfo(
                        id = properties.id ?: return@map null,
                        gameIdOnStore = null,
                        name = properties.name,
                        slug = properties.slug,
                        domain = properties.domain,
                        gamesCount = properties.gamesCount,
                        imageUrl = properties.imageUrl
                    )
                }
                is ShallowRAWGStoreInfoWithId -> {
                    StoreInfo(
                        id = properties.id ?: return@map null,
                        gameIdOnStore = storeInfo.id,
                        name = properties.name,
                        slug = properties.slug,
                        domain = properties.domain,
                        gamesCount = properties.gamesCount,
                        imageUrl = properties.imageUrl
                    )
                }
            }
        }?.filterNotNull(),
        tags = tags.map { gameTag ->
            GameTag(
                id = gameTag.id ?: return@map null,
                name = gameTag.name,
                slug = gameTag.slug,
                language = gameTag.language ?: return@map null,
                gamesCount = gameTag.gamesCount,
                imageUrl = gameTag.imageUrl ?: return@map null
            )
        }.filterNotNull(),
        screenshots = screenshots.map { screenshot ->
            GameScreenshot(
                id = screenshot.id ?: return@map null,
                imageUrl = screenshot.imageUrl ?: return@map null
            )
        }.filterNotNull(),
        genres = genres.map { genre ->
            GameGenre(
                id = genre.id ?: return@map null,
                name = genre.name,
                slug = genre.slug,
                gamesCount = genre.gamesCount,
                imageUrl = genre.imageUrl
            )
        }.filterNotNull()
    )
}

fun RAWGPage.toRichInfoGamesPage(): RichInfoGamesPage {
    return RichInfoGamesPage(
        count = count,
        nextPageUrl = nextPageUrl,
        previousPageUrl = previousPageUrl,
        games = results.mapNotNull(RAWGShallowGame::toRichInfoGame)
    )
}
