package com.mr3y.ludi.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(open val route: String, open val label: String?, open val iconVector: ImageVector?) {

    object Home : Screen("home", "Home", Icons.Outlined.Home)

    object Discover : Screen("discover", "Discover", Icons.Outlined.Search)

    object Deals : Screen("deals", "Deals", Icons.Outlined.LocalOffer)

    object Settings : Screen("settings", "Settings", Icons.Outlined.Settings)
}

val Icons.Outlined.LocalOffer: ImageVector
    get() {
        if (_localOffer != null) {
            return _localOffer!!
        }
        _localOffer = materialIcon(name = "Outlined.LocalOffer") {
            materialPath {
                moveTo(21.41f, 11.58f)
                lineToRelative(-9.0f, -9.0f)
                curveTo(12.05f, 2.22f, 11.55f, 2.0f, 11.0f, 2.0f)
                horizontalLineTo(4.0f)
                curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
                verticalLineToRelative(7.0f)
                curveToRelative(0.0f, 0.55f, 0.22f, 1.05f, 0.59f, 1.42f)
                lineToRelative(9.0f, 9.0f)
                curveToRelative(0.36f, 0.36f, 0.86f, 0.58f, 1.41f, 0.58f)
                reflectiveCurveToRelative(1.05f, -0.22f, 1.41f, -0.59f)
                lineToRelative(7.0f, -7.0f)
                curveToRelative(0.37f, -0.36f, 0.59f, -0.86f, 0.59f, -1.41f)
                reflectiveCurveToRelative(-0.23f, -1.06f, -0.59f, -1.42f)
                close()
                moveTo(13.0f, 20.01f)
                lineTo(4.0f, 11.0f)
                verticalLineTo(4.0f)
                horizontalLineToRelative(7.0f)
                verticalLineToRelative(-0.01f)
                lineToRelative(9.0f, 9.0f)
                lineToRelative(-7.0f, 7.02f)
                close()
            }
            materialPath {
                moveTo(6.5f, 6.5f)
                moveToRelative(-1.5f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, 3.0f, 0.0f)
                arcToRelative(1.5f, 1.5f, 0.0f, true, true, -3.0f, 0.0f)
            }
        }
        return _localOffer!!
    }

private var _localOffer: ImageVector? = null
