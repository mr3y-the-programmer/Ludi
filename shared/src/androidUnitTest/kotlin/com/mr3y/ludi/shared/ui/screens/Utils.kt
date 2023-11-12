package com.mr3y.ludi.shared.ui.screens

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.core.app.ApplicationProvider

internal val context: Context = ApplicationProvider.getApplicationContext()

internal fun ComposeContentTestRule.onNodeWithStateDescription(
    stateDescription: String
): SemanticsNodeInteraction {
    return onNode(hasStateDescription(stateDescription))
}

internal fun hasRole(role: Role): SemanticsMatcher = SemanticsMatcher.expectValue(SemanticsProperties.Role, role)
