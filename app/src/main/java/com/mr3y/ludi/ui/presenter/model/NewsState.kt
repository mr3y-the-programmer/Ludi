package com.mr3y.ludi.ui.presenter.model

import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle

data class NewsState(
    val isRefreshing: Boolean,
    val newsFeed: Result<List<ResourceWrapper<NewsArticle>>, Throwable>,
    val reviewsFeed: Result<List<ResourceWrapper<ReviewArticle>>, Throwable>,
    val newReleasesFeed: Result<List<ResourceWrapper<NewReleaseArticle>>, Throwable>
)
