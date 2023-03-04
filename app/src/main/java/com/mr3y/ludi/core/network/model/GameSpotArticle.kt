package com.mr3y.ludi.core.network.model

import com.prof.rssparser.Article

data class GameSpotArticle(
    override val title: String?,
    override val author: String?,
    val description: String?,
    val content: String?,
    val sourceLink: String?,
    override val publicationDate: String?,
): RSSFeedArticle

fun Article.toGameSpotArticle(): GameSpotArticle {
    return GameSpotArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull()
    )
}
