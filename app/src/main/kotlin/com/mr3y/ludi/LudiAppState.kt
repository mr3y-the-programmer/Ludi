package com.mr3y.ludi

import androidx.datastore.core.DataStore
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.di.ApplicationScope
import com.mr3y.ludi.di.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

/**
 * A global State Object that holds & provides synchronous access to most common things
 * used frequently in different screens of the app.
 */
@Inject
@Singleton
class LudiAppState(
    private val favGenresStore: DataStore<UserFavouriteGenres>,
    private val appScope: ApplicationScope
) {

    var userFavouriteGenresIds: List<Int>? = null
        private set

    private val userFavGenres = favGenresStore.data
        .catch {
            emit(UserFavouriteGenres.getDefaultInstance())
        }
        .map {
            it.favGenreList.map { favGenre ->
                favGenre.id
            }.also { ids ->
                userFavouriteGenresIds = ids
            }
        }

    init {
        // trigger flow collection as long as application scope is still active, so, we can
        // continuously react to user preferences changes.
        appScope.value.launch {
            userFavGenres.collect {}
        }
    }
}
