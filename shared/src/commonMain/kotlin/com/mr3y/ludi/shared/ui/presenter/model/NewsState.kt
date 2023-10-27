package com.mr3y.ludi.shared.ui.presenter.model

import app.cash.paging.PagingData
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.ReviewArticle
import kotlinx.coroutines.flow.Flow

data class NewsState(
    val isRefreshing: Boolean,
    val newsFeed: Flow<PagingData<NewsArticle>>,
    val reviewsFeed: Flow<PagingData<ReviewArticle>>,
    val newReleasesFeed: Flow<PagingData<NewReleaseArticle>>,
    val currentEvent: NewsStateEvent? = null
)

sealed interface NewsStateEvent {
    data object FailedToFetchNetworkResults : NewsStateEvent
}
