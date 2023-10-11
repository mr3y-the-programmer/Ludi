package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.mr3y.ludi.ui.screens.hasRole

internal fun ComposeContentTestRule.onGenre(genreStateDesc: String): SemanticsNodeInteraction {
    return onNode(hasStateDescription(genreStateDesc) and hasRole(Role.Checkbox))
}
