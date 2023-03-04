package com.mr3y.ludi.core.network.model

import com.prof.rssparser.Article

data class IGNArticle(
    override val title: String?,
    override val author: String?,
    val description: String?,
    val content: String?,
    val sourceLink: String?,
    override val publicationDate: String?,
): RSSFeedArticle

fun Article.toIGNArticle(): IGNArticle {
    return IGNArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull()
    )
}
