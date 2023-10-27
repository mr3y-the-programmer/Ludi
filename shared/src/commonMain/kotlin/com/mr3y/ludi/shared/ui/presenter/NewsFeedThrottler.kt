package com.mr3y.ludi.shared.ui.presenter

import com.mr3y.ludi.shared.di.annotations.Singleton
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * A simple plain class that has a longer lifetime than our [NewsViewModel] which helps us take correct
 * decisions on when to force a data refresh & request updated feed results from network.
 * For example, when the NewsViewModel gets popped off the backstack and then the user comes back quickly to NewsScreen,
 * we don't want to refresh & request updated data when the previously fetched data is still fresh & valid
 */
@Inject
@Singleton
class NewsFeedThrottler(private val timeSource: TimeSource) {

    private var lastUpdateTimeMark: TimeMark? = null

    fun commitSuccessfulUpdate() {
        lastUpdateTimeMark = timeSource.markNow()
    }

    fun allowRefreshingData() = lastUpdateTimeMark == null || lastUpdateTimeMark!!.elapsedNow() > 30.minutes
}
