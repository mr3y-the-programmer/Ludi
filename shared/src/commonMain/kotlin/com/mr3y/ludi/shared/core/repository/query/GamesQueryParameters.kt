package com.mr3y.ludi.shared.core.repository.query

fun GamesQuery(
    searchQuery: String? = null,
    isFuzzinessEnabled: Boolean? = null,
    matchTermsExactly: Boolean? = null,
    parentPlatforms: List<Int>? = null,
    platforms: List<Int>? = null,
    stores: List<Int>? = null,
    developers: List<String>? = null,
    genres: List<Int>? = null,
    tags: List<Int>? = null,
    dates: List<String>? = null,
    metaCriticScores: List<Int>? = null,
    sortingCriteria: GamesSortingCriteria? = null
) = GamesQueryParameters(searchQuery, isFuzzinessEnabled, matchTermsExactly, parentPlatforms, platforms, stores, developers, genres, tags, dates, metaCriticScores, sortingCriteria)

data class GamesQueryParameters(
    val searchQuery: String?,
    val isFuzzinessEnabled: Boolean?,
    val matchTermsExactly: Boolean?,
    val parentPlatforms: List<Int>?,
    val platforms: List<Int>?,
    val stores: List<Int>?,
    val developers: List<String>?,
    val genres: List<Int>?,
    val tags: List<Int>?,
    val dates: List<String>?,
    val metaCriticScores: List<Int>?,
    val sortingCriteria: GamesSortingCriteria?
) {
    companion object {
        val Empty = GamesQueryParameters(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}

enum class GamesSortingCriteria(val apiName: String) {
    NameAscending("name"), // A-Z
    NameDescending("-name"), // Z-A
    ReleasedAscending("released"),
    ReleasedDescending("-released"),
    DateAddedAscending("added"),
    DateAddedDescending("-added"),
    DateCreatedAscending("created"),
    DateCreatedDescending("-created"),
    DateUpdatedAscending("updated"),
    DateUpdatedDescending("-updated"),
    RatingAscending("rating"),
    RatingDescending("-rating"),
    MetacriticScoreAscending("metacritic"),
    MetacriticScoreDescending("-metacritic")
}
