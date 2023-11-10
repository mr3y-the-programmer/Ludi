package com.mr3y.ludi.shared.ui.presenter

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.paging.cachedIn
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mr3y.ludi.datastore.model.FollowedNewsDataSources
import com.mr3y.ludi.shared.core.model.Source
import com.mr3y.ludi.shared.core.repository.NewsRepository
import com.mr3y.ludi.shared.ui.presenter.model.NewsState
import com.mr3y.ludi.shared.ui.presenter.model.NewsStateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@Inject
class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val followedNewsDataSourcesStore: DataStore<FollowedNewsDataSources>,
    private val throttler: NewsFeedThrottler
) : ScreenModel {

    private val followedNewsDataSources = followedNewsDataSourcesStore.data
        .catch {
            emit(FollowedNewsDataSources())
        }.mapLatest {
            it.newsDataSource
                .map { followedNewsDataSource -> Source.valueOf(followedNewsDataSource.type) }
                .toSet()
                .ifEmpty { Source.values().toSet() }
        }

    val searchQuery = mutableStateOf("")

    private val refreshing = MutableStateFlow(0)

    private val newsArticlesFeed = snapshotFlow { searchQuery.value }
        .debounce(275)
        .flatMapLatest { text ->
            newsRepository.queryLatestGamingNews(text.takeIf { it.length > 3 })
        }.cachedIn(screenModelScope)

    private val reviewArticlesFeed = snapshotFlow { searchQuery.value }
        .debounce(275)
        .flatMapLatest { text ->
            newsRepository.queryGamesReviews(text.takeIf { it.length > 3 })
        }.cachedIn(screenModelScope)

    private val newReleaseArticlesFeed = newsRepository.queryGamesNewReleases().cachedIn(screenModelScope)

    private val _internalState = MutableStateFlow(InitialNewsState)

    val feedResults = combine(followedNewsDataSources, refreshing) { sources, refreshSignal ->
        _internalState.update { it.copy(isRefreshing = true) }
        val shouldForceRefresh = refreshSignal != 0 || throttler.allowRefreshingData()
        val isNewsResultFromNetwork = screenModelScope.async {
            newsRepository.updateGamingNews(sources, forceRefresh = shouldForceRefresh)
        }
        val isReviewsResultFromNetwork = screenModelScope.async {
            newsRepository.updateGamesReviews(sources, forceRefresh = shouldForceRefresh)
        }
        val isNewReleasesResultFromNetwork = screenModelScope.async {
            newsRepository.updateGamesNewReleases(sources, forceRefresh = shouldForceRefresh)
        }
        val (isNewsFromNetwork, isReviewsFromNetwork, isNewReleasesFromNetwork) = listOf(
            isNewsResultFromNetwork,
            isReviewsResultFromNetwork,
            isNewReleasesResultFromNetwork
        ).awaitAll()

        _internalState.update {

            throttler.commitSuccessfulUpdate()

            NewsState(
                isRefreshing = false,
                newsFeed = newsArticlesFeed,
                reviewsFeed = reviewArticlesFeed,
                newReleasesFeed = newReleaseArticlesFeed,
                currentEvent = if ((!isNewsFromNetwork || !isReviewsFromNetwork || !isNewReleasesFromNetwork) && shouldForceRefresh) {
                    NewsStateEvent.FailedToFetchNetworkResults
                } else {
                    null
                }
            )
        }
    }.launchIn(screenModelScope)

    val newsState: StateFlow<NewsState> = _internalState

    fun refresh() {
        refreshing.update { it + 1 }
    }

    fun updateSearchQuery(text: String) {
        searchQuery.value = text
    }

    fun consumeCurrentEvent() {
        _internalState.update { it.copy(currentEvent = null) }
    }

    companion object {
        val InitialNewsState = NewsState(
            isRefreshing = true,
            newsFeed = emptyFlow(),
            reviewsFeed = emptyFlow(),
            newReleasesFeed = emptyFlow()
        )
    }
}
