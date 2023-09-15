package com.mr3y.ludi.core.network.rssparser

import com.mr3y.ludi.core.model.Article
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source

class FakeRSSParser : Parser {

    private val cache = hashMapOf<String, List<Article>>()
    private var fail = false

    fun setNewsArticlesAtUrl(url: String, articles: () -> List<NewsArticle>) {
        cache[url] = articles()
    }

    fun setReviewArticlesAtUrl(url: String, articles: () -> List<ReviewArticle>) {
        cache[url] = articles()
    }

    fun setNewReleaseArticlesAtUrl(url: String, articles: () -> List<NewReleaseArticle>) {
        cache[url] = articles()
    }

    override suspend fun parseNewsArticlesAtUrl(url: String, source: Source): List<NewsArticle?> {
        if (fail) {
            throw Exception("Simulated Failure")
        }
        return cache[url] as? List<NewsArticle?> ?: emptyList()
    }

    override suspend fun parseReviewArticlesAtUrl(url: String, source: Source): List<ReviewArticle?> {
        if (fail) {
            throw Exception("Simulated Failure")
        }
        return cache[url] as? List<ReviewArticle?> ?: emptyList()
    }

    override suspend fun parseNewReleaseArticlesAtUrl(url: String, source: Source): List<NewReleaseArticle?> {
        if (fail) {
            throw Exception("Simulated Failure")
        }
        return cache[url] as? List<NewReleaseArticle?> ?: emptyList()
    }

    fun simulateFailure() {
        fail = true
    }

    fun reset() {
        fail = false
        cache.clear()
    }
}
