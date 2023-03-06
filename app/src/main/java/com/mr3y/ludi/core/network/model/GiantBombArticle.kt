package com.mr3y.ludi.core.network.model

import com.prof.rssparser.Article

data class GiantBombArticle(
    override val title: String?,
    override val author: String?,
    val description: String?,
    val image: String?,
    val content: String?,
    val sourceLink: String?,
    override val publicationDate: String?,
): RSSFeedArticle

fun Article.toGiantBombArticle(): GiantBombArticle {
    return GiantBombArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        image.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull()
    )
}