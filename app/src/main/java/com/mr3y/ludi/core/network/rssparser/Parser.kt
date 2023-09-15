package com.mr3y.ludi.core.network.rssparser

import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source

interface Parser {

    suspend fun parseNewsArticlesAtUrl(url: String, source: Source): List<NewsArticle?>

    suspend fun parseReviewArticlesAtUrl(url: String, source: Source): List<ReviewArticle?>

    suspend fun parseNewReleaseArticlesAtUrl(url: String, source: Source): List<NewReleaseArticle?>
}
