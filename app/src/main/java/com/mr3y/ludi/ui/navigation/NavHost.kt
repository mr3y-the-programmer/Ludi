package com.mr3y.ludi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mr3y.ludi.ui.screens.deals.DealsScreen
import com.mr3y.ludi.ui.screens.discover.DiscoverScreen
import com.mr3y.ludi.ui.screens.news.NewsScreen

@Composable
fun LudiNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Discover.route,
        modifier = modifier
    ) {
        composable(Screen.Discover.route) {
            DiscoverScreen()
        }
        composable(Screen.News.route) {
            NewsScreen()
        }
        composable(Screen.Deals.route) {
            DealsScreen()
        }
        composable(Screen.Settings.route) {

        }
    }
}