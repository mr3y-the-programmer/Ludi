package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.MarkupText
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.Title
import com.prof.rssparser.Article

data class TechRadarArticle(
    override val title: String?,
    override val author: String?,
    val description: String?,
    override val publicationDate: String?,
    val content: String?,
    val image: String?,
    val sourceLink: String?
) : RSSFeedArticle {
    override fun toNewsArticle(): NewsArticle {
        return NewsArticle(
            id = null,
            title = Title.Markup(title!!),
            description = description?.let { MarkupText(it) },
            thumbnailUrl = null,
            source = Source.TechRadar,
            sourceLinkUrl = sourceLink!!,
            content = content?.let { MarkupText(it) },
            imageUrl = image,
            author = author,
            publicationDate = publicationDate?.toZonedDateTime(),
        )
    }

    override fun toReviewArticle(): ReviewArticle {
        return ReviewArticle(
            title = Title.Markup(title!!),
            description = MarkupText(description!!),
            imageUrl = image,
            source = Source.TechRadar,
            sourceLinkUrl = sourceLink!!,
            content = content?.let { MarkupText(it) },
            author = author,
            publicationDate = publicationDate!!.toZonedDateTime(),
        )
    }

    override fun toNewReleaseArticle(): NewReleaseArticle? = null
}

fun Article.toTechRadarArticle(): TechRadarArticle {
    return TechRadarArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        image.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
    )
}
