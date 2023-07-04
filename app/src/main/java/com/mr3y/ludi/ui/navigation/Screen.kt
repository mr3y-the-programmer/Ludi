package com.mr3y.ludi.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(open val route: String, open val label: String?, open val iconVector: ImageVector?) {

    object News : Screen("news", "News", Icons.Outlined.Upcoming)

    object Discover : Screen("discover", "Discover", Icons.Outlined.Search)

    object Deals : Screen("deals", "Deals", Icons.Outlined.LocalOffer)

    object Settings : Screen("settings", "Settings", Icons.Outlined.Settings)
}
