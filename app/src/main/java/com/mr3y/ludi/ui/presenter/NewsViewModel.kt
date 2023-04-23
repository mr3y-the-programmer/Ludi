package com.mr3y.ludi.ui.presenter

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.FollowedNewsDataSources
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.NewsArticle
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.core.model.ReviewArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.repository.NewsRepository
import com.mr3y.ludi.ui.presenter.model.NewsState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.wrapResultResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>
) : ViewModel() {

    val newsState: StateFlow<NewsState> = followedNewsDataSourcesStore.data
        .catch {
            emit(FollowedNewsDataSources.getDefaultInstance())
        }
        .mapLatest {
            val sources = it.newsDataSourceList
                .map { followedNewsDataSource -> Source.valueOf(followedNewsDataSource.type) }
                .toSet()
                .ifEmpty { Source.values().toSet() }
            val newsResult = viewModelScope.async {
                newsRepository.getLatestGamingNews(sources).wrapResultResources(
                    transform = { articles -> articles.sort() }
                )
            }
            val reviewsResult = viewModelScope.async {
                newsRepository.getGamesReviews(sources).wrapResultResources(
                    transform = { articles -> articles.sort() }
                )
            }
            val newReleasesResult = viewModelScope.async {
                newsRepository.getGamesNewReleases(sources).wrapResultResources(
                    transform = { articles ->
                        articles
                            .filter { article -> article.releaseDate.isAfter(ZonedDateTime.now(ZoneId.systemDefault())) }
                            .sort()
                    }
                )
            }
            NewsState(
                newsFeed = newsResult.await(),
                reviewsFeed = reviewsResult.await(),
                newReleasesFeed = newReleasesResult.await()
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Initial
        )

    @JvmName("SortNewsArticlesKt")
    private fun Iterable<NewsArticle>.sort(): Iterable<NewsArticle> {
        return sortedWith(
            compareByDescending<NewsArticle> { it.publicationDate }.thenByDescending { it.title.text }
        )
    }

    @JvmName("SortReviewsArticlesKt")
    private fun Iterable<ReviewArticle>.sort(): Iterable<ReviewArticle> {
        return sortedWith(
            compareByDescending<ReviewArticle> { it.publicationDate }.thenByDescending { it.title.text }
        )
    }

    @JvmName("SortNewReleaseArticlesKt")
    private fun Iterable<NewReleaseArticle>.sort(): Iterable<NewReleaseArticle> {
        return sortedWith(
            compareByDescending<NewReleaseArticle> { it.releaseDate }.thenByDescending { it.title.text }
        )
    }
    companion object {
        val Initial = NewsState(
            newsFeed = Result.Success(placeholders()),
            reviewsFeed = Result.Success(placeholders()),
            newReleasesFeed = Result.Success(placeholders())
        )

        private fun placeholders(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}