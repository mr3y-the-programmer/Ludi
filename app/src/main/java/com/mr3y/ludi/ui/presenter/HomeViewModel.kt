package com.mr3y.ludi.ui.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.repository.NewsRepository
import com.mr3y.ludi.ui.presenter.model.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState.Default)
    val homeState: StateFlow<HomeState>
        get() = _homeState

    private val sources = setOf(Source.GiantBomb, Source.GameSpot, Source.MMOBomb)

    init {
        viewModelScope.launch {
            val newsResult = viewModelScope.async {
                newsRepository.getLatestGamingNews(sources)
            }
            val reviewsResult = viewModelScope.async {
                newsRepository.getGamesReviews(sources)
            }
            val newReleasesResult = viewModelScope.async {
                newsRepository.getGamesNewReleases(sources)
            }
            val news = newsResult.await()
            val reviews = reviewsResult.await()
            val newReleases = newReleasesResult.await()
            _homeState.update {
                it.copy(
                    newsFeed = news,
                    reviewsFeed = reviews,
                    newReleasesFeed = newReleases,
                )
            }
        }
    }
}