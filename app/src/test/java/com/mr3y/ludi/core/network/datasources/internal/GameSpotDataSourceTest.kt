package com.mr3y.ludi.core.network.datasources.internal

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.model.GameSpotArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(TestParameterInjector::class)
class GameSpotDataSourceTest {

    @Test
    fun whenFetchingRSSFeedSuccessfully_dataIsTransformedProperly(@TestParameter rssFeedUrlWithSampleData: RSSFeedURLWithSampleData) = runTest {
        // given a mocked request with sample data
        coEvery { rssParser.getChannel(rssFeedUrlWithSampleData.url) } returns rssFeedUrlWithSampleData.channel

        // when trying to fetch rss feed
        val result = when(rssFeedUrlWithSampleData.url) {
            GameSpotDataSource.RSSNewsFeedURL -> sut.fetchNewsFeed()
            GameSpotDataSource.RSSNewReleasesFeedURL -> sut.fetchNewReleasesFeed()
            GameSpotDataSource.RSSReviewsFeedURL -> sut.fetchReviewsFeed()
            else -> throw IllegalArgumentException()
        }

        // then assert we got the sample data parsed & transformed to our typed model
        expectThat(result).isA<Result.Success<List<GameSpotArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(rssFeedUrlWithSampleData.expectedArticles)
    }

    @Test
    fun whenFailingToFetchRSSFeed_exceptionIsWrapped(@TestParameter rssFeedUrlWithSampleData: RSSFeedURLWithSampleData) = runTest {
        // Assuming there is a failure while trying to fetch rss feed
        coEvery { rssParser.getChannel(rssFeedUrlWithSampleData.url) } throws Exception()

        // when trying to fetch rss feed
        val result = when(rssFeedUrlWithSampleData.url) {
            GameSpotDataSource.RSSNewsFeedURL -> sut.fetchNewsFeed()
            GameSpotDataSource.RSSNewReleasesFeedURL -> sut.fetchNewReleasesFeed()
            GameSpotDataSource.RSSReviewsFeedURL -> sut.fetchReviewsFeed()
            else -> throw IllegalArgumentException()
        }

        // then assert the exception is caught and wrapped into a typed Result.Error
        expectThat(result).isA<Result.Error>()
    }

