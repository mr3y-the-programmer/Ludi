package com.mr3y.ludi.ui.presenter.usecases.utils

import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.core.model.GameGenre

internal fun List<Game>.groupByGenre(): Map<GameGenre, List<Game>> {
    if (isEmpty()) {
        return emptyMap()
    }
    val otherGenreGames = mutableListOf<Game>()
    val otherGenre = GameGenre(
        id = Int.MAX_VALUE,
        name = "Other",
        slug = "other",
        gamesCount = Long.MAX_VALUE,
        imageUrl = null
    )
    val alreadyAssociated = mutableListOf<Game>()
    val genres = this.mapNotNull { it.genres.firstOrNull() }.toSet()
    if (genres.isEmpty()) {
        return mapOf(otherGenre.copy(gamesCount = size.toLong()) to this)
    }
    val gamesGroupedByGenre = genres.associateWith { genre ->
        (this - alreadyAssociated.toSet()).filter {
            val gameFirstGenre = it.genres.firstOrNull()
            if (gameFirstGenre != null) {
                val matched = gameFirstGenre == genre
                if (matched) {
                    alreadyAssociated += it
                }
                matched
            } else {
                otherGenreGames += it
                return@filter false
            }
        }
    }
    return if (otherGenreGames.isNotEmpty()) {
        gamesGroupedByGenre + Pair(otherGenre, otherGenreGames)
    } else {
        gamesGroupedByGenre
    }
}
