package com.mr3y.ludi.ui.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mr3y.ludi.ui.screens.deals.DealsScreen
import com.mr3y.ludi.ui.screens.discover.DiscoverScreen
import com.mr3y.ludi.ui.screens.news.NewsScreen
import com.mr3y.ludi.ui.screens.settings.EditPreferencesScreen
import com.mr3y.ludi.ui.screens.settings.SettingsScreen

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
            NewsScreen(
                onTuneClick = {
                    navController.navigate("${Screen.EditPreferences.route}/${PreferencesType.NewsDataSources}") {
                        popUpTo(Screen.News.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }
        composable(Screen.Deals.route) {
            DealsScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onFollowedNewsDataSourcesClick = {
                    navController.navigate("${Screen.EditPreferences.route}/${PreferencesType.NewsDataSources}")
                },
                onFavouriteGamesClick = {
                    navController.navigate("${Screen.EditPreferences.route}/${PreferencesType.Games}")
                },
                onFavouriteGenresClick = {
                    navController.navigate("${Screen.EditPreferences.route}/${PreferencesType.Genres}")
                }
            )
        }
        composable(
            route = "${Screen.EditPreferences.route}/{type}",
            arguments = listOf(navArgument("type") { type = NavType.EnumType(PreferencesType::class.java)})
        ) { backStackEntry ->
            val type = if (Build.VERSION.SDK_INT < 33) {
                backStackEntry.arguments?.getSerializable("type") as PreferencesType
            } else {
                backStackEntry.arguments?.getSerializable("type", PreferencesType::class.java)!!
            }
            EditPreferencesScreen(
                type = type,
                onDoneClick = { navController.navigateUp() }
            )
        }
    }
}
