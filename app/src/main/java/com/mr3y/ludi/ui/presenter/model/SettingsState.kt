package com.mr3y.ludi.ui.presenter.model

data class SettingsState(
    val themes: Set<Theme>,
    val selectedTheme: Theme?,
    val isUsingDynamicColor: Boolean?,
)

enum class Theme(val label: String) {
    Light("Light"),
    Dark("Dark"),
    SystemDefault("System Default")
}
