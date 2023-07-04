package com.mr3y.ludi.core.network.fixtures

import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.network.datasources.RSSNewReleasesFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSNewsFeedDataSource
import com.mr3y.ludi.core.network.datasources.RSSReviewsFeedDataSource
import com.mr3y.ludi.core.network.model.RSSFeedArticle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

open class CacheResponsesDataSource<T : RSSFeedArticle> :
    RSSNewsFeedDataSource<T>,
    RSSNewReleasesFeedDataSource<T>,
    RSSReviewsFeedDataSource<T> {

    val cache: Map<Feed, List<T>?>
        get() = _cache

    private val _cache: HashMap<Feed, List<T>?> = hashMapOf(Feed.News to null, Feed.Reviews to null, Feed.NewReleases to null)
    private val simulateFailure = MutableStateFlow(false)
    private var failureCause: Exception? = null

    protected open fun newsFeed(): List<T> = throw UnsupportedOperationException("$this isn't a RSS News feed provider")

    protected open fun reviewsFeed(): List<T> = throw UnsupportedOperationException("$this isn't a RSS Reviews feed provider")

    protected open fun newReleasesFeed(): List<T> = throw UnsupportedOperationException("$this isn't a RSS New releases feed provider")

    final override suspend fun fetchNewsFeed(): Result<List<T>, Throwable> {
        return if (_cache[Feed.News] != null) {
            Result.Success(_cache[Feed.News] as List<T>)
        } else {
            delay(300) // simulate network delay
            return if (simulateFailure.value) Result.Error(failureCause) else Result.Success(newsFeed()).also { _cache[Feed.News] = it.data }
        }
    }

    final override suspend fun fetchReviewsFeed(): Result<List<T>, Throwable> {
        return if (_cache[Feed.Reviews] != null) {
            Result.Success(_cache[Feed.Reviews] as List<T>)
        } else {
            delay(300) // simulate network delay
            return if (simulateFailure.value) Result.Error(failureCause) else Result.Success(reviewsFeed()).also { _cache[Feed.Reviews] = it.data }
        }
    }

    final override suspend fun fetchNewReleasesFeed(): Result<List<T>, Throwable> {
        return if (_cache[Feed.NewReleases] != null) {
            Result.Success(_cache[Feed.NewReleases] as List<T>)
        } else {
            delay(300) // simulate network delay
            return if (simulateFailure.value) Result.Error(failureCause) else Result.Success(newReleasesFeed()).also { _cache[Feed.NewReleases] = it.data }
        }
    }

    fun simulateSuccess() {
        simulateFailure.update { false }
    }

    fun simulateFailure(cause: Exception? = null) {
        failureCause = cause
        simulateFailure.update { true }
    }

    fun reset() {
        _cache.putAll(hashMapOf(Feed.News to null, Feed.Reviews to null, Feed.NewReleases to null))
        simulateFailure.update { false }
        failureCause = null
    }
}

enum class Feed {
    News,
    Reviews,
    NewReleases
}
