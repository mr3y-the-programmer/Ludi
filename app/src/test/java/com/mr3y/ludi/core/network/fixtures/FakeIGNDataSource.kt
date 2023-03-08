package com.mr3y.ludi.core.network.fixtures

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.model.IGNArticle
import java.util.ArrayDeque

class FakeIGNDataSource : RSSNewsFeedDataSource<IGNArticle> {

    private val newsResponses = ArrayDeque<List<IGNArticle>>()
    private var newsError: Result.Error? = null

    fun enqueueSuccessfulNewsFeedResponse(response: List<IGNArticle>) {
        newsResponses.add(response)
    }

    fun clearNewsResponses() {
        newsResponses.clear()
    }

    fun setNewsErrorResponse(error: Result.Error) {
        newsError = error
    }

    fun clearNewsErrorResponse() {
        newsError = null
    }

    fun clearAll() {
        clearNewsResponses()
        clearNewsErrorResponse()
    }

    override suspend fun fetchNewsFeed(): Result<List<IGNArticle>, Throwable> {
        return newsError ?: Result.Success(newsResponses.poll()!!)
    }
}