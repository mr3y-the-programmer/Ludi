package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

data class NewsArticle(
    override val title: Title,
    override val description: MarkupText?,
    override val source: Source,
    override val sourceLinkUrl: String,
    override val content: MarkupText?,
    override val imageUrl: String?,
    override val author: String?,
    override val publicationDate: ZonedDateTime?
) : Article {

    override fun toString(): String {
        return "NewsArticle(title=\"$title\"," +
            "    description=\"$description\",\n" +
            "    source=\"$source\",\n" +
            "    sourceLinkUrl=\"$sourceLinkUrl\",\n" +
            "    content=\"$content\",\n" +
            "    imageUrl=\"$imageUrl\",\n" +
            "    author=\"$author\",\n" +
            "    publicationDate=\"$publicationDate\")"
    }
}
