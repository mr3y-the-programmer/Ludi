package com.mr3y.ludi.ui.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        Screen.Home,
        Screen.Discover,
        Screen.Deals,
        Screen.Settings,
    )
    val showBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarTabs.map { it.route }
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                BottomNavigation(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    bottomBarTabs.forEach { screen ->
                        val (label, iconVector) = screen.label!! to screen.iconVector!!
                        BottomNavigationItem(
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        LudiNavHost(navController = navController, modifier = Modifier.padding(contentPadding))
    }
}