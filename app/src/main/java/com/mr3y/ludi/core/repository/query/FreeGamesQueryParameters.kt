package com.mr3y.ludi.core.repository.query

data class FreeGamesQueryParameters(
    val platform: FreeGamesPlatform?,
    val categories: List<FreeGamesCategory>?,
    val sortingCriteria: FreeGamesSortingCriteria?
)

enum class FreeGamesPlatform {
    all,
    pc,
    browser
}

enum class FreeGamesCategory {
    mmorpg,
    shooter,
    strategy,
    moba,
    racing,
    sports,
    social,
    sandbox,
    `open-world`,
    survival,
    pvp,
    pve,
    pixel,
    voxel,
    zombie,
    `turn-based`,
    `first-person`,
    `third-Person`,
    `top-down`,
    tank,
    space,
    sailing,
    `side-scroller`,
    superhero,
    permadeath,
    card,
    `battle-royale`,
    mmo,
    mmofps,
    mmotps,
    `3d`,
    `2d`,
    anime,
    fantasy,
    `sci-fi`,
    fighting,
    `action-rpg`,
    action,
    military,
    `martial-arts`,
    flight,
    `low-spec`,
    `tower-defense`,
    horror,
    mmorts
}

enum class FreeGamesSortingCriteria {
    alphabetical,
    `release-date`,
    popularity,
    relevance
}

internal fun buildFreeGamesFullUrl(endpointUrl: String, queryParameters: FreeGamesQueryParameters): String {
    return buildString {
        append("$endpointUrl?")
        val platform = queryParameters.platform
        val categories = queryParameters.categories
        val sortingCriteria = queryParameters.sortingCriteria
        if (platform != null) {
            append("platform=${platform.name}&")
        }
        if (categories != null) {
            when (categories.size) {
                1 -> append("category=${categories.single().name}&")
                else -> {
                    append("tag=")
                    categories.forEachIndexed { index, category ->
                        if (index == categories.lastIndex) {
                            append("${category.name}&")
                        } else {
                            append("${category.name}.")
                        }
                    }
                }
            }
        }
        if (sortingCriteria != null) {
            append("sort-by=${sortingCriteria.name}&")
        }
        deleteAt(lastIndex) // get rid of the temp '&' suffix or '?' if there are no query parameters
    }
}
