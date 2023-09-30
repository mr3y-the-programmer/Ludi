package com.mr3y.ludi.shared.core.model

import com.mr3y.ludi.shared.core.time.ZonedDateTime

data class ReviewArticle(
    override val title: Title,
    override val description: MarkupText?,
    override val imageUrl: String?,
    override val source: Source,
    override val content: MarkupText?,
    override val sourceLinkUrl: String,
    override val author: String?,
    override val publicationDate: ZonedDateTime?
) : Article
