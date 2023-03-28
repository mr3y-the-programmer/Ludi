package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.fixtures.RetrofitClientForTesting
import com.mr3y.ludi.core.network.model.MMOGamesArticle
import com.mr3y.ludi.core.network.model.MMOGiveawayEntry
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
class MMOGamesDataSourceTest {

    private lateinit var sut: MMOGamesDataSource
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        sut = RetrofitClientForTesting.getInstance(baseUrl = mockWebServer.url("/")).create()
    }

    @Test
    fun whenGettingDataFromAPISuccessfully_dataIsProperlyTransformed() = runTest {
        // given an enqueued mocked sample response
        val mockResponse = MockResponse()
            .setBody(
                """
                    [
                        {
                            "id": 133882,
                            "title": "Sony Must Reveal Trade Secrets To Microsoft In Ongoing Lawsuit",
                            "short_description": "The motion for disclosure, which includes platform exclusivity deals, was granted in February 2023.",
                            "thumbnail": "https://www.mmobomb.com/file/2023/3/xbox-playstation-218x150.jpg",
                            "main_image": "https://www.mmobomb.com/file/2023/3/xbox-playstation.jpg",
                            "article_content": "<p><img class=\"type:primaryImage aligncenter size-large\" src=\"https://www.mmobomb.com/file/2023/3/xbox-playstation-812x456.jpg\" alt=\"xbox playstation\" width=\"812\" height=\"456\" /></p>\r\n<p><a href=\"https://www.mmobomb.com/topic/sony\">Sony</a> is not cool with <a href=\"https://www.mmobomb.com/topic/microsoft\">Microsoft’s</a> proposed acquisition of Activision Blizzard, but this pushback means the giant Japanese video game and digital entertainment company <a href=\"https://www.ftc.gov/system/files/ftc_gov/pdf/607003_d09412_-_order_on_motion_of_sony_interactive_entertainment_llc_to_quash_or_limit_subpoena_duces_tecum.pdf\">must reveal numerous trade secrets</a> to its American rival.</p>\r\n<p>In January 2023, Microsoft subpoenaed Sony, requesting access to four years of sensitive company filings, <em>including platform exclusivity deals, </em>as<em> </em>a big piece of Sony's arguement against the deal being how Call of Duty being exclusive would be an issue. This action is part of a bigger picture, as <a href=\"https://www.mmobomb.com/news/it-official-despite-microsoft-recent-efforts-us-ftc-suing-to-block-activision-blizzard-purchase\" target=\"_blank\" rel=\"noopener\">the US Federal Trade Commission sued</a> Microsoft to block the Activision Blizzard deal on antitrust grounds in December 2022. </p>\r\n<p>The motion for disclosure was granted by administrative ruling in February 2023 — as part of a fact discovery stage in the pending trial — on the grounds it will help make a case for the defense as to why the takeover should proceed. According to the FTC, this lawsuit will determine whether or not the deal would allow Microsoft to have too much power in the industry which could (in the future) lead to antitrust issues.</p>",
                            "article_url": "https://www.mmobomb.com/news/sony-must-reveal-trade-secrets-to-microsoft-ongoing-lawsuit"
                        },
                        {
                            "id": 133883,
                            "title": "Bungie Announces Destiny 2 Collaboration With Among Us Plus More",
                            "short_description": "This Week at Bungie details what's in store for Destiny 2 following the new Lightfall expansion.",
                            "thumbnail": "https://www.mmobomb.com/file/2023/3/destiny-2-among-us-218x150.jpg",
                            "main_image": "https://www.mmobomb.com/file/2023/3/destiny-2-among-us.jpg",
                            "article_content": "<p>&lt;img src='https://www.mmobomb.com/file/2023/3/destiny-2-among-us-812x456.jpg' class='type:primaryImage aligncenter size-large' alt='destiny 2 among us' width='812' height='456'/&gt;</p>\r\n<p>Bungie has finally launched the hotly anticipated <a href=\"https://www.mmobomb.com/news/destiny-2-lightfall-launches-today\">Lightfall expansion for Destiny 2</a>. But the company has over fun things coming as announced in their weekly segment This Week at Bungie.</p>\r\n<p>In the article, published on March 2, Bungie talks about Strand, the Season of Defiance, Lightfall’s music, The Witness, and the upcoming raid. Also, the company announced that Destiny 2 cosmetics are now available in <a href=\"https://www.mmobomb.com/news/among-us-roadmap-says-time-new-map-this-year\">Among Us</a>.</p>\r\n<p>For more information, <a href=\"https://www.bungie.net/7/en/News/Article/lightfall-twab-03-02-23\">check out the post on Bungie’s official website</a>.</p>",
                            "article_url": "https://www.mmobomb.com/news/bungie-announces-destiny-2-collaboration-among-us-plus-more"
                        },
                        {
                            "id": 133879,
                            "title": "Pre-Registration Is Up If You're Hoping To Hop Into Veiled Experts' Final Closed Beta Before Launch",
                            "short_description": "If you want in, get registered now.",
                            "thumbnail": "https://www.mmobomb.com/file/2023/3/veiled-experts-nexon-final-closed-beta-218x150.jpg",
                            "main_image": "https://www.mmobomb.com/file/2023/3/veiled-experts-nexon-final-closed-beta.jpg",
                            "article_content": "<p><img class=\"type:primaryImage aligncenter size-large\" src=\"https://www.mmobomb.com/file/2023/3/veiled-experts-nexon-final-closed-beta-812x456.jpg\" alt=\"Veiled Experts Nexon final closed beta\" width=\"812\" height=\"456\" /></p>\r\n<p>Nexon announced today that it is opening up registration for the final Beta test for <a href=\"https://www.mmobomb.com/topic/veiled-experts\" target=\"_blank\" rel=\"noopener\">Veiled Experts</a>, a free-to-play online tactical <a href=\"https://www.mmobomb.com/games/shooter\" target=\"_blank\" rel=\"noopener\">multiplayer shooter</a>.</p>\r\n<p>The game features 3v3 or 5v5 Bomb Defusal mode, Team Deathmatch, Free for All, and team Deathmatch vs AI. The game is slated for release later this year.</p>\r\n<p>The final Beta test kicks off on March 30th and will run through April 6th.</p>\r\n<p>In Veiled Experts, players take on the roles of agents engaged in a worldwide conflict between governments, corporations, and terrorists. Each fights for control of a groundbreaking technology known as the Lepton System microchip. Each of the nine agents have a unique blend of skills and Lepton abilities.</p>\r\n<p>Check out the trailer for Veiled Experts below, and you can sign up for the final Beta test over on the official <a href=\"https://store.steampowered.com/app/1934780/VEILED_EXPERTS/\" target=\"_blank\" rel=\"noopener\">Steam page</a>.</p>\r\n<p><iframe src=\"https://www.youtube.com/embed/4ndc3lXlsd8\" title=\"YouTube video player\" width=\"560\" height=\"315\" allowfullscreen=\"allowfullscreen\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\"></iframe></p>",
                            "article_url": "https://www.mmobomb.com/news/pre-register-nexon-veiled-experts-final-beta-test"
                        }
                    ]
                """.trimIndent()
            )
        val expectedDeserializedResponse = listOf(
            MMOGamesArticle(
                133882,
                "Sony Must Reveal Trade Secrets To Microsoft In Ongoing Lawsuit",
                "The motion for disclosure, which includes platform exclusivity deals, was granted in February 2023.",
                "https://www.mmobomb.com/file/2023/3/xbox-playstation-218x150.jpg",
                "https://www.mmobomb.com/file/2023/3/xbox-playstation.jpg",
                "<p><img class=\"type:primaryImage aligncenter size-large\" src=\"https://www.mmobomb.com/file/2023/3/xbox-playstation-812x456.jpg\" alt=\"xbox playstation\" width=\"812\" height=\"456\" /></p>\r\n<p><a href=\"https://www.mmobomb.com/topic/sony\">Sony</a> is not cool with <a href=\"https://www.mmobomb.com/topic/microsoft\">Microsoft’s</a> proposed acquisition of Activision Blizzard, but this pushback means the giant Japanese video game and digital entertainment company <a href=\"https://www.ftc.gov/system/files/ftc_gov/pdf/607003_d09412_-_order_on_motion_of_sony_interactive_entertainment_llc_to_quash_or_limit_subpoena_duces_tecum.pdf\">must reveal numerous trade secrets</a> to its American rival.</p>\r\n<p>In January 2023, Microsoft subpoenaed Sony, requesting access to four years of sensitive company filings, <em>including platform exclusivity deals, </em>as<em> </em>a big piece of Sony's arguement against the deal being how Call of Duty being exclusive would be an issue. This action is part of a bigger picture, as <a href=\"https://www.mmobomb.com/news/it-official-despite-microsoft-recent-efforts-us-ftc-suing-to-block-activision-blizzard-purchase\" target=\"_blank\" rel=\"noopener\">the US Federal Trade Commission sued</a> Microsoft to block the Activision Blizzard deal on antitrust grounds in December 2022. </p>\r\n<p>The motion for disclosure was granted by administrative ruling in February 2023 — as part of a fact discovery stage in the pending trial — on the grounds it will help make a case for the defense as to why the takeover should proceed. According to the FTC, this lawsuit will determine whether or not the deal would allow Microsoft to have too much power in the industry which could (in the future) lead to antitrust issues.</p>",
                "https://www.mmobomb.com/news/sony-must-reveal-trade-secrets-to-microsoft-ongoing-lawsuit"
            ),
            MMOGamesArticle(
                133883,
                "Bungie Announces Destiny 2 Collaboration With Among Us Plus More",
                "This Week at Bungie details what's in store for Destiny 2 following the new Lightfall expansion.",
                "https://www.mmobomb.com/file/2023/3/destiny-2-among-us-218x150.jpg",
                "https://www.mmobomb.com/file/2023/3/destiny-2-among-us.jpg",
                "<p>&lt;img src='https://www.mmobomb.com/file/2023/3/destiny-2-among-us-812x456.jpg' class='type:primaryImage aligncenter size-large' alt='destiny 2 among us' width='812' height='456'/&gt;</p>\r\n<p>Bungie has finally launched the hotly anticipated <a href=\"https://www.mmobomb.com/news/destiny-2-lightfall-launches-today\">Lightfall expansion for Destiny 2</a>. But the company has over fun things coming as announced in their weekly segment This Week at Bungie.</p>\r\n<p>In the article, published on March 2, Bungie talks about Strand, the Season of Defiance, Lightfall’s music, The Witness, and the upcoming raid. Also, the company announced that Destiny 2 cosmetics are now available in <a href=\"https://www.mmobomb.com/news/among-us-roadmap-says-time-new-map-this-year\">Among Us</a>.</p>\r\n<p>For more information, <a href=\"https://www.bungie.net/7/en/News/Article/lightfall-twab-03-02-23\">check out the post on Bungie’s official website</a>.</p>",
                "https://www.mmobomb.com/news/bungie-announces-destiny-2-collaboration-among-us-plus-more"
            ),
            MMOGamesArticle(
                133879,
                "Pre-Registration Is Up If You're Hoping To Hop Into Veiled Experts' Final Closed Beta Before Launch",
                "If you want in, get registered now.",
                "https://www.mmobomb.com/file/2023/3/veiled-experts-nexon-final-closed-beta-218x150.jpg",
                "https://www.mmobomb.com/file/2023/3/veiled-experts-nexon-final-closed-beta.jpg",
                "<p><img class=\"type:primaryImage aligncenter size-large\" src=\"https://www.mmobomb.com/file/2023/3/veiled-experts-nexon-final-closed-beta-812x456.jpg\" alt=\"Veiled Experts Nexon final closed beta\" width=\"812\" height=\"456\" /></p>\r\n<p>Nexon announced today that it is opening up registration for the final Beta test for <a href=\"https://www.mmobomb.com/topic/veiled-experts\" target=\"_blank\" rel=\"noopener\">Veiled Experts</a>, a free-to-play online tactical <a href=\"https://www.mmobomb.com/games/shooter\" target=\"_blank\" rel=\"noopener\">multiplayer shooter</a>.</p>\r\n<p>The game features 3v3 or 5v5 Bomb Defusal mode, Team Deathmatch, Free for All, and team Deathmatch vs AI. The game is slated for release later this year.</p>\r\n<p>The final Beta test kicks off on March 30th and will run through April 6th.</p>\r\n<p>In Veiled Experts, players take on the roles of agents engaged in a worldwide conflict between governments, corporations, and terrorists. Each fights for control of a groundbreaking technology known as the Lepton System microchip. Each of the nine agents have a unique blend of skills and Lepton abilities.</p>\r\n<p>Check out the trailer for Veiled Experts below, and you can sign up for the final Beta test over on the official <a href=\"https://store.steampowered.com/app/1934780/VEILED_EXPERTS/\" target=\"_blank\" rel=\"noopener\">Steam page</a>.</p>\r\n<p><iframe src=\"https://www.youtube.com/embed/4ndc3lXlsd8\" title=\"YouTube video player\" width=\"560\" height=\"315\" allowfullscreen=\"allowfullscreen\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\"></iframe></p>",
                "https://www.mmobomb.com/news/pre-register-nexon-veiled-experts-final-beta-test"
            ),
        )
        mockWebServer.enqueue(mockResponse)

        // when trying to get the latest news
        val result = sut.getLatestNews(mockWebServer.url("/").toString())

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<List<MMOGamesArticle>>>()
        result as ApiResult.Success
        expectThat(result.value).isEqualTo(expectedDeserializedResponse)
    }

    @Test
    fun whenGettingLatestGiveawaysFromAPISuccessfully_dataIsDeserializedProperly() = runTest {
        // given an enqueued mocked sample response
        val serializedResponse = MockResponse()
            .setBody(
                """
                    [
                        {
                            "id": 128061,
                            "title": "Castle Clash $200 Bundle Key Giveaway (New Players Only)",
                            "keys_left": "21%",
                            "thumbnail": "https://www.mmobomb.com/file/2022/3/castle-clash-box-218x150.png",
                            "main_image": "https://www.mmobomb.com/file/2022/3/castle-clash-box.png",
                            "short_description": "To unlock your key instantly you just need to log in and click the button on the top.",
                            "giveaway_url": "https://www.mmobomb.com/giveaway/castle-clash-starter-keys"
                        },
                        {
                            "id": 128077,
                            "title": "Doomsday: Last Survivors Gift Key Giveaway (New Players)",
                            "keys_left": "24%",
                            "thumbnail": "https://www.mmobomb.com/file/2022/8/doomday-box-218x150.png",
                            "main_image": "https://www.mmobomb.com/file/2022/8/doomday-box.png",
                            "short_description": "To unlock your key instantly you just need to log in and click the button on the top.",
                            "giveaway_url": "https://www.mmobomb.com/giveaway/doomsday-last-survivors-gift-key"
                        },
                        {
                            "id": 119744,
                            "title": "Eudemons Online Gift Pack Key Giveaway",
                            "keys_left": "89%",
                            "thumbnail": "https://www.mmobomb.com/file/2023/3/eudemons-online-gift-pack-key-giveaway-218x150.png",
                            "main_image": "https://www.mmobomb.com/file/2023/3/eudemons-online-gift-pack-key-giveaway.png",
                            "short_description": "To unlock your key instantly you just need to complete all the steps on the top.",
                            "giveaway_url": "https://www.mmobomb.com/giveaway/eudemons-online-gift-pack-key-giveaway"
                        }
                    ]
                """.trimIndent()
            )
        mockWebServer.enqueue(serializedResponse)
        val expectedResponse = listOf(
            MMOGiveawayEntry(
                128061,
                "Castle Clash $200 Bundle Key Giveaway (New Players Only)",
                "21%",
                "https://www.mmobomb.com/file/2022/3/castle-clash-box-218x150.png",
                "https://www.mmobomb.com/file/2022/3/castle-clash-box.png",
                "To unlock your key instantly you just need to log in and click the button on the top.",
                "https://www.mmobomb.com/giveaway/castle-clash-starter-keys"
            ),
            MMOGiveawayEntry(
                128077,
                "Doomsday: Last Survivors Gift Key Giveaway (New Players)",
                "24%",
                "https://www.mmobomb.com/file/2022/8/doomday-box-218x150.png",
                "https://www.mmobomb.com/file/2022/8/doomday-box.png",
                "To unlock your key instantly you just need to log in and click the button on the top.",
                "https://www.mmobomb.com/giveaway/doomsday-last-survivors-gift-key"
            ),
            MMOGiveawayEntry(
                119744,
                "Eudemons Online Gift Pack Key Giveaway",
                "89%",
                "https://www.mmobomb.com/file/2023/3/eudemons-online-gift-pack-key-giveaway-218x150.png",
                "https://www.mmobomb.com/file/2023/3/eudemons-online-gift-pack-key-giveaway.png",
                "To unlock your key instantly you just need to complete all the steps on the top.",
                "https://www.mmobomb.com/giveaway/eudemons-online-gift-pack-key-giveaway"
            ),
        )

        // when trying to get the latest giveaways
        val result = sut.getLatestGiveaways(mockWebServer.url("/").toString())

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<List<MMOGiveawayEntry>>>()
        result as ApiResult.Success
        expectThat(result.value).isEqualTo(expectedResponse)
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
        val result = sut.getLatestNews(mockWebServer.url("/").toString())

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