package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.MarkupText
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.Title
import com.prof.rssparser.Article

fun Article.toNewsArticle(source: Source): NewsArticle? {
    return NewsArticle(
        id = null,
        title = Title.Plain(title ?: return null),
        description = description?.let { MarkupText(it) },
        thumbnailUrl = null,
        source = source,
        sourceLinkUrl = link ?: return null,
        content = content?.let { MarkupText(it) },
        imageUrl = image,
        author = author,
        publicationDate = pubDate?.toZonedDateTime()
    )
}

fun Article.toNewReleaseArticle(source: Source): NewReleaseArticle? {
    return NewReleaseArticle(
        title = Title.Plain(title ?: return null),
        description = description?.let { MarkupText(it) },
        source = source,
        sourceLinkUrl = link ?: return null,
        releaseDate = pubDate?.toZonedDateTime() ?: return null
    )
}

fun Article.toReviewArticle(source: Source): ReviewArticle? {
    return ReviewArticle(
        title = Title.Plain(title ?: return null),
        description = description?.let { MarkupText(it) },
        source = source,
        imageUrl = image,
        content = content?.let { MarkupText(it) },
        sourceLinkUrl = link ?: return null,
        author = author,
        publicationDate = pubDate?.toZonedDateTime()
    )
}
