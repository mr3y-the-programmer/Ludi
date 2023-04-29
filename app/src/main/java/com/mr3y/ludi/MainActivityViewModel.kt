package com.mr3y.ludi

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.ui.datastore.PreferencesKeys
import com.mr3y.ludi.ui.presenter.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) : ViewModel() {

    val preferences = userPreferences.data
        .catch {
            // TODO: materialize the error by showing snackbar or something & log it
            emit(emptyPreferences())
        }
        .map { preferences ->
            val selectedTheme = preferences[PreferencesKeys.SelectedThemeKey] ?: Theme.SystemDefault.name
            val isUsingDynamicColor = preferences[PreferencesKeys.DynamicColorKey] ?: true
            val showOnBoardingScreen = preferences[PreferencesKeys.OnBoardingScreenKey] ?: true
            UserPreferences(Theme.valueOf(selectedTheme), isUsingDynamicColor, showOnBoardingScreen)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

}

data class UserPreferences(
    val theme: Theme,
    val useDynamicColor: Boolean,
    val showOnBoardingScreen: Boolean
)