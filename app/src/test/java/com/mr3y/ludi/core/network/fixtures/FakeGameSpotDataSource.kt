package com.mr3y.ludi.core.network.fixtures

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewReleasesFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedDataSource
import com.mr3y.ludi.core.network.model.GameSpotArticle
import java.util.ArrayDeque

class FakeGameSpotDataSource
: RSSNewsFeedDataSource<GameSpotArticle>, RSSNewReleasesFeedDataSource<GameSpotArticle>, RSSReviewsFeedDataSource<GameSpotArticle> {

    private val newsResponses = ArrayDeque<List<GameSpotArticle>>()
    private var newsError: Result.Error? = null
    private val newReleasesResponses = ArrayDeque<List<GameSpotArticle>>()
    private var newReleasesError: Result.Error? = null
    private val reviewsResponses = ArrayDeque<List<GameSpotArticle>>()
    private var reviewsError: Result.Error? = null

    fun enqueueSuccessfulNewsFeedResponse(response: List<GameSpotArticle>) {
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

    fun enqueueSuccessfulReviewsFeedResponse(response: List<GameSpotArticle>) {
        reviewsResponses.add(response)
    }

    fun clearReviewsResponses() {
        reviewsResponses.clear()
    }

    fun setReviewsErrorResponse(error: Result.Error) {
        reviewsError = error
    }

    fun clearReviewsErrorResponse() {
        reviewsError = null
    }

    fun enqueueSuccessfulNewReleasesFeedResponse(response: List<GameSpotArticle>) {
        newReleasesResponses.add(response)
    }

    fun clearNewReleasesResponses() {
        newReleasesResponses.clear()
    }

    fun setNewReleasesErrorResponse(error: Result.Error) {
        newReleasesError = error
    }

    fun clearNewReleasesErrorResponse() {
        newReleasesError = null
    }

    fun clearAll() {
        clearNewsResponses()
        clearNewReleasesResponses()
        clearReviewsResponses()
        clearNewsErrorResponse()
        clearNewReleasesErrorResponse()
        clearReviewsErrorResponse()
    }

    override suspend fun fetchNewReleasesFeed(): Result<List<GameSpotArticle>, Throwable> {
        return newReleasesError ?: Result.Success(newReleasesResponses.poll()!!)
    }

    override suspend fun fetchNewsFeed(): Result<List<GameSpotArticle>, Throwable> {
        return newsError ?: Result.Success(newsResponses.poll()!!)
    }

    override suspend fun fetchReviewsFeed(): Result<List<GameSpotArticle>, Throwable> {
        return reviewsError ?: Result.Success(reviewsResponses.poll()!!)
    }
}