package com.mr3y.ludi.ui.screens.onboarding

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.core.app.ApplicationProvider

internal val context: Context = ApplicationProvider.getApplicationContext()

@JvmOverloads
fun getString(@StringRes resId: Int, formatArg1: Any? = null, formatArg2: Any? = null, formatArg3: Any? = null, formatArg4: Any? = null): String {
    return context.resources.getString(resId, formatArg1, formatArg2, formatArg3, formatArg4)
}

internal fun ComposeContentTestRule.onNodeWithStateDescription(
    stateDescription: String
): SemanticsNodeInteraction {
    return onNode(hasStateDescription(stateDescription))
}

internal fun ComposeContentTestRule.onGenre(
    @StringRes stateDescRes: Int,
    genreName: String
): SemanticsNodeInteraction {
    return onNode(hasStateDescription(getString(stateDescRes, genreName)) and hasRole(Role.Checkbox))
}

internal fun hasRole(role: Role): SemanticsMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)
