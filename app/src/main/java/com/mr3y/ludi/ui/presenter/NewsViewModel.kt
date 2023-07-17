package com.mr3y.ludi.ui.presenter

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.FollowedNewsDataSources
import com.mr3y.ludi.core.model.Article
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
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>
) : ViewModel() {

    private val followedNewsDataSources = followedNewsDataSourcesStore.data
        .catch {
            emit(FollowedNewsDataSources.getDefaultInstance())
        }.mapLatest {
            it.newsDataSourceList
                .map { followedNewsDataSource -> Source.valueOf(followedNewsDataSource.type) }
                .toSet()
                .ifEmpty { Source.values().toSet() }
        }

    private val _internalState = MutableStateFlow(InitialNewsState)

    @Suppress("UNCHECKED_CAST")
    val newsState: StateFlow<NewsState> = combine(
        followedNewsDataSources,
        _internalState
    ) { sources, _ ->
        val newsResult = viewModelScope.async {
            newsRepository.getLatestGamingNews(sources).wrapResultResources(
                transform = { articles -> articles.sortByRecent() }
            )
        }
        val reviewsResult = viewModelScope.async {
            newsRepository.getGamesReviews(sources).wrapResultResources(
                transform = { articles -> articles.sortByRecent() }
            )
        }
        val newReleasesResult = viewModelScope.async {
            newsRepository.getGamesNewReleases(sources).wrapResultResources(
                transform = { articles ->
                    articles.sortByRecent(desc = false)
                        .filter { article -> article.releaseDate.isAfter(ZonedDateTime.now()) }
                }
            )
        }
        val (news, reviews, newReleases) = listOf(
            newsResult,
            reviewsResult,
            newReleasesResult
        ).awaitAll()
        _internalState.updateAndGet {
            NewsState(
                isRefreshing = false,
                newsFeed = news as Result<List<ResourceWrapper<NewsArticle>>, Throwable>,
                reviewsFeed = reviews as Result<List<ResourceWrapper<ReviewArticle>>, Throwable>,
                newReleasesFeed = newReleases as Result<List<ResourceWrapper<NewReleaseArticle>>, Throwable>
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        _internalState.value
    )

    fun refresh() {
        _internalState.update { it.copy(isRefreshing = true) }
    }

    internal fun <T: Article> Iterable<T>.sortByRecent(desc: Boolean = true): Iterable<T> {
        return sortedWith(
            if (desc) {
                compareByDescending<T> { it.publicationDate }.thenByDescending { it.title.text }
            } else {
                compareBy<T> { it.publicationDate }.thenBy { it.title.text }
            }
        )
    }

    companion object {
        val InitialNewsState = NewsState(
            isRefreshing = true,
            newsFeed = Result.Success(placeholders()),
            reviewsFeed = Result.Success(placeholders()),
            newReleasesFeed = Result.Success(placeholders())
        )

        private fun placeholders(size: Int = 8): List<ResourceWrapper.Placeholder> {
            return buildList { repeat(size) { add(ResourceWrapper.Placeholder) } }
        }
    }
}
