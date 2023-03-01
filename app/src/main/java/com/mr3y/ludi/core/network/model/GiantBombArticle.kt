package com.mr3y.ludi.core.network.model

import com.prof.rssparser.Article

data class GiantBombArticle(
    val title: String?,
    val author: String?,
    val description: String?,
    val content: String?,
    val sourceLink: String?,
    val publicationDate: String?,
)

fun Article.toGiantBombArticle(): GiantBombArticle {
    return GiantBombArticle(
        title.ignoreIfEmptyOrNull(),
        author.ignoreIfEmptyOrNull(),
        description.ignoreIfEmptyOrNull(),
        content.ignoreIfEmptyOrNull(),
        link.ignoreIfEmptyOrNull(),
        pubDate.ignoreIfEmptyOrNull()
    )
}