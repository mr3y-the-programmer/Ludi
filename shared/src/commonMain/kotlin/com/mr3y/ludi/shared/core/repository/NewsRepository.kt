package com.mr3y.ludi.shared.core.repository

import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.model.Source

interface NewsRepository {
    suspend fun getLatestGamingNews(sources: Set<Source>): Result<Set<NewsArticle>, Throwable>

    suspend fun getGamesNewReleases(sources: Set<Source>): Result<Set<NewReleaseArticle>, Throwable>

    suspend fun getGamesReviews(sources: Set<Source>): Result<Set<ReviewArticle>, Throwable>
}
