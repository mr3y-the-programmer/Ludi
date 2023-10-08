package com.mr3y.ludi.shared.ui.presenter.model

import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.core.model.NewsArticle
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.core.model.ReviewArticle

data class NewsState(
    val isRefreshing: Boolean,
    val newsFeed: Result<List<NewsArticle>, Throwable>,
    val reviewsFeed: Result<List<ReviewArticle>, Throwable>,
    val newReleasesFeed: Result<List<NewReleaseArticle>, Throwable>
)
