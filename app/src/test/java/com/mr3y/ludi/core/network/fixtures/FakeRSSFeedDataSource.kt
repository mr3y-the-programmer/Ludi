package com.mr3y.ludi.core.network.fixtures

import com.mr3y.ludi.core.model.Article
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.network.datasources.RSSFeedDataSource
import com.mr3y.ludi.core.network.datasources.internal.RSSFeedSamples
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Suppress("UNCHECKED_CAST")
class FakeRSSFeedDataSource : RSSFeedDataSource {

    val cache: Map<Feed, List<Article>?>
        get() = _cache

    private val _cache: HashMap<Feed, List<Article>?> = hashMapOf(Feed.News to null, Feed.Reviews to null, Feed.NewReleases to null)
    private val simulateFailure = MutableStateFlow(false)
    private var failureCause: Exception? = null
    private val simulatedFailureSources = mutableSetOf<Source>()

    override suspend fun fetchNewsFeed(source: Source): Result<List<NewsArticle>, Throwable> {
        return if (_cache[Feed.News] != null && source !in simulatedFailureSources) {
            Result.Success(_cache[Feed.News] as List<NewsArticle>)
        } else {
            delay(300) // simulate network delay
            return if (simulateFailure.value || source in simulatedFailureSources) {
                Result.Error(failureCause)
            } else {
                Result.Success(RSSFeedSamples.NewsFeed.articles).also { _cache[Feed.News] = it.data } as Result<List<NewsArticle>, Throwable>
            }
        }
    }

    override suspend fun fetchReviewsFeed(source: Source): Result<List<ReviewArticle>, Throwable> {
        return if (_cache[Feed.Reviews] != null && source !in simulatedFailureSources) {
            Result.Success(_cache[Feed.Reviews] as List<ReviewArticle>)
        } else {
            delay(300) // simulate network delay
            return if (simulateFailure.value || source in simulatedFailureSources) {
                Result.Error(failureCause)
            } else {
                Result.Success(RSSFeedSamples.ReviewsFeed.articles).also { _cache[Feed.Reviews] = it.data } as Result<List<ReviewArticle>, Throwable>
            }
        }
    }

    override suspend fun fetchNewReleasesFeed(source: Source): Result<List<NewReleaseArticle>, Throwable> {
        return if (_cache[Feed.NewReleases] != null && source !in simulatedFailureSources) {
            Result.Success(_cache[Feed.NewReleases] as List<NewReleaseArticle>)
        } else {
            delay(300) // simulate network delay
            return if (simulateFailure.value || source in simulatedFailureSources) {
                Result.Error(failureCause)
            } else {
                Result.Success(RSSFeedSamples.NewReleasesFeed.articles).also { _cache[Feed.NewReleases] = it.data } as Result<List<NewReleaseArticle>, Throwable>
            }
        }
    }

    fun simulateSuccess() {
        simulateFailure.update { false }
        simulatedFailureSources.clear()
    }

    fun simulateFailure(cause: Exception? = null) {
        failureCause = cause
        simulateFailure.update { true }
    }

    fun simulateFailureOnDataSource(source: Source, cause: Exception? = null) {
        failureCause = cause
        simulatedFailureSources += source
    }

    fun reset() {
        _cache.putAll(hashMapOf(Feed.News to null, Feed.Reviews to null, Feed.NewReleases to null))
        simulateFailure.update { false }
        failureCause = null
        simulatedFailureSources.clear()
    }
}

enum class Feed {
    News,
    Reviews,
    NewReleases
}
