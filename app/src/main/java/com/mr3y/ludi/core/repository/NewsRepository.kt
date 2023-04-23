package com.mr3y.ludi.core.repository

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle

interface NewsRepository {
    suspend fun getLatestGamingNews(sources: Set<Source>): Result<Set<NewsArticle>, Throwable>

    suspend fun getGamesNewReleases(sources: Set<Source>): Result<Set<NewReleaseArticle>, Throwable>

    suspend fun getGamesReviews(sources: Set<Source>): Result<Set<ReviewArticle>, Throwable>
}
