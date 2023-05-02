package com.mr3y.ludi.core.repository.query

fun RichInfoGamesQuery(
    page: Int? = null,
    pageSize: Int? = null,
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
    sortingCriteria: RichInfoGamesSortingCriteria? = null
) = RichInfoGamesQueryParameters(page, pageSize, searchQuery, isFuzzinessEnabled, matchTermsExactly, parentPlatforms, platforms, stores, developers, genres, tags, dates, metaCriticScores, sortingCriteria)

data class RichInfoGamesQueryParameters(
    val page: Int?,
    val pageSize: Int?,
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
    val sortingCriteria: RichInfoGamesSortingCriteria?
) {
    companion object {
        val Empty = RichInfoGamesQueryParameters(
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
            null,
            null,
            null
        )
    }
}

enum class RichInfoGamesSortingCriteria {
    NameAscending, // A-Z
    NameDescending, // Z-A
    ReleasedAscending,
    ReleasedDescending,
    DateAddedAscending,
    DateAddedDescending,
    DateCreatedAscending,
    DateCreatedDescending,
    DateUpdatedAscending,
    DateUpdatedDescending,
    RatingAscending,
    RatingDescending,
    MetacriticScoreAscending,
    MetacriticScoreDescending
}

internal fun buildRichInfoGamesFullUrl(endpointUrl: String, queryParameters: RichInfoGamesQueryParameters): String {
    return buildString {
        append("$endpointUrl?")
        if (queryParameters.page != null) {
            append("page=${queryParameters.page}&")
        }
        if (queryParameters.pageSize != null) {
            append("page_size=${queryParameters.pageSize}&")
        }
        if (queryParameters.searchQuery != null) {
            append("search=${queryParameters.searchQuery}&")
        }
        if (queryParameters.isFuzzinessEnabled != null) {
            append("search_precise=${!queryParameters.isFuzzinessEnabled}&")
        }
        if (queryParameters.matchTermsExactly != null) {
            append("search_exact=${queryParameters.matchTermsExactly}&")
        }
        if (queryParameters.parentPlatforms != null) {
            append("parent_platforms=")
            queryParameters.parentPlatforms.forEachIndexed { index, parentPlatform ->
                if (index == queryParameters.parentPlatforms.lastIndex) {
                    append("$parentPlatform&")
                } else {
                    append("$parentPlatform,")
                }
            }
        }
        if (queryParameters.platforms != null) {
            append("platforms=")
            queryParameters.platforms.forEachIndexed { index, platform ->
                if (index == queryParameters.platforms.lastIndex) {
                    append("$platform&")
                } else {
                    append("$platform,")
                }
            }
        }
        if (queryParameters.stores != null) {
            append("stores=")
            queryParameters.stores.forEachIndexed { index, store ->
                if (index == queryParameters.stores.lastIndex) {
                    append("$store&")
                } else {
                    append("$store,")
                }
            }
        }
        if (queryParameters.developers != null) {
            append("developers=")
            queryParameters.developers.forEachIndexed { index, developer ->
                if (index == queryParameters.developers.lastIndex) {
                    append("$developer&")
                } else {
                    append("$developer,")
                }
            }
        }
        if (queryParameters.genres != null) {
            append("genres=")
            queryParameters.genres.forEachIndexed { index, genre ->
                if (index == queryParameters.genres.lastIndex) {
                    append("$genre&")
                } else {
                    append("$genre,")
                }
            }
        }
        if (queryParameters.tags != null) {
            append("tags=")
            queryParameters.tags.forEachIndexed { index, tag ->
                if (index == queryParameters.tags.lastIndex) {
                    append("$tag&")
                } else {
                    append("$tag,")
                }
            }
        }
        if (queryParameters.dates != null) {
            append("dates=")
            queryParameters.dates.forEachIndexed { index, date ->
                if (index == queryParameters.dates.lastIndex) {
                    append("$date&")
                } else {
                    append("$date,")
                }
            }
        }
        if (queryParameters.metaCriticScores != null) {
            append("metacritic=")
            queryParameters.metaCriticScores.forEachIndexed { index, score ->
                if (index == queryParameters.metaCriticScores.lastIndex) {
                    append("$score&")
                } else {
                    append("$score,")
                }
            }
        }
        if (queryParameters.sortingCriteria != null) {
            append("ordering=")
            when (queryParameters.sortingCriteria) {
                RichInfoGamesSortingCriteria.NameAscending -> append("name")
                RichInfoGamesSortingCriteria.NameDescending -> append("-name")
                RichInfoGamesSortingCriteria.ReleasedAscending -> append("released")
                RichInfoGamesSortingCriteria.ReleasedDescending -> append("-released")
                RichInfoGamesSortingCriteria.DateAddedAscending -> append("added")
                RichInfoGamesSortingCriteria.DateAddedDescending -> append("-added")
                RichInfoGamesSortingCriteria.DateCreatedAscending -> append("created")
                RichInfoGamesSortingCriteria.DateCreatedDescending -> append("-created")
                RichInfoGamesSortingCriteria.DateUpdatedAscending -> append("updated")
                RichInfoGamesSortingCriteria.DateUpdatedDescending -> append("-updated")
                RichInfoGamesSortingCriteria.RatingAscending -> append("rating")
                RichInfoGamesSortingCriteria.RatingDescending -> append("-rating")
                RichInfoGamesSortingCriteria.MetacriticScoreAscending -> append("metacritic")
                RichInfoGamesSortingCriteria.MetacriticScoreDescending -> append("-metacritic")
            }
            append('&')
        }
        deleteAt(lastIndex) // get rid of the temp '&' suffix or '?' if there are no query parameters
    }
}
