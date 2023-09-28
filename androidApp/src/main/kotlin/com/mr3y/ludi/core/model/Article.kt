package com.mr3y.ludi.core.model

import java.time.ZonedDateTime

sealed interface Article {
    val title: Title
    val description: MarkupText?
    val imageUrl: String?
    val content: MarkupText?
    val author: String?
    val source: Source
    val sourceLinkUrl: String
    val publicationDate: ZonedDateTime?
}

sealed interface Title {
    val text: String
    data class Markup(override val text: String) : Title
    data class Plain(override val text: String) : Title
}
