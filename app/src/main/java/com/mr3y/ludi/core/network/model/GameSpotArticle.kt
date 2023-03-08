package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.*
import com.prof.rssparser.Article

data class GameSpotArticle(
    override val title: String?,
    override val author: String?,
    val description: String?,
    val image: String?,
    val content: String?,
    val sourceLink: String?,
    override val publicationDate: String?,
): RSSFeedArticle {
    override fun toNewsArticle(): NewsArticle {
        return NewsArticle(
            id = null,
            title = Title.Plain(title!!),
            description = MarkupText(description!!),
            thumbnailUrl = null,
            source = Source.GameSpot,
            sourceLinkUrl = sourceLink!!,
            content = null,
            imageUrl = image!!,
            author = author!!,
            publicationDate = publicationDate!!.toZonedDateTime()
        )
    }

    override fun toNewReleaseArticle(): NewReleaseArticle {
        return NewReleaseArticle(
            title = Title.Plain(title!!),
            description = null,
            source = Source.GameSpot,
            sourceLinkUrl = sourceLink!!,
            releaseDate = publicationDate!!.toZonedDateTime(),
        )
    }

    override fun toReviewArticle(): ReviewArticle {
        return ReviewArticle(
            title = Title.Plain(title!!),
            description = MarkupText(description!!),
            source = Source.GameSpot,
            imageUrl = image!!,
            content = null,
            sourceLinkUrl = sourceLink!!,
            author = author!!,
            publicationDate = publicationDate!!.toZonedDateTime(),
        )
    }
}

fun Article.toGameSpotArticle(): GameSpotArticle {
    return GameSpotArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        image.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull()
    )
}
