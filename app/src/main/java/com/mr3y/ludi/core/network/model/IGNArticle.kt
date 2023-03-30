package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.*
import com.prof.rssparser.Article

data class IGNArticle(
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
            title = Title.Markup(title!!),
            description = if (description != null) MarkupText(description) else null,
            thumbnailUrl = null,
            source = Source.IGN,
            sourceLinkUrl = sourceLink!!,
            content = if (content != null) MarkupText(content) else null,
            imageUrl = image,
            author = author,
            publicationDate = publicationDate?.toZonedDateTime()
        )
    }

    override fun toNewReleaseArticle(): NewReleaseArticle? = null

    override fun toReviewArticle(): ReviewArticle? = null
}

fun Article.toIGNArticle(): IGNArticle {
    return IGNArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        image.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull()
    )
}
