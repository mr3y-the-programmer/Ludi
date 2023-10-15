package com.mr3y.ludi.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.mr3y.ludi.datastore.model.UserFavouriteGenres
import com.mr3y.ludi.shared.di.ApplicationScope
import com.mr3y.ludi.shared.di.annotations.Singleton
import com.mr3y.ludi.shared.ui.datastore.PreferencesKeys
import com.mr3y.ludi.shared.ui.presenter.model.Theme
import com.mr3y.ludi.shared.ui.theme.isDynamicColorSupported
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

/**
 * A global State Object that holds & provides synchronous access to most common things
 * that are used frequently in different screens of the app.
 */
@Inject
@Singleton
class LudiSharedState(
    private val userPreferences: DataStore<Preferences>,
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

    val preferences = userPreferences.data
        .catch {
            emit(emptyPreferences())
        }
        .map { preferences ->
            val selectedTheme = preferences[PreferencesKeys.SelectedThemeKey] ?: Theme.SystemDefault.name
            val isUsingDynamicColor = preferences[PreferencesKeys.DynamicColorKey] ?: isDynamicColorSupported()
            val showOnBoardingScreen = preferences[PreferencesKeys.OnBoardingScreenKey] ?: true
            UserPreferences(Theme.valueOf(selectedTheme), isUsingDynamicColor, showOnBoardingScreen)
        }
        .stateIn(
            appScope.value,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}

data class UserPreferences(
    val theme: Theme,
    val useDynamicColor: Boolean,
    val showOnBoardingScreen: Boolean
)
