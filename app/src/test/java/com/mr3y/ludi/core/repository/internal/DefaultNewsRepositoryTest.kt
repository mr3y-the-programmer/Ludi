package com.mr3y.ludi.core.repository.internal

import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.network.datasources.internal.MMOGamesDataSource
import com.mr3y.ludi.core.network.fixtures.FakeGameSpotDataSource
import com.mr3y.ludi.core.network.fixtures.FakeGiantBombDataSource
import com.mr3y.ludi.core.network.fixtures.FakeIGNDataSource
import com.mr3y.ludi.core.network.fixtures.FakeTechRadarDataSource
import com.mr3y.ludi.core.network.fixtures.Feed
import com.mr3y.ludi.core.network.fixtures.PrintlnLogWriter
import com.mr3y.ludi.core.network.fixtures.logger
import com.mr3y.ludi.core.network.model.MMOGamesArticle
import com.slack.eithernet.ApiResult
import com.slack.eithernet.test.enqueue
import com.slack.eithernet.test.newEitherNetController
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.BeforeClass
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.net.HttpURLConnection

class DefaultNewsRepositoryTest {

    @Test
    fun whenGettingNewsSuccessfullyFromChosenDataSources_DataIsAggregated() = runTest {
        // given some enqueued responses in GameSpot, GiantBomb, MMOBomb
        val mmoBombArticles = listOf(
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
            )
        )
        controller.enqueue(MMOGamesDataSource::getLatestNews, ApiResult.success(mmoBombArticles))

        // when trying to get the latest available news
        val result = sut.getLatestGamingNews(setOf(Source.GameSpot, Source.GiantBomb, Source.IGN, Source.TechRadar, Source.MMOBomb))

        // then verify we get the data aggregated
        val expectedNews = setOf(
            giantBombDataSource.cache[Feed.News]!!.map { it.toNewsArticle() },
            gameSpotDataSource.cache[Feed.News]!!.map { it.toNewsArticle() },
            mmoBombArticles.map(MMOGamesArticle::toNewsArticle),
            ignDataSource.cache[Feed.News]!!.map { it.toNewsArticle() },
            techRadarDataSource.cache[Feed.News]!!.map { it.toNewsArticle() }
        ).flatten().toSet()

