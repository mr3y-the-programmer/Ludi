package com.mr3y.ludi.core.network.model

import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle

sealed interface RSSFeedArticle {
    val title: String?
    val author: String?
    val publicationDate: String?

    fun toNewsArticle(): NewsArticle?
    fun toNewReleaseArticle(): NewReleaseArticle?
    fun toReviewArticle(): ReviewArticle?
}