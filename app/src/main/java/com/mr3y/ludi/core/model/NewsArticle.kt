package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

data class NewsArticle(
    val id: Long?,
    override val title: Title,
    val description: MarkupText?,
    val thumbnailUrl: String?,
    val source: Source,
    val sourceLinkUrl: String,
    val content: MarkupText?,
    val imageUrl: String?,
    val author: String?,
    val publicationDate: ZonedDateTime?
): Article {

    override fun toString(): String {
        return "NewsArticle(id=$id," +
                "    title=\"$title\"," +
                "    description=\"$description\",\n" +
                "    thumbnailUrl=\"$thumbnailUrl\",\n" +
                "    source=\"$source\",\n" +
                "    sourceLinkUrl=\"$sourceLinkUrl\",\n" +
                "    content=\"$content\",\n" +
                "    imageUrl=\"$imageUrl\",\n" +
                "    author=\"$author\",\n" +
                "    publicationDate=\"$publicationDate\")"
    }
}
