package com.mr3y.ludi.shared.core.network.datasources.internal

import com.mr3y.ludi.shared.core.network.fixtures.KtorClientForTesting
import com.mr3y.ludi.shared.core.network.fixtures.doCleanup
import com.mr3y.ludi.shared.core.network.fixtures.enqueueMockResponse
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.shared.core.network.model.GamerPowerGiveawayEntryStatus
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class GamerPowerDataSourceTest {

    private lateinit var sut: GamerPowerDataSourceImpl
    private val dispatcher = StandardTestDispatcher()
    private val client = KtorClientForTesting.getInstance(dispatcher)

    @Before
    fun setUp() {
        sut = GamerPowerDataSourceImpl(client)
    }

    @Test
    fun whenQueryingDataFromAPISuccessfully_dataIsProperlyDeserialized() = runTest(dispatcher) {
        // given an enqueued mocked response for querying giveaways
        val serializedResponse =
            """
                [
                  {
                    "id": 2301,
                    "title": "World of Warships - Starter Pack: Ishizuchi",
                    "worth": "$23.47",
                    "thumbnail": "https://www.gamerpower.com/offers/1/641df70fc9883.jpg",
                    "image": "https://www.gamerpower.com/offers/1b/641df70fc9883.jpg",
                    "description": "Claim your free World of Warships - Starter Pack and unlock the Premium battleship Ishizuchi, Japanese Tier IV, plus camouflages and bonuses.",
                    "instructions": "1- Download this DLC content directly via Epic Games Store before the offer expires.\r\n2 - Please note the base game World of Warships (free-to-play) is required to enjoy this content.",
                    "open_giveaway_url": "https://www.gamerpower.com/open/world-of-warships-starter-pack-ishizuchi",
                    "published_date": "2023-03-24 15:16:31",
                    "type": "DLC",
                    "platforms": "PC, Epic Games Store",
                    "end_date": "2023-03-30 23:59:00",
                    "users": 930,
                    "status": "Active",
                    "gamerpower_url": "https://www.gamerpower.com/world-of-warships-starter-pack-ishizuchi",
                    "open_giveaway": "https://www.gamerpower.com/open/world-of-warships-starter-pack-ishizuchi"
                  },
                  {
                    "id": 2300,
                    "title": "Spandex Force: Champion Rising",
                    "worth": "$9.99",
                    "thumbnail": "https://www.gamerpower.com/offers/1/641de37e3e819.jpg",
                    "image": "https://www.gamerpower.com/offers/1b/641de37e3e819.jpg",
                    "description": "Grab Spandex Force: Champion Rising for free and create your own hero today!",
                    "instructions": "1. Login into your free IndieGala account.\r\n2. Scroll down and click the \"Add to Your Library\" button to add the game to your library.\r\n3. Go to \"My Library\" to find your game.",
                    "open_giveaway_url": "https://www.gamerpower.com/open/spandex-force-champion-rising",
                    "published_date": "2023-03-24 13:53:02",
                    "type": "Game",
                    "platforms": "PC, DRM-Free",
                    "end_date": "N/A",
                    "users": 1480,
                    "status": "Active",
                    "gamerpower_url": "https://www.gamerpower.com/spandex-force-champion-rising",
                    "open_giveaway": "https://www.gamerpower.com/open/spandex-force-champion-rising"
                  },
                  {
                    "id": 1178,
                    "title": "Dungeons and Dragons Online: Free Quest Packs",
                    "worth": "N/A",
                    "thumbnail": "https://www.gamerpower.com/offers/1/61698e2ca90cf.jpg",
                    "image": "https://www.gamerpower.com/offers/1b/61698e2ca90cf.jpg",
                    "description": "Claim the code below and unlock lots of Dungeons and Dragons quest packs, raids, and adventure packs permanently on your account for free! This special code is available to redeem through April 23rd, 2023 so you will need to act fast!\r\n\r\nCode: DUNGEONCRAWL",
                    "instructions": "1. Redeem your code before April 23rd, 2023.\r\n2. Enjoy!",
                    "open_giveaway_url": "https://www.gamerpower.com/open/dungeons-and-dragons-online-all-quest-packs",
                    "published_date": "2023-03-24 13:46:12",
                    "type": "DLC",
                    "platforms": "PC",
                    "end_date": "2023-04-23 23:59:00",
                    "users": 3540,
                    "status": "Active",
                    "gamerpower_url": "https://www.gamerpower.com/dungeons-and-dragons-online-all-quest-packs",
                    "open_giveaway": "https://www.gamerpower.com/open/dungeons-and-dragons-online-all-quest-packs"
                  }
                ]
            """.trimIndent()

        client.enqueueMockResponse(serializedResponse, HttpStatusCode.OK)
        val expectedResponse = listOf(
            GamerPowerGiveawayEntry(
                2301,
                "World of Warships - Starter Pack: Ishizuchi",
                "$23.47",
                "https://www.gamerpower.com/offers/1/641df70fc9883.jpg",
                "https://www.gamerpower.com/offers/1b/641df70fc9883.jpg",
                "Claim your free World of Warships - Starter Pack and unlock the Premium battleship Ishizuchi, Japanese Tier IV, plus camouflages and bonuses.",
                "1- Download this DLC content directly via Epic Games Store before the offer expires.\r\n2 - Please note the base game World of Warships (free-to-play) is required to enjoy this content.",
                "https://www.gamerpower.com/open/world-of-warships-starter-pack-ishizuchi",
                "2023-03-24 15:16:31",
                "DLC",
                "PC, Epic Games Store",
                "2023-03-30 23:59:00",
                930,
                GamerPowerGiveawayEntryStatus.Active,
                "https://www.gamerpower.com/world-of-warships-starter-pack-ishizuchi"
            ),
            GamerPowerGiveawayEntry(
                2300,
                "Spandex Force: Champion Rising",
                "$9.99",
                "https://www.gamerpower.com/offers/1/641de37e3e819.jpg",
                "https://www.gamerpower.com/offers/1b/641de37e3e819.jpg",
                "Grab Spandex Force: Champion Rising for free and create your own hero today!",
                "1. Login into your free IndieGala account.\r\n2. Scroll down and click the \"Add to Your Library\" button to add the game to your library.\r\n3. Go to \"My Library\" to find your game.",
                "https://www.gamerpower.com/open/spandex-force-champion-rising",
                "2023-03-24 13:53:02",
                "Game",
                "PC, DRM-Free",
                null,
                1480,
                GamerPowerGiveawayEntryStatus.Active,
                "https://www.gamerpower.com/spandex-force-champion-rising"
            ),
            GamerPowerGiveawayEntry(
                1178,
                "Dungeons and Dragons Online: Free Quest Packs",
                null,
                "https://www.gamerpower.com/offers/1/61698e2ca90cf.jpg",
                "https://www.gamerpower.com/offers/1b/61698e2ca90cf.jpg",
                "Claim the code below and unlock lots of Dungeons and Dragons quest packs, raids, and adventure packs permanently on your account for free! This special code is available to redeem through April 23rd, 2023 so you will need to act fast!\r\n\r\nCode: DUNGEONCRAWL",
                "1. Redeem your code before April 23rd, 2023.\r\n2. Enjoy!",
                "https://www.gamerpower.com/open/dungeons-and-dragons-online-all-quest-packs",
                "2023-03-24 13:46:12",
                "DLC",
                "PC",
                "2023-04-23 23:59:00",
                3540,
                GamerPowerGiveawayEntryStatus.Active,
                "https://www.gamerpower.com/dungeons-and-dragons-online-all-quest-packs"
            )
        )

        // when trying to query latest giveaways
        val result = sut.queryLatestGiveaways("https://www.gamerpower.com/api/giveaways")

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<List<GamerPowerGiveawayEntry>>>()
        result as ApiResult.Success
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

        // when trying to query the latest giveaways
        val result = sut.queryLatestGiveaways("https://www.gamerpower.com/api/giveaways")

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
