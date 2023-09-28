package com.mr3y.ludi.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.mr3y.ludi.activityComponent
import com.mr3y.ludi.ui.navigation.PreferencesType
import com.mr3y.ludi.ui.presenter.EditPreferencesViewModel

/**
 * Provide a [ScreenModel] getting from kotlin-inject graph.
 *
 * @return A new instance of [ScreenModel] or the same instance remembered by the composition
 */
@Composable
inline fun <reified T : ScreenModel> Screen.getScreenModel(
    tag: String? = null
): T {
    val context = LocalContext.current
    return rememberScreenModel(tag) {
        val screenModels = context.activityComponent.screenModels
        val model = screenModels[T::class]?.invoke()
            ?: error(
                "${T::class} not found in kotlin-inject graph.\nPlease, check if you have a Multibinding " +
                        "declaration to your ScreenModel using @IntoMap with " +
                        "key ${T::class.qualifiedName}::class)"
            )
        model as T
    }
}

@Composable
inline fun <reified T : ScreenModel, reified P> Screen.getScreenModel(
    tag: String? = null,
    arg1: P
): T {
    val context = LocalContext.current
    return rememberScreenModel(tag) {
        when {
            T::class.java.isAssignableFrom(EditPreferencesViewModel::class.java) -> {
                val prefType = arg1 as? PreferencesType ?: throw IllegalArgumentException("Can't resolve $arg1, make sure to provide PreferencesType argument")
                context.activityComponent.editPreferencesViewModelFactory(prefType) as T
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