        expectThat(result).isA<Result.Success<List<NewsArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expectedNews.toSet())
    }

    @Test
    fun whenGettingPartialErrorsAndSomeSuccessfulResponses_responsesAreReturned() = runTest {
        // given some successful responses & a failure in specific datasource/s
        controller.enqueue(MMOGamesDataSource::getLatestNews, ApiResult.httpFailure(code = HttpURLConnection.HTTP_BAD_REQUEST))

        // when trying to get the latest available news
        val result = sut.getLatestGamingNews(setOf(Source.GameSpot, Source.GiantBomb, Source.MMOBomb))

        // then verify we get the aggregated successful responses & the error is logged
        val expectedNews = giantBombDataSource.cache[Feed.News]!!.map { it.toNewsArticle() }
            .plus(gameSpotDataSource.cache[Feed.News]!!.map { it.toNewsArticle() })
            .toSet()

        expectThat(result).isA<Result.Success<List<NewsArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expectedNews.toSet())
        expectThat(logWriter.logs.size).isEqualTo(1)
    }

    @Test
    fun whenGettingAllErrorsAndNoSuccessfulResponses_ResultErrorIsReturned() = runTest {
        // given a failure in all data source and no successful responses
        controller.enqueue(MMOGamesDataSource::getLatestNews, ApiResult.httpFailure(code = HttpURLConnection.HTTP_BAD_REQUEST))
        gameSpotDataSource.simulateFailure()
        giantBombDataSource.simulateFailure()

        // when trying to get the latest available news
        val result = sut.getLatestGamingNews(setOf(Source.GameSpot, Source.GiantBomb, Source.MMOBomb))

        // then verify we get an error result
        expectThat(result).isA<Result.Error>()
        expectThat(logWriter.logs.size).isEqualTo(3)
    }

    @Test
    fun whenGettingReviewsSuccessfullyFromChosenDataSources_DataIsAggregated() = runTest {
        val result = sut.getGamesReviews(setOf(Source.GameSpot, Source.GiantBomb, Source.MMOBomb))

        val expected = giantBombDataSource.cache[Feed.Reviews]!!.map { it.toReviewArticle() }
            .plus(gameSpotDataSource.cache[Feed.Reviews]!!.map { it.toReviewArticle() })
            .toSet()

        expectThat(result).isA<Result.Success<List<ReviewArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
    }

    @Test
    fun whenGettingPartialErrorsAndSomeSuccessfulReviews_responsesAreReturned() = runTest {
        techRadarDataSource.simulateFailure()

        val result = sut.getGamesReviews(setOf(Source.GameSpot, Source.TechRadar))

        val expected = gameSpotDataSource.cache[Feed.Reviews]!!.map { it.toReviewArticle() }.toSet()

        expectThat(result).isA<Result.Success<List<ReviewArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
        expectThat(logWriter.logs.size).isEqualTo(1)
    }

    @Test
    fun whenGettingAllErrorsAndNoSuccessfulReviews_ResultErrorIsReturned() = runTest {
        gameSpotDataSource.simulateFailure()
        giantBombDataSource.simulateFailure()

        val result = sut.getGamesReviews(setOf(Source.GameSpot, Source.GiantBomb))

        expectThat(result).isA<Result.Error>()
        expectThat(logWriter.logs.size).isEqualTo(2)
    }

    @Test
    fun whenGettingNewReleasesSuccessfullyFromChosenDataSources_DataIsAggregated() = runTest {
        val result = sut.getGamesNewReleases(setOf(Source.GameSpot, Source.GiantBomb, Source.MMOBomb))

        val expected = giantBombDataSource.cache[Feed.NewReleases]!!.map { it.toNewReleaseArticle() }
            .plus(gameSpotDataSource.cache[Feed.NewReleases]!!.map { it.toNewReleaseArticle() })
            .toSet()

        expectThat(result).isA<Result.Success<List<NewReleaseArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
    }

    @Test
    fun whenGettingPartialErrorsAndSomeSuccessfulNewReleases_responsesAreReturned() = runTest {
        giantBombDataSource.simulateFailure()

        val result = sut.getGamesNewReleases(setOf(Source.GameSpot, Source.GiantBomb))

        val expected = gameSpotDataSource.cache[Feed.NewReleases]!!.map { it.toNewReleaseArticle() }.toSet()

        expectThat(result).isA<Result.Success<List<NewReleaseArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expected)
        expectThat(logWriter.logs.size).isEqualTo(1)
    }

    @Test
    fun whenGettingAllErrorsAndNoSuccessfulNewReleases_ResultErrorIsReturned() = runTest {
        gameSpotDataSource.simulateFailure()
        giantBombDataSource.simulateFailure()

        val result = sut.getGamesNewReleases(setOf(Source.GameSpot, Source.GiantBomb))

        expectThat(result).isA<Result.Error>()
        expectThat(logWriter.logs.size).isEqualTo(2)
    }

    @After
    fun teardown() {
        giantBombDataSource.reset()
        gameSpotDataSource.reset()
        ignDataSource.reset()
        techRadarDataSource.reset()
        logWriter.reset()
    }

    companion object {

        private lateinit var sut: DefaultNewsRepository
        private val controller = newEitherNetController<MMOGamesDataSource>()
        private val giantBombDataSource = FakeGiantBombDataSource()
        private val gameSpotDataSource = FakeGameSpotDataSource()
        private val ignDataSource = FakeIGNDataSource()
        private val techRadarDataSource = FakeTechRadarDataSource()
        private val logWriter = PrintlnLogWriter()

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sut = DefaultNewsRepository(
                mapOf(
                    Source.GiantBomb to giantBombDataSource,
                    Source.GameSpot to gameSpotDataSource,
                    Source.IGN to ignDataSource,
                    Source.TechRadar to techRadarDataSource
                ),
                controller.api,
                logger(logWriter = logWriter)
            )
        }
    }
}
