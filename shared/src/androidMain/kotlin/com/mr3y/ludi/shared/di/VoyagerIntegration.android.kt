package com.mr3y.ludi.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.DealsViewModel
import com.mr3y.ludi.shared.ui.presenter.DiscoverViewModel
import com.mr3y.ludi.shared.ui.presenter.EditPreferencesViewModel
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel
import com.mr3y.ludi.shared.ui.presenter.OnBoardingViewModel
import com.mr3y.ludi.shared.ui.presenter.SettingsViewModel

/**
 * Provide a [ScreenModel] getting from kotlin-inject graph.
 *
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
actual inline fun <reified T : ScreenModel> Screen.getScreenModel(
    tag: String?
): T {
    val hostActivityComponent = (LocalContext.current as HostActivityComponentOwner).hostActivityComponent
    return rememberScreenModel(tag) {
        when {
            T::class.java.isAssignableFrom(OnBoardingViewModel::class.java) -> {
                OnboardingFeatureComponent::class.create(hostActivityComponent).bind as T
            }
            T::class.java.isAssignableFrom(DiscoverViewModel::class.java) -> {
                DiscoverFeatureComponent::class.create(hostActivityComponent).bind as T
            }
            T::class.java.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsFeatureComponent::class.create(hostActivityComponent).bind as T
            }
            T::class.java.isAssignableFrom(DealsViewModel::class.java) -> {
                DealsFeatureComponent::class.create(hostActivityComponent).bind as T
            }
            T::class.java.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsFeatureComponent::class.create(hostActivityComponent).bind as T
            }
            else -> {
                error(
                    "${T::class} not found in kotlin-inject graph.\nPlease, check if you have declared a Feature Component " +
                        "for your ScreenModel & its required dependencies"
                )
            }
        }
    }
}

@Composable
actual inline fun <reified T : ScreenModel, reified P> Screen.getScreenModel(
    tag: String?,
    arg1: P
): T {
    val hostActivityComponent = (LocalContext.current as HostActivityComponentOwner).hostActivityComponent
    return rememberScreenModel(tag) {
        when {
            T::class.java.isAssignableFrom(EditPreferencesViewModel::class.java) -> {
                val prefType = arg1 as? PreferencesType ?: throw IllegalArgumentException("Can't resolve $arg1, make sure to provide PreferencesType argument")
                hostActivityComponent.editPreferencesViewModelFactory(prefType) as T
            }
            else -> {
                error(
                    "${T::class.java} not found in kotlin-inject graph.\nPlease, make sure that you've declared your assisted ScreenModel " +
                        "in kotlin-inject graph"
                )
            }
        }
    }
}
