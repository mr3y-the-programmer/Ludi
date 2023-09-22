package com.mr3y.ludi.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.mr3y.ludi.ui.navigation.BottomBarTab
import com.mr3y.ludi.ui.screens.deals.DealsScreenTab
import com.mr3y.ludi.ui.screens.discover.DiscoverScreenTab
import com.mr3y.ludi.ui.screens.news.NewsScreenTab
import com.mr3y.ludi.ui.screens.settings.SettingsScreenTab

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val bottomBarTabs = listOf(
        DiscoverScreenTab,
        NewsScreenTab,
        DealsScreenTab,
        SettingsScreenTab
    )
    Navigator(screen = DiscoverScreenTab) { navigator ->
        Scaffold(
            modifier = modifier,
            bottomBar = {
                if (navigator.lastItem is BottomBarTab) {
                    NavigationBar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bottomBarTabs.forEach { tab ->
                            val (label, iconVector) = tab.label to tab.icon
                            val isSelected = navigator.lastItem == tab
                            val tabWeight by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        navigator.push(tab)
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
}
