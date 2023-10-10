package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.ReviewArticle

data class NewsState(
    val isRefreshing: Boolean,
    val newsFeed: Result<Set<NewsArticle>, Throwable>,
    val reviewsFeed: Result<Set<ReviewArticle>, Throwable>,
    val newReleasesFeed: Result<Set<NewReleaseArticle>, Throwable>
)
