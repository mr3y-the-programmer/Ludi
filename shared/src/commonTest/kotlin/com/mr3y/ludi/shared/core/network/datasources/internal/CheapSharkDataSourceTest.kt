package com.mr3y.ludi.shared.core.network.datasources.internal

import com.mr3y.ludi.shared.core.network.fixtures.KtorClientForTesting
import com.mr3y.ludi.shared.core.network.fixtures.doCleanup
import com.mr3y.ludi.shared.core.network.fixtures.enqueueMockResponse
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.CheapSharkDeal
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class CheapSharkDataSourceTest {

    private lateinit var sut: CheapSharkDataSourceImpl
    private val dispatcher = StandardTestDispatcher()
    private val client = KtorClientForTesting.getInstance(dispatcher)

    @Before
    fun setUp() {
        sut = CheapSharkDataSourceImpl(client)
    }

    @Test
    fun whenQueryingDataFromAPISuccessfully_dataIsProperlyDeserialized() = runTest(dispatcher) {
        // given an enqueued mocked response for querying deals
        val serializedResponse =
            """
                [
                  {
                    "internalName": "DEUSEXHUMANREVOLUTIONDIRECTORSCUT",
                    "title": "Deus Ex: Human Revolution - Director's Cut",
                    "metacriticLink": "/game/pc/deus-ex-human-revolution---directors-cut",
                    "dealID": "HhzMJAgQYGZ%2B%2BFPpBG%2BRFcuUQZJO3KXvlnyYYGwGUfU%3D",
                    "storeID": "1",
                    "gameID": "102249",
                    "salePrice": "2.99",
                    "normalPrice": "19.99",
                    "isOnSale": "1",
                    "savings": "85.042521",
                    "metacriticScore": "91",
                    "steamRatingText": "Very Positive",
                    "steamRatingPercent": "92",
                    "steamRatingCount": "17993",
                    "steamAppID": "238010",
                    "releaseDate": 1382400000,
                    "lastChange": 1621536418,
                    "dealRating": "9.6",
                    "thumb": "https://cdn.cloudflare.steamstatic.com/steam/apps/238010/capsule_sm_120.jpg?t=1619788192"
                  },
                  {
                    "internalName": "THIEFDEADLYSHADOWS",
                    "title": "Thief: Deadly Shadows",
                    "metacriticLink": "/game/pc/thief-deadly-shadows",
                    "dealID": "EX0oH20b7A1H2YiVjvVx5A0HH%2F4etw3x%2F6YMGVPpKbA%3D",
                    "storeID": "1",
                    "gameID": "396",
                    "salePrice": "0.98",
                    "normalPrice": "8.99",
                    "isOnSale": "1",
                    "savings": "89.098999",
                    "metacriticScore": "85",
                    "steamRatingText": "Very Positive",
                    "steamRatingPercent": "81",
                    "steamRatingCount": "1670",
                    "steamAppID": "6980",
                    "releaseDate": 1085443200,
                    "lastChange": 1621540561,
                    "dealRating": "9.4",
                    "thumb": "https://cdn.cloudflare.steamstatic.com/steam/apps/6980/capsule_sm_120.jpg?t=1592493801"
                  },
                  {
                    "internalName": "CYBERNETICAFINAL",
                    "title": "Cybernetica: Final",
                    "metacriticLink": "/game/pc/cybernetica-final",
                    "dealID": "YAm7vyXnmK5YWsFyTy22SFn5p%2F4Lrpu2eCCrrfvNO44%3D",
                    "storeID": "1",
                    "gameID": "226893",
                    "salePrice": "1.89",
                    "normalPrice": "18.99",
                    "isOnSale": "1",
                    "savings": "90.047393",
                    "metacriticScore": "0",
                    "steamRatingText": null,
                    "steamRatingPercent": "0",
                    "steamRatingCount": "0",
                    "steamAppID": "1549710",
                    "releaseDate": 1614643200,
                    "lastChange": 1621359373,
                    "dealRating": "9.1",
                    "thumb": "https://cdn.cloudflare.steamstatic.com/steam/apps/1549710/capsule_sm_120.jpg?t=1614708882"
                  }
                ]
            """.trimIndent()
        client.enqueueMockResponse(serializedResponse, HttpStatusCode.OK)
        val expectedResponse = listOf(
            CheapSharkDeal(
                "DEUSEXHUMANREVOLUTIONDIRECTORSCUT",
                "Deus Ex: Human Revolution - Director's Cut",
                "/game/pc/deus-ex-human-revolution---directors-cut",
                "HhzMJAgQYGZ%2B%2BFPpBG%2BRFcuUQZJO3KXvlnyYYGwGUfU%3D",
                1,
                102249,
                2.99f,
                19.99f,
                true,
                85.042521,
                91,
                "Very Positive",
                92,
                17993,
                238010,
                1382400000,
                1621536418,
                9.6f,
                "https://cdn.cloudflare.steamstatic.com/steam/apps/238010/capsule_sm_120.jpg?t=1619788192"
            ),
            CheapSharkDeal(
                "THIEFDEADLYSHADOWS",
                "Thief: Deadly Shadows",
                "/game/pc/thief-deadly-shadows",
                "EX0oH20b7A1H2YiVjvVx5A0HH%2F4etw3x%2F6YMGVPpKbA%3D",
                1,
                396,
                0.98f,
                8.99f,
                true,
                89.098999,
                85,
                "Very Positive",
                81,
                1670,
                6980,
                1085443200,
                1621540561,
                9.4f,
                "https://cdn.cloudflare.steamstatic.com/steam/apps/6980/capsule_sm_120.jpg?t=1592493801"
            ),
            CheapSharkDeal(
                "CYBERNETICAFINAL",
                "Cybernetica: Final",
                "/game/pc/cybernetica-final",
                "YAm7vyXnmK5YWsFyTy22SFn5p%2F4Lrpu2eCCrrfvNO44%3D",
                1,
                226893,
                1.89f,
                18.99f,
                true,
                90.047393,
                0,
                null,
                0,
                0,
                1549710,
                1614643200,
                1621359373,
                9.1f,
                "https://cdn.cloudflare.steamstatic.com/steam/apps/1549710/capsule_sm_120.jpg?t=1614708882"
            )
        )

        // when trying to query latest deals
        val result = sut.queryLatestDeals("https://www.cheapshark.com/api/1.0/deals")

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<List<CheapSharkDeal>>>()
        result as ApiResult.Success<List<CheapSharkDeal>>
        expectThat(result.data).isA<List<CheapSharkDeal>>()
        expectThat(result.data).isEqualTo(expectedResponse)
    }

    @Test
    fun `whenThingsDon'tGoAsExpected_failureIsWrapped`() = runTest(dispatcher) {
        // given an enqueued 404 error response
        val mockResponse =
            """
                    {
                        "status": 0,
                        "status_message": "Bad query request!"
                    }
            """.trimIndent()
        client.enqueueMockResponse(mockResponse, HttpStatusCode.NotFound)

        // when trying to query the latest deals
        val result = sut.queryLatestDeals("https://www.cheapshark.com/api/1.0/deals")

        // then expect the result is HttpFailure
        expectThat(result).isA<ApiResult.Error>()
        result as ApiResult.Error
        expectThat(result.code).isEqualTo(HttpStatusCode.NotFound.value)
    }

    @After
    fun teardown() {
        client.doCleanup()
    }
}
