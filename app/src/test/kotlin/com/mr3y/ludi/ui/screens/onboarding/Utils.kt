package com.mr3y.ludi.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.mr3y.ludi.ui.screens.getString
import com.mr3y.ludi.ui.screens.hasRole

internal fun ComposeContentTestRule.onGenre(
    @StringRes stateDescRes: Int,
    genreName: String
): SemanticsNodeInteraction {
    return onNode(hasStateDescription(getString(stateDescRes, genreName)) and hasRole(Role.Checkbox))
}
