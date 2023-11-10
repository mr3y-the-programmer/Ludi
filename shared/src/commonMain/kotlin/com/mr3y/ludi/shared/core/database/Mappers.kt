package com.mr3y.ludi.shared.core.database

import com.mr3y.ludi.shared.ArticleEntity
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.model.Source
import java.time.ZonedDateTime

fun NewsArticle.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        title,
        description,
        source,
        sourceLinkUrl,
        content,
        imageUrl,
        author,
        publicationDate,
        "news"
    )
}

fun ReviewArticle.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        title,
        description,
        source,
        sourceLinkUrl,
        content,
        imageUrl,
        author,
        publicationDate,
        "reviews"
    )
}

fun NewReleaseArticle.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        title,
        description,
        source,
        sourceLinkUrl,
        content,
        imageUrl,
        author,
        publicationDate,
        "new_releases"
    )
}

fun ArticleEntity.toNewsArticle(): NewsArticle {
    return NewsArticle(
        title = title,
        description = description,
        source = source,
        sourceLinkUrl = sourceLinkUrl,
        content = content,
        imageUrl = imageUrl,
        author = author,
        publicationDate = publicationDate
    )
}

fun ArticleEntity.toReviewArticle(): ReviewArticle {
    return ReviewArticle(
        title = title,
        description = description,
        source = source,
        sourceLinkUrl = sourceLinkUrl,
        content = content,
        imageUrl = imageUrl,
        author = author,
        publicationDate = publicationDate
    )
}

fun ArticleEntity.toNewReleaseArticle(): NewReleaseArticle {
    return NewReleaseArticle(
        title = title,
        description = description,
        source = source,
        sourceLinkUrl = sourceLinkUrl,
        releaseDate = publicationDate!!
    )
}

fun mapToArticleEntity(
    title: String,
    description: String?,
    source: String,
    content: String?,
    author: String?,
    sourceLinkUrl: String,
    imageUrl: String?,
    publicationDate: ZonedDateTime?,
    type: String
): ArticleEntity {
    return ArticleEntity(
        title = TitleColumnAdapter.decode(title),
        description = description?.let { MarkupTextColumnAdapter.decode(it) },
        source = Source.valueOf(source),
        content = content?.let { MarkupTextColumnAdapter.decode(it) },
        author = author,
        sourceLinkUrl = sourceLinkUrl,
        imageUrl = imageUrl,
        publicationDate = publicationDate,
        type = type
    )
}
