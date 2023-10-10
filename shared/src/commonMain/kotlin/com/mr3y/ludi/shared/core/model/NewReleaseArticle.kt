package com.mr3y.ludi.shared.core.model

import java.time.ZonedDateTime

data class NewReleaseArticle(
    override val title: Title,
    override val description: MarkupText?,
    override val source: Source,
    override val sourceLinkUrl: String,
    val releaseDate: ZonedDateTime
) : Article {
    override val imageUrl: String? = null
    override val content: MarkupText? = null
    override val author: String? = null
    override val publicationDate: ZonedDateTime = releaseDate
}
