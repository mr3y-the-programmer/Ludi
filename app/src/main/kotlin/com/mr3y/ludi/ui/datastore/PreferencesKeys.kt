package com.mr3y.ludi.ui.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val SelectedThemeKey = stringPreferencesKey("selected_theme")
    val DynamicColorKey = booleanPreferencesKey("dynamic_color")
    val OnBoardingScreenKey = booleanPreferencesKey("show_onboarding_screen")
}
