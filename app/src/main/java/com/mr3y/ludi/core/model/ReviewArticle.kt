package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

data class ReviewArticle(
    override val title: Title,
    val description: MarkupText?,
    val imageUrl: String?,
    val source: Source,
    val content: MarkupText?,
    val sourceLinkUrl: String,
    val author: String?,
    val publicationDate: ZonedDateTime?
) : Article
