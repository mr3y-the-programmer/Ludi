package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

data class NewReleaseArticle(
    override val title: Title,
    val description: MarkupText?,
    val source: Source,
    val sourceLinkUrl: String,
    val releaseDate: ZonedDateTime
): Article
