package com.mr3y.ludi.ui.screens.onboarding

import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.presenter.model.OnboardingGames
import com.mr3y.ludi.ui.presenter.model.OnboardingState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper

val PreviewAllGenres = Result.Success(
    ResourceWrapper.ActualResource(
        setOf(
            GameGenre(
                id = 1,
                name = "Action",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 2,
                name = "Adventure",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 3,
                name = "Arcade",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 4,
                name = "Board games",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 5,
                name = "Educational",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 6,
                name = "Family",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 7,
                name = "Indie",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 8,
                name = "Massively Multiplayer",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 9,
                name = "Racing",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
            GameGenre(
                id = 10,
                name = "Simulation",
                slug = null,
                gamesCount = 2000,
                imageUrl = null
            ),
        )
    )
)

val PreviewSelectedGenres = setOf(
    GameGenre(
        id = 3,
        name = "Arcade",
        slug = null,
        gamesCount = 2000,
        imageUrl = null
    ),
    GameGenre(
        id = 8,
        name = "Massively Multiplayer",
        slug = null,
        gamesCount = 2000,
        imageUrl = null
    ),
    GameGenre(
        id = 9,
        name = "Racing",
        slug = null,
        gamesCount = 2000,
        imageUrl = null
    ),
)

val PreviewOnboardingGames = OnboardingGames.SuggestedGames(Result.Success(listOf(ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder, ResourceWrapper.Placeholder)))

val PreviewNewsDataSources = listOf(
    NewsDataSource("Game spot", R.drawable.game_spot_logo, Source.GameSpot),
    NewsDataSource("Giant bomb", R.drawable.giant_bomb_logo, Source.GiantBomb),
    NewsDataSource("IGN", R.drawable.ign_logo, Source.IGN),
    NewsDataSource("MMO bomb", R.drawable.mmobomb_logo, Source.MMOBomb),
    NewsDataSource("Tech Radar", R.drawable.tech_radar_logo, Source.TechRadar)
)

val PreviewOnboardingState = OnboardingState(
    allNewsDataSources = PreviewNewsDataSources,
    followedNewsDataSources = emptyList(),
    isUpdatingFollowedNewsDataSources = false,
    searchQuery = "",
    onboardingGames = PreviewOnboardingGames,
    favouriteGames = emptyList(),
    isUpdatingFavouriteGames = false,
    allGamingGenres = PreviewAllGenres,
    selectedGamingGenres = PreviewSelectedGenres
)
