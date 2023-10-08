package com.mr3y.ludi.shared

import androidx.datastore.core.DataStore
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.di.ApplicationScope
import com.mr3y.ludi.shared.di.annotations.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

/**
 * A global State Object that holds & provides synchronous access to most common things
 * that are used frequently in different screens of the app.
 */
@Inject
@Singleton
class LudiSharedState(
    private val favGenresStore: DataStore<UserFavouriteGenres>,
    private val appScope: ApplicationScope
) {

    var userFavouriteGenresIds: List<Int>? = null
        private set

    private val userFavGenres = favGenresStore.data
        .catch {
            emit(UserFavouriteGenres())
        }
        .map {
            it.favGenre.map { genre ->
                genre.id
            }.also { ids ->
                userFavouriteGenresIds = ids
            }
        }.launchIn(appScope.value) // trigger flow collection as long as application scope is still active, so, we can
        // continuously react to user preferences changes.

}
