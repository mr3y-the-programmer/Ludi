package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.model.IGNArticle
import com.prof.rssparser.Article
import com.prof.rssparser.Channel
import com.prof.rssparser.Image
import com.prof.rssparser.Parser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.BeforeClass
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class IGNDataSourceTest {

    @Test
    fun whenFetchingRSSFeedSuccessfully_dataIsTransformedProperly() = runTest {
        // given a mocked request with sample data
        val channel = Channel(
            title = "IGN Video Games",
            link = "https://www.ign.com/articles",
            description = "The latest IGN news, reviews and videos about video games.",
            lastBuildDate = null,
            image = Image(
                title = "IGN Logo",
                url = "https://s3.amazonaws.com/o.assets.images.ign.com/kraken/IGN-Logo-RSS.png",
                link = "https://www.ign.com",
                description = null
            ),
            articles = listOf(
                Article(
                    title = "<![CDATA[ Deal Alert: 1TB Micro SDXC Memory Card (Nintendo Switch Compatible) for \$84.98 ]]>",
                    link = "https://www.ign.com/articles/deal-alert-nintendo-switch-1tb-micro-sdxc-memory-card",
                    description = null,
                    pubDate = "Thu, 2 Mar 2023 01:30:02 +0000",
                    author = "Eric Song",
                    content = "<![CDATA[ <section class=\"article-page\"><p>Amazon is currently a <a href=\"https://zdcs.link/73Ox7\">Micro Center 1TB Micro SDXC card</a> for only \$84.98. That&#39;s one of the least expensive 1TB Micro SDXC cards we&#39;ve seen, and it&#39;s fully compatible with the Nintendo Switch console. </p><section data-transform=\"mobile-ad-break\"></section><p>If you&#39;ve started compiling a collection of digital games, you probably already know just how limited the Switch&#39;s base storage capacity. With only 32GB of starting space (and some of it reserved for the OS), you&#39;ll barely fit The Legend of Zelda: Breath of the Wild and Pokemon Sword or Shield, which tap out at 13.5GB each! There&#39;s only one expansion slot in the Switch console so you want to make sure you get the biggest card you can afford.</p><h2>1TB Micro SDXC Card (Switch Compatible) for \$84.98</h2><p><section data-transform=\"commerce-deal\" data-slug=\"daily-deals-micro-center-micro-sdxc-card\" data-type=\"grid\"></section></p><p>Micro Center is a legitimate company; they have about two dozen retail stores and they&#39;ve been around for over 40 years. This particular brand of Micro SDXC card has over 14,000 reviews on Amazon with an average user score of 4.6/5. The Micro Center &quot;Premium&quot; card has a faster Class U3 rating, which means it boasts a minimum read speed of 30MB/s. Of course if you plan to use this in a Nintendo Switch, it doesn&#39;t even matter. The Nintendo Switch can only support U1 (10MB/s) speeds, so a higher rated U3 speed will just be throttled back down to U1 speeds anyways. So make sure your money goes into maximum storage capacity, not maximum speed.</p><section data-transform=\"object-feedback\"></section><p><strong>Note: </strong>An <a href=\"https://zdcs.link/wmRyD\">official Nintendo Switch branded Micro SDXC card</a> is no more compatible than any other Micro SDXC card with similar specs. Aside from the official licensing, you&#39;re paying extra only for the card&#39;s design. Of course, no one&#39;s going to see the card once installed.</p><h2>Steam Deck Owners Can Use These Cards Too!</h2><a href=\"https://assets-prd.ignimgs.com/2022/11/14/steamdeck-1668459111261.jpg\"><img src=\"https://assets-prd.ignimgs.com/2022/11/14/steamdeck-1668459111261.jpg\" class=\"article-image-full-size\"/></a><p>The Switch isn&#39;t the only gaming system that accepts these cards. If you&#39;re a Steam Deck owner, you can also use this card, especially if you picked up the 64GB storage option. This deal may come around on Black Friday, but we highly doubt that it will be any cheaper. You might as well get it now and never worry about running out of space again. Check out our guide to the <a href=\"https://www.ign.com/articles/best-steam-deck-sd-cards\">best SD cards for Steam Deck</a> for more info.</p><section data-transform=\"divider\"></section><p>For more deals, take a look at our <a href=\"https://www.ign.com/articles/daily-deals-geforce-rtx-4090-gpu-xbox-elite-controller-hisense-4k-tv-ps5-ssd\">daily deals for today</a>.</p></section> ]]>",
                    guid = null,
                    image = "https://assets-prd.ignimgs.com/2023/03/01/columns-accessdenied-column-1677666433954.jpg",
                    audio = null,
                    video = null,
                    sourceName = null,
                    sourceUrl = null,
                    categories = emptyList(),
                    itunesArticleData = null,
                    commentsUrl = null
                ),
                Article(
                    title = "<![CDATA[ AFL 23 Release Date Confirmed, Arriving April ]]>",
                    link = "https://www.ign.com/articles/afl-23-release-date-confirmed-arriving-april",
                    description = "<![CDATA[ AFL and AFLW teams to feature. ]]>",
                    pubDate = "Thu, 2 Mar 2023 00:20:15 +0000",
                    author = "Luke Reilly",
                    content = "<![CDATA[ <section class=\"article-page\"><p>Cricket 22 and AO Tennis developer Big Ant has confirmed AFL 23 will be arriving on PlayStation and Xbox platforms, plus PC, on April 13, 2023.</p><section data-transform=\"mobile-ad-break\"></section><p>The studio also revealed a small selection of new screenshots from the game, which will feature all the clubs and teams from the AFL and AFLW, plus the AFL grounds featured in the 2023 season.</p><section data-transform=\"slideshow\" data-slug=\"afl-23-screenshots-march-2023\" data-value=\"afl-23-screenshots-march-2023\" data-type=\"slug\" data-caption=\"\"></section><p>The Melbourne, Australia-based developer, which has a long history of building cricket, rugby league, and tennis games, previously developed AFL Live for PS3, Xbox 360, and PC in 2011. Over the last decade, AFL for Wii (2011), AFL Live 2 (2013), AFL Evolution (2017), and AFL Evolution 2 (2020) were developed by Wicked Witch Software, which is also based in Melbourne.</p><section data-transform=\"divider\"></section><p><em>Luke is Games Editor at IGN&#39;s Sydney office. You can chat to him on Twitter </em><a href=\"https://twitter.com/MrLukeReilly\"><em><strong>@MrLukeReilly</strong></em></a><em>.</em></p></section> ]]>",
                    guid = null,
                    image = "https://assets-prd.ignimgs.com/2023/03/06/meta-quest-2-price-drop-1678118979942.png",
                    audio = null,
                    video = null,
                    sourceName = null,
                    sourceUrl = null,
                    categories = emptyList(),
                    itunesArticleData = null,
                    commentsUrl = null
                ),
                Article(
                    title = "<![CDATA[ Marc Laidlaw on Publishing Half-Life 3 Plot as Fan-Fiction: 'I Was Deranged' ]]>",
                    link = "https://www.ign.com/articles/marc-laidlaw-on-publishing-half-life-3-plot-fan-fiction",
                    description = "<![CDATA[ Half-Life writer Marc Laidlaw shared his regrets for the trouble that came with publishing his Episode 3 story. ]]>",
                    pubDate = "Wed, 1 Mar 2023 23:33:30 +0000",
                    author = "Andrea Shearon",
                    content = "<![CDATA[ <section class=\"article-page\"><p>Half-Life 2: Episode 3 may not be an official endeavor, but that doesn&#39;t mean series writer Marc Laidlaw hasn&#39;t pondered a universe after Episode 2. However, he seems to regret publishing those thoughts as an unofficial short story.</p><section data-transform=\"mobile-ad-break\"></section><p>In an interview with <a href=\"https://www.rockpapershotgun.com/the-narrative-had-to-be-baked-into-the-corridors-marc-laidlaw-on-writing-half-life\"><u>Rock Paper Shotgun</u></a>, Laidlaw explained why he regrets publishing the 2017 Half-Life fan-fiction on his website. The writer had plans for more adventures in the Half-Life universe but touched on the headaches, embarrassment, and fan confusion that followed his decisions. When noting why he did it, Laidlaw kept it blunt.</p><p>&quot;I was deranged,&quot; Laidlaw told Rock Paper Shotgun. &quot;I was living on an island, totally cut off from my friends and creative community of the last couple decades, I was completely out of touch and had nobody to talk me out of it. It just seemed like a fun thing to do, until I did it.&quot;</p><p>For those not familiar with the Episode 3 debacle, Laidlaw <a href=\"https://www.ign.com/articles/2017/08/25/half-life-2-episode-3-potential-plot-revealed-by-series-writer\"><u>published the &quot;Epistle 3&quot; short story</u></a> to his website in 2017. The unofficial tale arrived just on the heels of Episode 2&#39;s anniversary and was told from the perspective of someone named &quot;Gertie Freemont&quot; with mentions of &quot;Alex Vaunt&quot; – apparent references to Gordon Freeman and Alyx Vance. Though he&#39;s since <a href=\"https://www.ign.com/articles/2017/07/18/ex-valve-writer-discusses-plans-for-unreleased-half-life-games\"><u>discussed plans</u></a> for Half-Life&#39;s future, <a href=\"https://www.ign.com/articles/2016/01/08/half-life-writer-marc-laidlaw-leaves-valve\"><u>Laidlaw left Valve</u></a> in 2016 and was no longer affiliated with the company when publishing Epistle 3.</p><section data-transform=\"object-feedback\"></section><p>Laidlaw told Rock Paper Shotgun he would &quot;have come out the other side a lot less embarrassed&quot; had he refrained from hitting publish. Even after the writer described his enthusiasm, ideas, and creative disagreements over Half-Life, he noted the decision caused trouble for friends at Valve and led hopeful fans to believe Episode 3 could&#39;ve followed his vision.</p><section data-transform=\"ignvideo\" data-slug=\"why-valve-was-semi-afraid-of-fan-reactions-to-half-life-alyxs-ending\" data-loop=\"\"></section><p>Despite the fiasco, Laidlaw did go into brief detail in the interview, noting his ideas continued with Borealis. However, his final thoughts seem pretty clear – those things don&#39;t shape up until development begins.</p><p>Ultimately, Half-Life continued in some capacity with the <a href=\"https://www.ign.com/articles/half-life-alyx-review\"><u>2020 release of Half-Life: Alyx</u></a>, though it&#39;s not the Episode 3 continuation fans are eternally chasing. Since then, other developments on long-canceled Half-Life projects have also trickled into the public eye, including <a href=\"https://www.ign.com/articles/half-life-concept-art-unreleased-spin-off-episode-3\"><u>concept art of potential villains</u></a>.</p><section data-transform=\"divider\"></section><p><em>Andrea Shearon is a freelance contributor for IGN covering games and entertainment. She&#39;s worn several hats over her seven-year career in the games industry, with bylines over at Fanbyte, USA Today&#39;s FTW, TheGamer, VG247, and RPG Site. Find her on Twitter (</em><a href=\"https://twitter.com/Maajora\"><em>@Maajora</em></a><em>) or the Materia Possessions podcast chatting about FFXIV, RPGs, and any series involving giant robots. </em></p></section> ]]>",
                    guid = null,
                    image = "https://assets-prd.ignimgs.com/2023/03/06/mirror-1678110784656.jpg",
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
            updatePeriod = null
        )
        val expectedArticles = listOf(
            IGNArticle(
                title = "<![CDATA[ Deal Alert: 1TB Micro SDXC Memory Card (Nintendo Switch Compatible) for \$84.98 ]]>",
                author = "Eric Song",
                description = null,
                image = "https://assets-prd.ignimgs.com/2023/03/01/columns-accessdenied-column-1677666433954.jpg",
                content = "<![CDATA[ <section class=\"article-page\"><p>Amazon is currently a <a href=\"https://zdcs.link/73Ox7\">Micro Center 1TB Micro SDXC card</a> for only \$84.98. That&#39;s one of the least expensive 1TB Micro SDXC cards we&#39;ve seen, and it&#39;s fully compatible with the Nintendo Switch console. </p><section data-transform=\"mobile-ad-break\"></section><p>If you&#39;ve started compiling a collection of digital games, you probably already know just how limited the Switch&#39;s base storage capacity. With only 32GB of starting space (and some of it reserved for the OS), you&#39;ll barely fit The Legend of Zelda: Breath of the Wild and Pokemon Sword or Shield, which tap out at 13.5GB each! There&#39;s only one expansion slot in the Switch console so you want to make sure you get the biggest card you can afford.</p><h2>1TB Micro SDXC Card (Switch Compatible) for \$84.98</h2><p><section data-transform=\"commerce-deal\" data-slug=\"daily-deals-micro-center-micro-sdxc-card\" data-type=\"grid\"></section></p><p>Micro Center is a legitimate company; they have about two dozen retail stores and they&#39;ve been around for over 40 years. This particular brand of Micro SDXC card has over 14,000 reviews on Amazon with an average user score of 4.6/5. The Micro Center &quot;Premium&quot; card has a faster Class U3 rating, which means it boasts a minimum read speed of 30MB/s. Of course if you plan to use this in a Nintendo Switch, it doesn&#39;t even matter. The Nintendo Switch can only support U1 (10MB/s) speeds, so a higher rated U3 speed will just be throttled back down to U1 speeds anyways. So make sure your money goes into maximum storage capacity, not maximum speed.</p><section data-transform=\"object-feedback\"></section><p><strong>Note: </strong>An <a href=\"https://zdcs.link/wmRyD\">official Nintendo Switch branded Micro SDXC card</a> is no more compatible than any other Micro SDXC card with similar specs. Aside from the official licensing, you&#39;re paying extra only for the card&#39;s design. Of course, no one&#39;s going to see the card once installed.</p><h2>Steam Deck Owners Can Use These Cards Too!</h2><a href=\"https://assets-prd.ignimgs.com/2022/11/14/steamdeck-1668459111261.jpg\"><img src=\"https://assets-prd.ignimgs.com/2022/11/14/steamdeck-1668459111261.jpg\" class=\"article-image-full-size\"/></a><p>The Switch isn&#39;t the only gaming system that accepts these cards. If you&#39;re a Steam Deck owner, you can also use this card, especially if you picked up the 64GB storage option. This deal may come around on Black Friday, but we highly doubt that it will be any cheaper. You might as well get it now and never worry about running out of space again. Check out our guide to the <a href=\"https://www.ign.com/articles/best-steam-deck-sd-cards\">best SD cards for Steam Deck</a> for more info.</p><section data-transform=\"divider\"></section><p>For more deals, take a look at our <a href=\"https://www.ign.com/articles/daily-deals-geforce-rtx-4090-gpu-xbox-elite-controller-hisense-4k-tv-ps5-ssd\">daily deals for today</a>.</p></section> ]]>",
                sourceLink = "https://www.ign.com/articles/deal-alert-nintendo-switch-1tb-micro-sdxc-memory-card",
                publicationDate = "Thu, 2 Mar 2023 01:30:02 +0000"
            ),
            IGNArticle(
                title = "<![CDATA[ AFL 23 Release Date Confirmed, Arriving April ]]>",
                author = "Luke Reilly",
                description = "<![CDATA[ AFL and AFLW teams to feature. ]]>",
                image = "https://assets-prd.ignimgs.com/2023/03/06/meta-quest-2-price-drop-1678118979942.png",
                content = "<![CDATA[ <section class=\"article-page\"><p>Cricket 22 and AO Tennis developer Big Ant has confirmed AFL 23 will be arriving on PlayStation and Xbox platforms, plus PC, on April 13, 2023.</p><section data-transform=\"mobile-ad-break\"></section><p>The studio also revealed a small selection of new screenshots from the game, which will feature all the clubs and teams from the AFL and AFLW, plus the AFL grounds featured in the 2023 season.</p><section data-transform=\"slideshow\" data-slug=\"afl-23-screenshots-march-2023\" data-value=\"afl-23-screenshots-march-2023\" data-type=\"slug\" data-caption=\"\"></section><p>The Melbourne, Australia-based developer, which has a long history of building cricket, rugby league, and tennis games, previously developed AFL Live for PS3, Xbox 360, and PC in 2011. Over the last decade, AFL for Wii (2011), AFL Live 2 (2013), AFL Evolution (2017), and AFL Evolution 2 (2020) were developed by Wicked Witch Software, which is also based in Melbourne.</p><section data-transform=\"divider\"></section><p><em>Luke is Games Editor at IGN&#39;s Sydney office. You can chat to him on Twitter </em><a href=\"https://twitter.com/MrLukeReilly\"><em><strong>@MrLukeReilly</strong></em></a><em>.</em></p></section> ]]>",
                sourceLink = "https://www.ign.com/articles/afl-23-release-date-confirmed-arriving-april",
                publicationDate = "Thu, 2 Mar 2023 00:20:15 +0000"
            ),
            IGNArticle(
                title = "<![CDATA[ Marc Laidlaw on Publishing Half-Life 3 Plot as Fan-Fiction: 'I Was Deranged' ]]>",
                author = "Andrea Shearon",
                description = "<![CDATA[ Half-Life writer Marc Laidlaw shared his regrets for the trouble that came with publishing his Episode 3 story. ]]>",
                image = "https://assets-prd.ignimgs.com/2023/03/06/mirror-1678110784656.jpg",
                content = "<![CDATA[ <section class=\"article-page\"><p>Half-Life 2: Episode 3 may not be an official endeavor, but that doesn&#39;t mean series writer Marc Laidlaw hasn&#39;t pondered a universe after Episode 2. However, he seems to regret publishing those thoughts as an unofficial short story.</p><section data-transform=\"mobile-ad-break\"></section><p>In an interview with <a href=\"https://www.rockpapershotgun.com/the-narrative-had-to-be-baked-into-the-corridors-marc-laidlaw-on-writing-half-life\"><u>Rock Paper Shotgun</u></a>, Laidlaw explained why he regrets publishing the 2017 Half-Life fan-fiction on his website. The writer had plans for more adventures in the Half-Life universe but touched on the headaches, embarrassment, and fan confusion that followed his decisions. When noting why he did it, Laidlaw kept it blunt.</p><p>&quot;I was deranged,&quot; Laidlaw told Rock Paper Shotgun. &quot;I was living on an island, totally cut off from my friends and creative community of the last couple decades, I was completely out of touch and had nobody to talk me out of it. It just seemed like a fun thing to do, until I did it.&quot;</p><p>For those not familiar with the Episode 3 debacle, Laidlaw <a href=\"https://www.ign.com/articles/2017/08/25/half-life-2-episode-3-potential-plot-revealed-by-series-writer\"><u>published the &quot;Epistle 3&quot; short story</u></a> to his website in 2017. The unofficial tale arrived just on the heels of Episode 2&#39;s anniversary and was told from the perspective of someone named &quot;Gertie Freemont&quot; with mentions of &quot;Alex Vaunt&quot; – apparent references to Gordon Freeman and Alyx Vance. Though he&#39;s since <a href=\"https://www.ign.com/articles/2017/07/18/ex-valve-writer-discusses-plans-for-unreleased-half-life-games\"><u>discussed plans</u></a> for Half-Life&#39;s future, <a href=\"https://www.ign.com/articles/2016/01/08/half-life-writer-marc-laidlaw-leaves-valve\"><u>Laidlaw left Valve</u></a> in 2016 and was no longer affiliated with the company when publishing Epistle 3.</p><section data-transform=\"object-feedback\"></section><p>Laidlaw told Rock Paper Shotgun he would &quot;have come out the other side a lot less embarrassed&quot; had he refrained from hitting publish. Even after the writer described his enthusiasm, ideas, and creative disagreements over Half-Life, he noted the decision caused trouble for friends at Valve and led hopeful fans to believe Episode 3 could&#39;ve followed his vision.</p><section data-transform=\"ignvideo\" data-slug=\"why-valve-was-semi-afraid-of-fan-reactions-to-half-life-alyxs-ending\" data-loop=\"\"></section><p>Despite the fiasco, Laidlaw did go into brief detail in the interview, noting his ideas continued with Borealis. However, his final thoughts seem pretty clear – those things don&#39;t shape up until development begins.</p><p>Ultimately, Half-Life continued in some capacity with the <a href=\"https://www.ign.com/articles/half-life-alyx-review\"><u>2020 release of Half-Life: Alyx</u></a>, though it&#39;s not the Episode 3 continuation fans are eternally chasing. Since then, other developments on long-canceled Half-Life projects have also trickled into the public eye, including <a href=\"https://www.ign.com/articles/half-life-concept-art-unreleased-spin-off-episode-3\"><u>concept art of potential villains</u></a>.</p><section data-transform=\"divider\"></section><p><em>Andrea Shearon is a freelance contributor for IGN covering games and entertainment. She&#39;s worn several hats over her seven-year career in the games industry, with bylines over at Fanbyte, USA Today&#39;s FTW, TheGamer, VG247, and RPG Site. Find her on Twitter (</em><a href=\"https://twitter.com/Maajora\"><em>@Maajora</em></a><em>) or the Materia Possessions podcast chatting about FFXIV, RPGs, and any series involving giant robots. </em></p></section> ]]>",
                sourceLink = "https://www.ign.com/articles/marc-laidlaw-on-publishing-half-life-3-plot-fan-fiction",
                publicationDate = "Wed, 1 Mar 2023 23:33:30 +0000"
            )
        )
        coEvery { rssParser.getChannel(IGNDataSource.RSSNewsFeedURL) } returns channel

        // when trying to fetch rss feed
        val result = sut.fetchNewsFeed()

        // then assert we got the sample data parsed & transformed to our typed model
        expectThat(result).isA<Result.Success<List<IGNArticle>>>()
        result as Result.Success
        expectThat(result.data).isEqualTo(expectedArticles)
    }

    @Test
    fun whenFailingToFetchRSSFeed_exceptionIsWrapped() = runTest {
        // Assuming there is a failure while trying to fetch rss feed
        coEvery { rssParser.getChannel(IGNDataSource.RSSNewsFeedURL) } throws Exception()

        // when trying to fetch rss feed
        val result = sut.fetchNewsFeed()

        // then assert the exception is caught and wrapped into a typed Result.Error
        expectThat(result).isA<Result.Error>()
    }

    companion object {

        private lateinit var sut: IGNDataSource
        private val rssParser = mockk<Parser>()

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sut = IGNDataSource(rssParser)
        }
    }
}
