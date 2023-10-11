package com.mr3y.ludi.shared.core.repository.query

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class URLQueryBuilderTest {

    @Test
    fun givenSomeQueryParametersWithRichInfoGamesEndPointUrl_thenVerifyTheResultingUrlMatchingExpectedOne() {
        val endPointUrl = "https://api.rawg.io/api/games"
        val queryParameters = listOf(
            GamesQueryParameters.Empty.copy(isFuzzinessEnabled = true, platforms = listOf(18, 1, 7), dates = listOf("2019-09-01", "2019-09-30"), sortingCriteria = GamesSortingCriteria.NameAscending),
            GamesQueryParameters.Empty,
            GamesQueryParameters.Empty.copy(pageSize = 50, searchQuery = "fall", matchTermsExactly = true, stores = listOf(5, 6), tags = listOf(31, 7), sortingCriteria = GamesSortingCriteria.RatingDescending)
        )
        val result = queryParameters.map { buildGamesFullUrl(endPointUrl, it) }
        val expected = listOf(
            "https://api.rawg.io/api/games?search_precise=false&platforms=18,1,7&dates=2019-09-01,2019-09-30&ordering=name",
            "https://api.rawg.io/api/games",
            "https://api.rawg.io/api/games?page_size=50&search=fall&search_exact=true&stores=5,6&tags=31,7&ordering=-rating"
        )
        expectThat(result).isEqualTo(expected)
    }

    @Test
    fun givenSomeQueryParametersForDealsEndPointUrl_thenVerifyTheResultingUrlMatchingExpectedOne() {
        val endPointUrl = "https://www.cheapshark.com/api/1.0/deals"
        val queryParameters = listOf(
            DealsQuery(searchQuery = "lo", matchTermsExactly = false, stores = listOf(1, 2, 3), sortingCriteria = DealsSorting.Recent, sortingDirection = DealsSortingDirection.Descending),
            DealsQuery()
        )
        val result = queryParameters.map { buildDealsFullUrl(endPointUrl, it) }
        val expected = listOf(
            "https://www.cheapshark.com/api/1.0/deals?title=lo&exact=0&storeID=1,2,3&sortBy=Recent&desc=1",
            "https://www.cheapshark.com/api/1.0/deals"
        )
        expectThat(result).isEqualTo(expected)
    }

    @Test
    fun givenSomeQueryParametersForGiveawaysEndPointUrl_thenVerifyTheResultingUrlMatchingExpectedOne() {
        val endPointUrl = "https://www.gamerpower.com/api/filter"
        val queryParameters = listOf(
            GiveawaysQueryParameters(platforms = listOf(GiveawayPlatform.Android, GiveawayPlatform.EpicGames, GiveawayPlatform.Steam), sorting = GiveawaysSorting.Popularity),
            GiveawaysQueryParameters(platforms = listOf(GiveawayPlatform.Android, GiveawayPlatform.EpicGames, GiveawayPlatform.Steam), sorting = null),
            GiveawaysQueryParameters(sorting = GiveawaysSorting.Date, platforms = null),
            GiveawaysQueryParameters(null, null)
        )
        val result = queryParameters.map { buildGiveawaysFullUrl(endPointUrl, it) }
        val expected = listOf(
            "https://www.gamerpower.com/api/filter?platform=android.epic-games-store.steam&sort-by=popularity",
            "https://www.gamerpower.com/api/filter?platform=android.epic-games-store.steam",
            "https://www.gamerpower.com/api/filter",
            "https://www.gamerpower.com/api/filter"
        )
        expectThat(result).isEqualTo(expected)
    }
}