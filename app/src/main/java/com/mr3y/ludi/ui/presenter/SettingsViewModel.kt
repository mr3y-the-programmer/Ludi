package com.mr3y.ludi.ui.presenter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr3y.ludi.ui.datastore.PreferencesKeys.DynamicColorKey
import com.mr3y.ludi.ui.datastore.PreferencesKeys.SelectedThemeKey
import com.mr3y.ludi.ui.presenter.model.SettingsState
import com.mr3y.ludi.ui.presenter.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) : ViewModel() {

    val settingsState = userPreferences.data
        .catch {
            // TODO: materialize the error by showing snackbar or something & log it
            emit(emptyPreferences())
        }
        .map { preferences ->
            val selectedTheme = preferences[SelectedThemeKey] ?: Theme.SystemDefault.name
            val isUsingDynamicColor = preferences[DynamicColorKey] ?: true
            SettingsState(
                themes = Theme.values().toSet(),
                selectedTheme = Theme.valueOf(selectedTheme),
                isUsingDynamicColor = isUsingDynamicColor
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Initial
        )

    fun setAppTheme(theme: Theme) {
        viewModelScope.launch {
            userPreferences.edit { mutablePreferences ->
                mutablePreferences[SelectedThemeKey] = theme.name
            }
        }
    }

    fun enableUsingDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.edit { mutablePreferences ->
                mutablePreferences[DynamicColorKey] = enabled
            }
        }
    }

    companion object {
        val Initial = SettingsState(
            Theme.values().toSet(),
            null,
            null
        )
    }
}