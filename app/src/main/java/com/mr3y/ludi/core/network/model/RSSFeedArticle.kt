package com.mr3y.ludi.core.network.model

sealed interface RSSFeedArticle {
    val title: String?
    val author: String?
    val publicationDate: String?
}