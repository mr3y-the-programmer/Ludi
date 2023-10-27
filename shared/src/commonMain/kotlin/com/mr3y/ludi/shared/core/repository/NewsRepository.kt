package com.mr3y.ludi.shared.core.repository

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import com.mr3y.ludi.shared.core.model.Source
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun queryLatestGamingNews(): Flow<PagingData<NewsArticle>>

    fun queryGamesNewReleases(): Flow<PagingData<NewReleaseArticle>>

    fun queryGamesReviews(): Flow<PagingData<ReviewArticle>>


    suspend fun updateGamingNews(sources: Set<Source>, forceRefresh: Boolean): Boolean

    suspend fun updateGamesNewReleases(sources: Set<Source>, forceRefresh: Boolean): Boolean

    suspend fun updateGamesReviews(sources: Set<Source>, forceRefresh: Boolean): Boolean
}
