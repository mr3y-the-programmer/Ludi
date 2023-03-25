package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.fixtures.RetrofitClientForTesting
import com.mr3y.ludi.core.network.model.FreeToGameGame
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.create
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class FreeToGameDataSourceTest {

    private lateinit var sut: FreeToGameDataSource
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        sut = RetrofitClientForTesting.getInstance(baseUrl = mockWebServer.url("/")).create()
    }

    @Test
    fun whenQueryingDataFromAPISuccessfully_dataIsProperlyTransformed() = runTest {
        // given an enqueued mocked response for querying games
        val mockResponse = MockResponse()
            .setBody(
                """
                    [
                        {
                            "id": 540,
                            "title": "Overwatch 2",
                            "thumbnail": "https://www.freetogame.com/g/540/thumbnail.jpg",
                            "short_description": "A hero-focused first-person team shooter from Blizzard Entertainment.",
                            "game_url": "https://www.freetogame.com/open/overwatch-2",
                            "genre": "Shooter",
                            "platform": "PC (Windows)",
                            "publisher": "Activision Blizzard",
                            "developer": "Blizzard Entertainment",
                            "release_date": "2022-10-04",
                            "freetogame_profile_url": "https://www.freetogame.com/overwatch-2"
                        },
                        {
                            "id": 521,
                            "title": "Diablo Immortal",
                            "thumbnail": "https://www.freetogame.com/g/521/thumbnail.jpg",
                            "short_description": "Built for mobile and also released on PC, Diablo Immortal fills in the gaps between Diablo II and III in an MMOARPG environment.",
                            "game_url": "https://www.freetogame.com/open/diablo-immortal",
                            "genre": "MMOARPG",
                            "platform": "PC (Windows)",
                            "publisher": "Blizzard Entertainment",
                            "developer": "Blizzard Entertainment",
                            "release_date": "2022-06-02",
                            "freetogame_profile_url": "https://www.freetogame.com/diablo-immortal"
                        },
                        {
                            "id": 517,
                            "title": "Lost Ark",
                            "thumbnail": "https://www.freetogame.com/g/517/thumbnail.jpg",
                            "short_description": "Smilegate’s free-to-play multiplayer ARPG is a massive adventure filled with lands waiting to be explored, people waiting to be met, and an ancient evil waiting to be destroyed.",
                            "game_url": "https://www.freetogame.com/open/lost-ark",
                            "genre": "ARPG",
                            "platform": "PC (Windows)",
                            "publisher": "Amazon Games",
                            "developer": "Smilegate RPG",
                            "release_date": "2022-02-11",
                            "freetogame_profile_url": "https://www.freetogame.com/lost-ark"
                        }
                    ]
                """.trimIndent()
            )

        val expectedDeserializedResponse = listOf(
            FreeToGameGame(
                540,
                "Overwatch 2",
                "https://www.freetogame.com/g/540/thumbnail.jpg",
                "A hero-focused first-person team shooter from Blizzard Entertainment.",
                "https://www.freetogame.com/open/overwatch-2",
                "Shooter",
                "PC (Windows)",
                "Activision Blizzard",
                "Blizzard Entertainment",
                "2022-10-04",
                "https://www.freetogame.com/overwatch-2"
            ),
            FreeToGameGame(
                521,
                "Diablo Immortal",
                "https://www.freetogame.com/g/521/thumbnail.jpg",
                "Built for mobile and also released on PC, Diablo Immortal fills in the gaps between Diablo II and III in an MMOARPG environment.",
                "https://www.freetogame.com/open/diablo-immortal",
                "MMOARPG",
                "PC (Windows)",
                "Blizzard Entertainment",
                "Blizzard Entertainment",
                "2022-06-02",
                "https://www.freetogame.com/diablo-immortal"
            ),
            FreeToGameGame(
                517,
                "Lost Ark",
                "https://www.freetogame.com/g/517/thumbnail.jpg",
                "Smilegate’s free-to-play multiplayer ARPG is a massive adventure filled with lands waiting to be explored, people waiting to be met, and an ancient evil waiting to be destroyed.",
                "https://www.freetogame.com/open/lost-ark",
                "ARPG",
                "PC (Windows)",
                "Amazon Games",
                "Smilegate RPG",
                "2022-02-11",
                "https://www.freetogame.com/lost-ark"
            ),
        )
        mockWebServer.enqueue(mockResponse)

        // when trying to query games
        val result = sut.queryGames(mockWebServer.url("/").toString())

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<List<FreeToGameGame>>>()
        result as ApiResult.Success
        expectThat(result.value).isEqualTo(expectedDeserializedResponse)
    }

    @Test
    fun `whenThingsDon'tGoAsExpected_failureIsWrapped`() = runTest {
        // given an enqueued 404 error response
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody(
                """
                    {
                        "status": 0,
                        "status_message": "No game found with that id"
                    }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        // when trying to get the latest news
        val result = sut.queryGames(mockWebServer.url("/").toString())

        // then expect the result is HttpFailure
        expectThat(result).isA<ApiResult.Failure<Unit>>()
        result as ApiResult.Failure
        expectThat(result).isA<ApiResult.Failure.HttpFailure<Unit>>()
        result as ApiResult.Failure.HttpFailure
        expectThat(result.code).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}