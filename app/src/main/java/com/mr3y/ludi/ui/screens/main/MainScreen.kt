package com.mr3y.ludi.ui.screens.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mr3y.ludi.ui.navigation.LudiNavHost
import com.mr3y.ludi.ui.navigation.Screen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val bottomBarTabs = listOf(
        Screen.Discover,
        Screen.News,
        Screen.Deals,
        Screen.Settings
    )
    val showBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarTabs.map { it.route }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavigationBar(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottomBarTabs.forEach { screen ->
                        val (label, iconVector) = screen.label!! to screen.iconVector!!
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        val tabWeight by animateFloatAsState(targetValue = if (isSelected) 1.5f else 1f)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
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
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        LudiNavHost(navController = navController, modifier = Modifier.padding(contentPadding))
    }
}