    enum class RSSFeedURLWithSampleData(val url: String, val channel: Channel, val expectedArticles: List<GameSpotArticle>) {
        NewsFeed(
            GameSpotDataSource.RSSNewsFeedURL,
            Channel(
                title = "GameSpot - Game News",
                link = "https://www.gamespot.com/feeds/game-news",
                description = "All the latest GameSpot Game and Tech News",
                lastBuildDate = "Thu, 02 Mar 2023 05:06:57 -0800",
                articles = listOf(
                    Article(
                        title = "Wo Long: Fallen Dynasty New Game Plus Explained: 5-Star Gear And Other Changes",
                        link = "https://www.gamespot.com/articles/wo-long-fallen-dynasty-new-game-plus-explained-5-star-gear-and-other-changes/1100-6511878/?ftag=CAD-01-10abi2f",
                        description = "<![CDATA[ <p><a href=\"https://www.gamespot.com/games/wo-long-fallen-dynasty/\">Wo Long: Fallen Dynasty</a> is a challenging soulslike experience that is certain to last <a href=\"https://www.gamespot.com/articles/how-long-is-wo-long-fallen-dynasty/1100-6511879/\">quite a few hours</a> on an initial playthrough. With so much content to see throughout your first run through the campaign, you might think that's all that is available to you--but there's plenty more to delve into after besting the final boss. As with Team Ninja's other games, there is a New Game Plus mode to experience once you've rolled the credits, and we'll tell you what to expect below.</p><h2>How New Game Plus works in Wo Long: Fallen Dynasty</h2><p>Once you beat the game by defeating the final boss, you unlock the ability to replay the game on the harder <strong>Rising Dragon difficulty</strong>. Parts 1-3 are available from the get-go on this new difficulty, meaning you can take on missions out of their original order if you find yourself stuck on something or simply don't want to play a particular level. As you reach certain completion milestones, you gain access to more and more late-game missions that you can once again finish in whatever order you prefer.</p><p>Enemies will hit much harder on this new difficulty, so maxing out your 4-star gear and having a <a href=\"https://www.gamespot.com/articles/wo-long-fallen-dynasty-how-to-respec/1100-6511683/\">solid build</a> will be of vital importance if you want to tackle the whole campaign again. Otherwise, not much changes besides the <strong>addition of 5-star gear</strong>, which can be periodically found in chests or dropped by enemies. Coming by this highest tier of gear won't be common, so you may need to grind certain missions or bosses for chances at scoring specific pieces that would help your build.</p><p>If you're just diving in, though, be sure to check out our <a href=\"https://www.gamespot.com/articles/wo-long-fallen-dynasty-beginners-guide/1100-6511641/\">beginner's guide</a> for some tips to get you started.</p> ]]>",
                        pubDate = "Thu, 02 Mar 2023 03:00:00 -0800",
                        author = "Billy Givens",
                        image = "https://www.gamespot.com/a/uploads/screen_medium/679/6794662/4102198-1%284%29.jpg",
                        guid = null,
                        content = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    ),
                    Article(
                        title = "How Long Is Wo Long: Fallen Dynasty?",
                        link = "https://www.gamespot.com/articles/how-long-is-wo-long-fallen-dynasty/1100-6511879/?ftag=CAD-01-10abi2f",
                        description = "<![CDATA[ <p><a href=\"https://www.gamespot.com/games/wo-long-fallen-dynasty/\">Wo Long: Fallen Dynasty</a> is Team Ninja's newest soulslike outing, bringing the company's trademark challenge and flair to a tale based in ancient China. With winding levels filled to the brim with demons, punishing bosses who can snuff you out in just a hit or two, and an abundance of gear to grind for and upgrade, Wo Long: Fallen Dynasty will certainly keep you busy for many, many hours. If you'd like to know just how long it'll take you to make your way through, though, we've got the answer here.</p><h2>How long is Wo Long: Fallen Dynasty?</h2><p>Completing the campaign in Wo Long: Fallen Dynasty will average around <strong>25 hours</strong>. That being said, genre veterans with considerable knowledge of how these types of games play may be able to work through it a bit quicker, while those trying their hands at a soulslike for the first time could find that it takes them even longer.</p><p>Completionists, meanwhile, can expect to spend at least <strong>5-10 additional hours beating any skipped side missions</strong> and wrapping up the extra requirements for obtaining all of the Trophies or Achievements in the game. This includes finding various types of collectibles, fully upgrading armor and weapons, engaging in some cooperative play, and doing a few miscellaneous tasks.</p><p>Those looking to extend their time with Wo Long: Fallen Dynasty and test their mettle can dive in and check out <a href=\"https://www.gamespot.com/articles/does-wo-long-fallen-dynasty-have-new-game-plus/1100-6511878/\">New Game Plus mode</a>, too, which is considerably more difficult while offering the chance at better gear. It's only for those seeking a true challenge and is not required to earn all of the Trophies or Achievements.</p><p>If you decide to dive into Wo Long: Fallen Dynasty, make sure to check out our <a href=\"https://www.gamespot.com/articles/wo-long-fallen-dynasty-beginners-guide/1100-6511641/\">beginner's guide</a> for some helpful tips.</p> ]]>",
                        pubDate = "Thu, 02 Mar 2023 03:00:00 -0800",
                        author = "Billy Givens",
                        image = "https://www.gamespot.com/a/uploads/screen_medium/679/6794662/4102197-1%283%29.jpg",
                        guid = null,
                        content = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    ),
                    Article(
                        title = "Halo Infinite Team Deep Dives On Season 3's New Maps",
                        link = "https://www.gamespot.com/articles/halo-infinite-team-deep-dives-on-season-3s-new-maps/1100-6511964/?ftag=CAD-01-10abi2f",
                        description = "<![CDATA[ <p>Almost a week ahead of Halo Infinite Season 3's launch, developer 343 Industries has released new information about the upcoming updates maps and modes.</p><p>As previously shown in <a href=\"https://www.gamespot.com/articles/halo-infinite-season-3-echoes-within-is-its-biggest-update-yet/1100-6511817/\">Season 3's launch trailer</a>, the season will introduce three new maps to Halo: Infinite. The first, Oasis, is a Big Team Battle map, set on red rocks in the process of terraforming to a verdant green. Basically everywhere on this map is vehicle accessible, meaning that you can take a warthog into the tunnels and buildings that populate the landscape. This offers both chaotic and strategic possibilities, especially for CTF.</p><p>The second is an arena map called Cliffhanger. This map is a snowy mountaintop, which hides an ONI research facility. Cliffhanger was designed with zone-control modes in mind. It's asymmetric and the design team wanted to make each area feel unique and tactically different, depending on the mode you are playing the map in.</p><p>The third is another arena map, entitled Chasm. This one showcases the interior of Zeta Halo, with bridges and floating columns standing above an immense drop. Chasm is based on, but not directly taken from, an area in Halo Infinite's campaign. It has been adjusted to become more of a symmetrical map and tuned to make grappling through the space a risky, but deadly, challenge.</p><p>As for the rest of the update, the last part of the blog post concerned Escalation Slayer. Similar to Call of Duty's Gun Game mode, Escalation Slayer tasks you with killing enemies with specific weapons. With each kill, you'll advance through set loadouts. First player to get a kill with every loadout wins. Melee attacking enemy players from behind will drop them a level. Escalation Slayer will launch with two variants: FFA Escalation and Team Escalation, where the whole team advances through the loadout levels together. A Big Team Battle version is on the way, as well as another variant that incorporates \"super\" weapons seen in Halo Infinite's campaign.</p><p>Here's the full breakdown of Escalation Slayer loadouts.</p><ul><li><strong>LEVEL 1:</strong> Rocket Launcher, Cindershot, Repulsor</li><li><strong>LEVEL 2:</strong> Energy Sword, Gravity Hammer, Grappleshot</li><li><strong>LEVEL 3:</strong> Sniper Rifle, Skewer, Threat Sensor</li><li><strong>LEVEL 4:</strong> Hydra, Ravager, Repulsor</li><li><strong>LEVEL 5:</strong> Bulldog, Needler, Grappleshot</li><li><strong>LEVEL 6:</strong> Battle Rifle, Commando, Threat Sensor</li><li><strong>LEVEL 7:</strong> Shock Rifle, Stalker Rifle, Drop Wall</li><li><strong>LEVEL 8:</strong> Heatwave, Sentinel Beam, Thruster</li><li><strong>LEVEL 9:</strong> Bandit, Assault Rifle, Drop Wall</li><li><strong>LEVEL 10:</strong> Mangler, Sidekick, Threat Sensor</li><li><strong>LEVEL 11:</strong> Oddball, Shroud Screen</li></ul><p>You can read the full deep dive on <a href=\"https://www.halowaypoint.com/news/echoes-within-maps-modes-preview\">Halo Waypoint</a>.</p> ]]>",
                        pubDate = "Wed, 01 Mar 2023 21:30:00 -0800",
                        author = "Grace Benfell",
                        image = "https://www.gamespot.com/a/uploads/screen_medium/1690/16904437/4106435-oasis%281%29.jpg",
                        guid = null,
                        content = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    )
                ),
                itunesChannelData = null,
                image = null,
                updatePeriod = null
            ),
            listOf(
                GameSpotArticle(
                    title = "Wo Long: Fallen Dynasty New Game Plus Explained: 5-Star Gear And Other Changes",
                    author = "Billy Givens",
                    description = "<![CDATA[ <p><a href=\"https://www.gamespot.com/games/wo-long-fallen-dynasty/\">Wo Long: Fallen Dynasty</a> is a challenging soulslike experience that is certain to last <a href=\"https://www.gamespot.com/articles/how-long-is-wo-long-fallen-dynasty/1100-6511879/\">quite a few hours</a> on an initial playthrough. With so much content to see throughout your first run through the campaign, you might think that's all that is available to you--but there's plenty more to delve into after besting the final boss. As with Team Ninja's other games, there is a New Game Plus mode to experience once you've rolled the credits, and we'll tell you what to expect below.</p><h2>How New Game Plus works in Wo Long: Fallen Dynasty</h2><p>Once you beat the game by defeating the final boss, you unlock the ability to replay the game on the harder <strong>Rising Dragon difficulty</strong>. Parts 1-3 are available from the get-go on this new difficulty, meaning you can take on missions out of their original order if you find yourself stuck on something or simply don't want to play a particular level. As you reach certain completion milestones, you gain access to more and more late-game missions that you can once again finish in whatever order you prefer.</p><p>Enemies will hit much harder on this new difficulty, so maxing out your 4-star gear and having a <a href=\"https://www.gamespot.com/articles/wo-long-fallen-dynasty-how-to-respec/1100-6511683/\">solid build</a> will be of vital importance if you want to tackle the whole campaign again. Otherwise, not much changes besides the <strong>addition of 5-star gear</strong>, which can be periodically found in chests or dropped by enemies. Coming by this highest tier of gear won't be common, so you may need to grind certain missions or bosses for chances at scoring specific pieces that would help your build.</p><p>If you're just diving in, though, be sure to check out our <a href=\"https://www.gamespot.com/articles/wo-long-fallen-dynasty-beginners-guide/1100-6511641/\">beginner's guide</a> for some tips to get you started.</p> ]]>",
                    image = "https://www.gamespot.com/a/uploads/screen_medium/679/6794662/4102198-1%284%29.jpg",
                    content = null,
                    sourceLink = "https://www.gamespot.com/articles/wo-long-fallen-dynasty-new-game-plus-explained-5-star-gear-and-other-changes/1100-6511878/?ftag=CAD-01-10abi2f",
                    publicationDate = "Thu, 02 Mar 2023 03:00:00 -0800",
                ),
                GameSpotArticle(
                    title = "How Long Is Wo Long: Fallen Dynasty?",
                    author = "Billy Givens",
                    description = "<![CDATA[ <p><a href=\"https://www.gamespot.com/games/wo-long-fallen-dynasty/\">Wo Long: Fallen Dynasty</a> is Team Ninja's newest soulslike outing, bringing the company's trademark challenge and flair to a tale based in ancient China. With winding levels filled to the brim with demons, punishing bosses who can snuff you out in just a hit or two, and an abundance of gear to grind for and upgrade, Wo Long: Fallen Dynasty will certainly keep you busy for many, many hours. If you'd like to know just how long it'll take you to make your way through, though, we've got the answer here.</p><h2>How long is Wo Long: Fallen Dynasty?</h2><p>Completing the campaign in Wo Long: Fallen Dynasty will average around <strong>25 hours</strong>. That being said, genre veterans with considerable knowledge of how these types of games play may be able to work through it a bit quicker, while those trying their hands at a soulslike for the first time could find that it takes them even longer.</p><p>Completionists, meanwhile, can expect to spend at least <strong>5-10 additional hours beating any skipped side missions</strong> and wrapping up the extra requirements for obtaining all of the Trophies or Achievements in the game. This includes finding various types of collectibles, fully upgrading armor and weapons, engaging in some cooperative play, and doing a few miscellaneous tasks.</p><p>Those looking to extend their time with Wo Long: Fallen Dynasty and test their mettle can dive in and check out <a href=\"https://www.gamespot.com/articles/does-wo-long-fallen-dynasty-have-new-game-plus/1100-6511878/\">New Game Plus mode</a>, too, which is considerably more difficult while offering the chance at better gear. It's only for those seeking a true challenge and is not required to earn all of the Trophies or Achievements.</p><p>If you decide to dive into Wo Long: Fallen Dynasty, make sure to check out our <a href=\"https://www.gamespot.com/articles/wo-long-fallen-dynasty-beginners-guide/1100-6511641/\">beginner's guide</a> for some helpful tips.</p> ]]>",
                    image = "https://www.gamespot.com/a/uploads/screen_medium/679/6794662/4102197-1%283%29.jpg",
                    content = null,
                    sourceLink = "https://www.gamespot.com/articles/how-long-is-wo-long-fallen-dynasty/1100-6511879/?ftag=CAD-01-10abi2f",
                    publicationDate = "Thu, 02 Mar 2023 03:00:00 -0800",
                ),
                GameSpotArticle(
                    title = "Halo Infinite Team Deep Dives On Season 3's New Maps",
                    author = "Grace Benfell",
                    description = "<![CDATA[ <p>Almost a week ahead of Halo Infinite Season 3's launch, developer 343 Industries has released new information about the upcoming updates maps and modes.</p><p>As previously shown in <a href=\"https://www.gamespot.com/articles/halo-infinite-season-3-echoes-within-is-its-biggest-update-yet/1100-6511817/\">Season 3's launch trailer</a>, the season will introduce three new maps to Halo: Infinite. The first, Oasis, is a Big Team Battle map, set on red rocks in the process of terraforming to a verdant green. Basically everywhere on this map is vehicle accessible, meaning that you can take a warthog into the tunnels and buildings that populate the landscape. This offers both chaotic and strategic possibilities, especially for CTF.</p><p>The second is an arena map called Cliffhanger. This map is a snowy mountaintop, which hides an ONI research facility. Cliffhanger was designed with zone-control modes in mind. It's asymmetric and the design team wanted to make each area feel unique and tactically different, depending on the mode you are playing the map in.</p><p>The third is another arena map, entitled Chasm. This one showcases the interior of Zeta Halo, with bridges and floating columns standing above an immense drop. Chasm is based on, but not directly taken from, an area in Halo Infinite's campaign. It has been adjusted to become more of a symmetrical map and tuned to make grappling through the space a risky, but deadly, challenge.</p><p>As for the rest of the update, the last part of the blog post concerned Escalation Slayer. Similar to Call of Duty's Gun Game mode, Escalation Slayer tasks you with killing enemies with specific weapons. With each kill, you'll advance through set loadouts. First player to get a kill with every loadout wins. Melee attacking enemy players from behind will drop them a level. Escalation Slayer will launch with two variants: FFA Escalation and Team Escalation, where the whole team advances through the loadout levels together. A Big Team Battle version is on the way, as well as another variant that incorporates \"super\" weapons seen in Halo Infinite's campaign.</p><p>Here's the full breakdown of Escalation Slayer loadouts.</p><ul><li><strong>LEVEL 1:</strong> Rocket Launcher, Cindershot, Repulsor</li><li><strong>LEVEL 2:</strong> Energy Sword, Gravity Hammer, Grappleshot</li><li><strong>LEVEL 3:</strong> Sniper Rifle, Skewer, Threat Sensor</li><li><strong>LEVEL 4:</strong> Hydra, Ravager, Repulsor</li><li><strong>LEVEL 5:</strong> Bulldog, Needler, Grappleshot</li><li><strong>LEVEL 6:</strong> Battle Rifle, Commando, Threat Sensor</li><li><strong>LEVEL 7:</strong> Shock Rifle, Stalker Rifle, Drop Wall</li><li><strong>LEVEL 8:</strong> Heatwave, Sentinel Beam, Thruster</li><li><strong>LEVEL 9:</strong> Bandit, Assault Rifle, Drop Wall</li><li><strong>LEVEL 10:</strong> Mangler, Sidekick, Threat Sensor</li><li><strong>LEVEL 11:</strong> Oddball, Shroud Screen</li></ul><p>You can read the full deep dive on <a href=\"https://www.halowaypoint.com/news/echoes-within-maps-modes-preview\">Halo Waypoint</a>.</p> ]]>",
                    image = "https://www.gamespot.com/a/uploads/screen_medium/1690/16904437/4106435-oasis%281%29.jpg",
                    content = null,
                    sourceLink = "https://www.gamespot.com/articles/halo-infinite-team-deep-dives-on-season-3s-new-maps/1100-6511964/?ftag=CAD-01-10abi2f",
                    publicationDate = "Wed, 01 Mar 2023 21:30:00 -0800",
                )
            )
        ),
        NewReleasesFeed(
            GameSpotDataSource.RSSNewReleasesFeedURL,
            Channel(
                title = "GameSpot - New Game Releases",
                link = "https://www.gamespot.com/feeds/new-games/",
                description = "The newest Games on GameSpot.",
                lastBuildDate = "Thu, 02 Mar 2023 06:47:39 -0800",
                articles = listOf(
                    Article(
                        title = "logik",
                        link = "https://www.gamespot.com/games/logik/",
                        description = "",
                        pubDate = "Fri, 03 Mar 2023 00:00:00 -0800",
                        author = null,
                        content = "",
                        guid = null,
                        image = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    ),
                    Article(
                        title = "CAT Interstellar: Episode II",
                        link = "https://www.gamespot.com/games/cat-interstellar-episode-ii/",
                        description = null,
                        pubDate = "Fri, 03 Mar 2023 00:00:00 -0800",
                        author = null,
                        content = null,
                        guid = null,
                        image = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    ),
                    Article(
                        title = "TIMESTRIDE",
                        link = "https://www.gamespot.com/games/timestride/",
                        description = null,
                        pubDate = "Fri, 03 Mar 2023 00:00:00 -0800",
                        author = "",
                        content = null,
                        guid = null,
                        image = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    )
                ),
                itunesChannelData = null,
                image = null,
                updatePeriod = null
            ),
            listOf(
                GameSpotArticle(
                    title = "logik",
                    author = null,
                    description = null,
                    image = null,
                    content = null,
                    sourceLink = "https://www.gamespot.com/games/logik/",
                    publicationDate = "Fri, 03 Mar 2023 00:00:00 -0800"
                ),
                GameSpotArticle(
                    title = "CAT Interstellar: Episode II",
                    author = null,
                    description = null,
                    image = null,
                    content = null,
                    sourceLink = "https://www.gamespot.com/games/cat-interstellar-episode-ii/",
                    publicationDate = "Fri, 03 Mar 2023 00:00:00 -0800"
                ),
                GameSpotArticle(
                    title = "TIMESTRIDE",
                    author = null,
                    description = null,
                    image = null,
                    content = null,
                    sourceLink = "https://www.gamespot.com/games/timestride/",
                    publicationDate = "Fri, 03 Mar 2023 00:00:00 -0800"
                )
            )
        ),
        ReviewsFeed(
            GameSpotDataSource.RSSReviewsFeedURL,
            Channel(
                title = "GameSpot - Game Reviews",
                link = "https://www.gamespot.com/feeds/reviews/",
                description = "The latest Game Reviews from GameSpot.",
                lastBuildDate = "Thu, 02 Mar 2023 05:28:28 -0800",
                articles = listOf(
                    Article(
                        title = "Wo Long: Fallen Dynasty Review - Souls-Like Of The Three Kingdoms",
                        link = "https://www.gamespot.com/reviews/wo-long-fallen-dynasty-review-souls-like-of-the-three-kingdoms/1900-6418040/?ftag=CAD-01-10abi2f",
                        description = "<![CDATA[ <p dir=\"ltr\">The first boss fight in Wo Long: Fallen Dynasty is right up there with the toughest first bosses in video game history. This opening battle pits you against Zhang Liang of the Yellow Turbans, as you clash in a kinetic two-phase fight to the death. It's an intense skill check that challenges your prowess of Wo Long's mechanics almost immediately. In many ways, it feels like a rite of passage for the rest of the game and a bold statement of intent from developer Team Ninja. I initially loved how it forced me to adapt to the demands of the game's particular brand of Souls-like combat, yet the further I progressed, the more this feeling dissipated as I realized that this introductory struggle was little more than an unbalanced outlier, providing a much sterner test than the bosses following it.</p><p dir=\"ltr\">For many, this sudden difficulty spike will be a barrier to entry, halting progress a mere 10 minutes into the game. It's a shame Wo Long begins with such a sturdy roadblock, not least because this initial undertaking isn't indicative of the rest of the game moving forward. In fact, outside of this first boss, Team Ninja has crafted one of the more approachable Souls-likes in what is a traditionally challenging genre.</p><p dir=\"ltr\">I didn't encounter another boss fight on par with Zhang Liang's difficulty until roughly 15 hours into Wo Long's campaign. Most of the bosses in between were a relative cakewalk, to the point where I was able to cut down each one on my first attempt--usually in under a minute. I still had fun dispatching every single one, but the ease with which I was able to do so makes them lose some of their luster and reinforces the notion that the first boss is at odds with the rest of the game. The battle with Zhang Liang sets up expectations that never come to fruition, particularly when other fights allow you to summon help from either AI or human teammates.</p><a href=\"https://www.gamespot.com/reviews/wo-long-fallen-dynasty-review-souls-like-of-the-three-kingdoms/1900-6418040/?ftag=CAD-01-10abi2f/\">Continue Reading at GameSpot</a> ]]>",
                        pubDate = "Thu, 02 Mar 2023 03:00:00 -0800",
                        author = "Richard Wakeling",
                        image = "https://www.gamespot.com/a/uploads/screen_medium/43/434805/4106384-wolongthumb.jpg",
                        guid = null,
                        content = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    ),
                    Article(
                        title = "Tales Of Symphonia Remastered Review - A Classic Regenerated",
                        link = "https://www.gamespot.com/reviews/tales-of-symphonia-remastered-review-a-classic-regenerated/1900-6418037/?ftag=CAD-01-10abi2f",
                        description = "<![CDATA[ <p dir=\"ltr\">Tales of Symphonia was a formative experience for me. For my young 11-year-old brain, it redefined my understanding of the JRPG genre. The vibrant presentation, action-focused combat, and mature story took me by surprise. Weekend after weekend, a friend and I would explore the world of Sylvarant together, making incremental progress in each play session. While I had played a few JRPGs before, none had hooked me the way Tales of Symphonia had.</p><p dir=\"ltr\">Despite my deep reverence for Tales of Symphonia, I haven't touched it since 2004. I don't really know why. I bought it on PC a few years back, but it just felt wrong to play that game sitting at my desk one random evening after work--almost as if it would tarnish the magic of that experience and the memories tied to it. However, with the release of Tales of Symphonia Remastered, I decided it was finally time to return to this world to see if it was as good as I remember. The result was a bit mixed.</p><p dir=\"ltr\">Tales of Symphonia follows a kid named Lloyd Irving as he accompanies the Chosen One on a globetrotting adventure. The Chosen One, Colette, instructed by divine prophecy, must \"regenerate\" the world in order to end war, famine, and hatred. It seems like standard JRPG fare, but the story is darker and far more complex than it initially lets on. Despite trying to do the right thing, Lloyd and his companions are confronted with moral quandaries that often leave a trail of destruction behind them. What makes the story so effective is how it rarely shies away from the consequences of our heroes' actions. Conflicts are rarely resolved neatly, and the story is better for it.</p><a href=\"https://www.gamespot.com/reviews/tales-of-symphonia-remastered-review-a-classic-regenerated/1900-6418037/?ftag=CAD-01-10abi2f/\">Continue Reading at GameSpot</a> ]]>",
                        pubDate = "Fri, 24 Feb 2023 10:26:00 -0800",
                        author = "Jake Dekker",
                        image = "https://www.gamespot.com/a/uploads/screen_medium/123/1239113/4104338-1211168567-screenshot05.jpg",
                        guid = null,
                        content = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    ),
                    Article(
                        title = "Octopath Traveler 2 Review - Go Your Own Way",
                        link = "https://www.gamespot.com/reviews/octopath-traveler-2-review-go-your-own-way/1900-6418036/?ftag=CAD-01-10abi2f",
                        description = "<![CDATA[ <p dir=\"ltr\">Octopath Traveler was a pleasant surprise when it debuted a few years back. Its then-new HD-2D engine was a delight to behold, and the gameplay drew inspiration from some of Square Enix's most storied franchises: a deep Final Fantasy-style class and customization system mixed with the non-linear exploration and story of the SaGa series with a dash of combat that took cues from Bravely Default. These are great inspirations to draw from, but it resulted in a game that, while excellent, seemed to be struggling for a distinct identity. Perhaps the developers recognized this as well--with Octopath Traveler II, Square Enix seems to be trying to add new gameplay elements that give the franchise a personality of its own. And, for the most part, it has succeeded admirably.</p><p dir=\"ltr\">The core of Octopath Traveler II is a traditional, turn-based JRPG with many of the usual gameplay elements: towns and dungeons to explore, objectives to complete, etc. Where most JRPGs present a linear method of progression, however, Octopath Traveler takes a very different approach: You begin the game by selecting a \"main\" character from eight candidates. This character has their own unique background, story arc, and goals, and will serve as a constant presence throughout your playtime. After an introductory story chapter, you are then free to explore the world to your liking. Eventually, you'll meet the other seven characters, allowing you to bring them into your party and follow their storylines as well, all culminating in a finale that ties the individual story threads together.</p><p dir=\"ltr\">The focus on individual character arcs rather than a huge, high-stakes threat for most of the game's runtime is refreshing, allowing Octopath Traveler II to tell a variety of intriguing stories that vary wildly in both tone and focus. Some of them are comparatively weaker, but others command and hold your attention and keep you eager for more. Agnea's star-struck search for fame is notably bland, for instance, while Temenos' investigation into a murder plot by a religious cult and Throne's quest to kill the adoptive parents who raised her are excellent stand-outs. My personal favorite questline is the story of Osvald, who I chose as my starting character--a tale of a scholar who plans a Count-of-Monte-Christo-style prison escape and revenge after being framed for the murder of his own family by a scheming colleague.</p><a href=\"https://www.gamespot.com/reviews/octopath-traveler-2-review-go-your-own-way/1900-6418036/?ftag=CAD-01-10abi2f/\">Continue Reading at GameSpot</a> ]]>",
                        pubDate = "Wed, 22 Feb 2023 08:47:00 -0800",
                        author = "Heidi Kemps",
                        image = "https://www.gamespot.com/a/uploads/screen_medium/123/1239113/4102547-octopath2.jpg",
                        guid = null,
                        content = null,
                        audio = null,
                        video = null,
                        sourceName = null,
                        sourceUrl = null,
                        categories = emptyList(),
                        itunesArticleData = null,
                        commentsUrl = null
                    )
                ),
                itunesChannelData = null,
                image = null,
                updatePeriod = null
            ),
            listOf(
                GameSpotArticle(
                    title = "Wo Long: Fallen Dynasty Review - Souls-Like Of The Three Kingdoms",
                    author = "Richard Wakeling",
                    description = "<![CDATA[ <p dir=\"ltr\">The first boss fight in Wo Long: Fallen Dynasty is right up there with the toughest first bosses in video game history. This opening battle pits you against Zhang Liang of the Yellow Turbans, as you clash in a kinetic two-phase fight to the death. It's an intense skill check that challenges your prowess of Wo Long's mechanics almost immediately. In many ways, it feels like a rite of passage for the rest of the game and a bold statement of intent from developer Team Ninja. I initially loved how it forced me to adapt to the demands of the game's particular brand of Souls-like combat, yet the further I progressed, the more this feeling dissipated as I realized that this introductory struggle was little more than an unbalanced outlier, providing a much sterner test than the bosses following it.</p><p dir=\"ltr\">For many, this sudden difficulty spike will be a barrier to entry, halting progress a mere 10 minutes into the game. It's a shame Wo Long begins with such a sturdy roadblock, not least because this initial undertaking isn't indicative of the rest of the game moving forward. In fact, outside of this first boss, Team Ninja has crafted one of the more approachable Souls-likes in what is a traditionally challenging genre.</p><p dir=\"ltr\">I didn't encounter another boss fight on par with Zhang Liang's difficulty until roughly 15 hours into Wo Long's campaign. Most of the bosses in between were a relative cakewalk, to the point where I was able to cut down each one on my first attempt--usually in under a minute. I still had fun dispatching every single one, but the ease with which I was able to do so makes them lose some of their luster and reinforces the notion that the first boss is at odds with the rest of the game. The battle with Zhang Liang sets up expectations that never come to fruition, particularly when other fights allow you to summon help from either AI or human teammates.</p><a href=\"https://www.gamespot.com/reviews/wo-long-fallen-dynasty-review-souls-like-of-the-three-kingdoms/1900-6418040/?ftag=CAD-01-10abi2f/\">Continue Reading at GameSpot</a> ]]>",
                    image = "https://www.gamespot.com/a/uploads/screen_medium/43/434805/4106384-wolongthumb.jpg",
                    content = null,
                    sourceLink = "https://www.gamespot.com/reviews/wo-long-fallen-dynasty-review-souls-like-of-the-three-kingdoms/1900-6418040/?ftag=CAD-01-10abi2f",
                    publicationDate = "Thu, 02 Mar 2023 03:00:00 -0800"
                ),
                GameSpotArticle(
                    title = "Tales Of Symphonia Remastered Review - A Classic Regenerated",
                    author = "Jake Dekker",
                    description = "<![CDATA[ <p dir=\"ltr\">Tales of Symphonia was a formative experience for me. For my young 11-year-old brain, it redefined my understanding of the JRPG genre. The vibrant presentation, action-focused combat, and mature story took me by surprise. Weekend after weekend, a friend and I would explore the world of Sylvarant together, making incremental progress in each play session. While I had played a few JRPGs before, none had hooked me the way Tales of Symphonia had.</p><p dir=\"ltr\">Despite my deep reverence for Tales of Symphonia, I haven't touched it since 2004. I don't really know why. I bought it on PC a few years back, but it just felt wrong to play that game sitting at my desk one random evening after work--almost as if it would tarnish the magic of that experience and the memories tied to it. However, with the release of Tales of Symphonia Remastered, I decided it was finally time to return to this world to see if it was as good as I remember. The result was a bit mixed.</p><p dir=\"ltr\">Tales of Symphonia follows a kid named Lloyd Irving as he accompanies the Chosen One on a globetrotting adventure. The Chosen One, Colette, instructed by divine prophecy, must \"regenerate\" the world in order to end war, famine, and hatred. It seems like standard JRPG fare, but the story is darker and far more complex than it initially lets on. Despite trying to do the right thing, Lloyd and his companions are confronted with moral quandaries that often leave a trail of destruction behind them. What makes the story so effective is how it rarely shies away from the consequences of our heroes' actions. Conflicts are rarely resolved neatly, and the story is better for it.</p><a href=\"https://www.gamespot.com/reviews/tales-of-symphonia-remastered-review-a-classic-regenerated/1900-6418037/?ftag=CAD-01-10abi2f/\">Continue Reading at GameSpot</a> ]]>",
                    image = "https://www.gamespot.com/a/uploads/screen_medium/123/1239113/4104338-1211168567-screenshot05.jpg",
                    content = null,
                    sourceLink = "https://www.gamespot.com/reviews/tales-of-symphonia-remastered-review-a-classic-regenerated/1900-6418037/?ftag=CAD-01-10abi2f",
                    publicationDate = "Fri, 24 Feb 2023 10:26:00 -0800"
                ),
                GameSpotArticle(
                    title = "Octopath Traveler 2 Review - Go Your Own Way",
                    author = "Heidi Kemps",
                    description = "<![CDATA[ <p dir=\"ltr\">Octopath Traveler was a pleasant surprise when it debuted a few years back. Its then-new HD-2D engine was a delight to behold, and the gameplay drew inspiration from some of Square Enix's most storied franchises: a deep Final Fantasy-style class and customization system mixed with the non-linear exploration and story of the SaGa series with a dash of combat that took cues from Bravely Default. These are great inspirations to draw from, but it resulted in a game that, while excellent, seemed to be struggling for a distinct identity. Perhaps the developers recognized this as well--with Octopath Traveler II, Square Enix seems to be trying to add new gameplay elements that give the franchise a personality of its own. And, for the most part, it has succeeded admirably.</p><p dir=\"ltr\">The core of Octopath Traveler II is a traditional, turn-based JRPG with many of the usual gameplay elements: towns and dungeons to explore, objectives to complete, etc. Where most JRPGs present a linear method of progression, however, Octopath Traveler takes a very different approach: You begin the game by selecting a \"main\" character from eight candidates. This character has their own unique background, story arc, and goals, and will serve as a constant presence throughout your playtime. After an introductory story chapter, you are then free to explore the world to your liking. Eventually, you'll meet the other seven characters, allowing you to bring them into your party and follow their storylines as well, all culminating in a finale that ties the individual story threads together.</p><p dir=\"ltr\">The focus on individual character arcs rather than a huge, high-stakes threat for most of the game's runtime is refreshing, allowing Octopath Traveler II to tell a variety of intriguing stories that vary wildly in both tone and focus. Some of them are comparatively weaker, but others command and hold your attention and keep you eager for more. Agnea's star-struck search for fame is notably bland, for instance, while Temenos' investigation into a murder plot by a religious cult and Throne's quest to kill the adoptive parents who raised her are excellent stand-outs. My personal favorite questline is the story of Osvald, who I chose as my starting character--a tale of a scholar who plans a Count-of-Monte-Christo-style prison escape and revenge after being framed for the murder of his own family by a scheming colleague.</p><a href=\"https://www.gamespot.com/reviews/octopath-traveler-2-review-go-your-own-way/1900-6418036/?ftag=CAD-01-10abi2f/\">Continue Reading at GameSpot</a> ]]>",
                    image = "https://www.gamespot.com/a/uploads/screen_medium/123/1239113/4102547-octopath2.jpg",
                    content = null,
                    sourceLink = "https://www.gamespot.com/reviews/octopath-traveler-2-review-go-your-own-way/1900-6418036/?ftag=CAD-01-10abi2f",
                    publicationDate = "Wed, 22 Feb 2023 08:47:00 -0800"
                )
            )
        )
    }

    companion object {
        private lateinit var sut: GameSpotDataSource
        private val rssParser = mockk<Parser>()

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sut = GameSpotDataSource(rssParser)
        }
    }
}