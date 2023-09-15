package com.mr3y.ludi.core.network.rssparser.internal

import com.mr3y.ludi.core.model.MarkupText
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.Title
import com.mr3y.ludi.core.network.model.toZonedDateTime
import com.mr3y.ludi.core.network.rssparser.Parser
import com.prof.rssparser.Article
import javax.inject.Inject

internal class DefaultParser @Inject constructor(
    private val parser: com.prof.rssparser.Parser
) : Parser {
    override suspend fun parseNewsArticlesAtUrl(url: String, source: Source): List<NewsArticle?> {
        return fetchArticlesAt(url).map { it.toNewsArticle(source) }
    }

    override suspend fun parseReviewArticlesAtUrl(url: String, source: Source): List<ReviewArticle?> {
        return fetchArticlesAt(url).map { it.toReviewArticle(source) }
    }

    override suspend fun parseNewReleaseArticlesAtUrl(url: String, source: Source): List<NewReleaseArticle?> {
        return fetchArticlesAt(url).map { it.toNewReleaseArticle(source) }
    }

    private suspend inline fun fetchArticlesAt(url: String) = parser.getChannel(url).articles

    private fun Article.toNewsArticle(source: Source): NewsArticle? {
        return NewsArticle(
            title = Title.Plain(title ?: return null),
            description = description?.let { MarkupText(it) },
            source = source,
            sourceLinkUrl = link ?: return null,
            content = content?.let { MarkupText(it) },
            imageUrl = image,
            author = author,
            publicationDate = pubDate?.toZonedDateTime()
        )
    }

    private fun Article.toNewReleaseArticle(source: Source): NewReleaseArticle? {
        return NewReleaseArticle(
            title = Title.Plain(title ?: return null),
            description = description?.let { MarkupText(it) },
            source = source,
            sourceLinkUrl = link ?: return null,
            releaseDate = pubDate?.toZonedDateTime() ?: return null
        )
    }

    private fun Article.toReviewArticle(source: Source): ReviewArticle? {
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
}
