package com.mr3y.ludi.shared.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.mr3y.ludi.shared.ui.adaptive.LocalWindowSizeClass
import com.mr3y.ludi.shared.ui.components.LudiNavigationRail
import com.mr3y.ludi.shared.ui.navigation.BottomBarTab
import com.mr3y.ludi.shared.ui.screens.deals.DealsScreenTab
import com.mr3y.ludi.shared.ui.screens.discover.DiscoverScreenTab
import com.mr3y.ludi.shared.ui.screens.news.NewsScreenTab
import com.mr3y.ludi.shared.ui.screens.settings.SettingsScreenTab

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val tabs = listOf(
        DiscoverScreenTab,
        NewsScreenTab,
        DealsScreenTab,
        SettingsScreenTab
    )
    val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
    val useNavRail = widthSizeClass >= WindowWidthSizeClass.Medium && widthSizeClass < WindowWidthSizeClass.Expanded
    val useNavDrawer = widthSizeClass >= WindowWidthSizeClass.Expanded
    Navigator(screen = DiscoverScreenTab) { navigator ->
        if (useNavDrawer) {
            Expanded(navigator, tabs, modifier)
        } else if (useNavRail) {
            Medium(navigator, tabs, modifier)
        } else {
            Compact(navigator, tabs, modifier)
        }
    }
}

@Composable
fun Compact(
    navigator: Navigator,
    bottomBarTabs: List<BottomBarTab>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (navigator.lastItem is BottomBarTab) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottomBarTabs.forEach { tab ->
                        val (label, iconVector) = tab.label to tab.icon
                        val isSelected = navigator.lastItem == tab as Screen
                        val tabWeight by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navigator.push(tab as Screen)
                                }
                            },
                            label = { Text(text = label) },
                            icon = { Icon(iconVector, null) },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                            ),
                            modifier = Modifier.weight(tabWeight)
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(
                    ScaffoldDefaults.contentWindowInsets.exclude(
                        NavigationBarDefaults.windowInsets
                    )
                ),
            propagateMinConstraints = true
        ) {
            FadeTransition(navigator = navigator)
        }
    }
}

@Composable
fun Medium(
    navigator: Navigator,
    tabs: List<BottomBarTab>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        LudiNavigationRail(
            modifier = Modifier.fillMaxHeight()
        ) {
            tabs.forEachIndexed { index, tab ->
                val (label, iconVector) = tab.label to tab.icon
                val isSelected = navigator.lastItem == tab as Screen
                NavigationRailItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navigator.push(tab as Screen)
                        }
                    },
                    label = { Text(text = label) },
                    icon = { Icon(iconVector, null) },
                    colors = NavigationRailItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                    )
                )
                if (index != tabs.lastIndex) {
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
        Box(
            modifier = Modifier.consumeWindowInsets(
                WindowInsets.systemBars.only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
            ),
            propagateMinConstraints = true
        ) {
            FadeTransition(navigator = navigator)
        }
    }
}

@Composable
fun Expanded(
    navigator: Navigator,
    tabs: List<BottomBarTab>,
    modifier: Modifier = Modifier
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(240.dp).fillMaxHeight(),
                drawerShape = MaterialTheme.shapes.large
            ) {
                Spacer(Modifier.height(12.dp))
                tabs.forEach { tab ->
                    val (label, iconVector) = tab.label to tab.icon
                    val isSelected = navigator.lastItem == tab as Screen
                    NavigationDrawerItem(
                        icon = { Icon(iconVector, contentDescription = null) },
                        label = { Text(label) },
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navigator.push(tab as Screen)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.consumeWindowInsets(
                WindowInsets.systemBars.only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
            ),
            propagateMinConstraints = true
        ) {
            FadeTransition(navigator = navigator)
        }
    }
}
