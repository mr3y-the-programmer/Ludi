package com.mr3y.ludi.shared.core.network.rssparser

import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.model.Source

interface Parser {

    suspend fun parseNewsArticlesAtUrl(url: String, source: Source): List<NewsArticle?>

    suspend fun parseReviewArticlesAtUrl(url: String, source: Source): List<ReviewArticle?>

    suspend fun parseNewReleaseArticlesAtUrl(url: String, source: Source): List<NewReleaseArticle?>
}
