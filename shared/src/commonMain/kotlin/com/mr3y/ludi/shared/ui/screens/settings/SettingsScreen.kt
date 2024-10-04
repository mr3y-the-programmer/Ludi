package com.mr3y.ludi.shared.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mr3y.ludi.shared.di.getScreenModel
import com.mr3y.ludi.shared.ui.navigation.BottomBarTab
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.SettingsViewModel
import com.mr3y.ludi.shared.ui.presenter.model.SettingsState
import com.mr3y.ludi.shared.ui.presenter.model.Theme
import com.mr3y.ludi.shared.ui.theme.isDynamicColorSupported

object SettingsScreenTab : Screen, BottomBarTab {

    override val key: ScreenKey
        get() = "settings"
    override val label: String
        get() = "Settings"
    override val icon: ImageVector
        get() = Icons.Outlined.Settings

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SettingsViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        SettingsScreen(
            onFollowedNewsDataSourcesClick = { navigator.push(EditPreferencesScreen(PreferencesType.NewsDataSources)) },
            onFavouriteGamesClick = { navigator.push(EditPreferencesScreen(PreferencesType.Games)) },
            onFavouriteGenresClick = { navigator.push(EditPreferencesScreen(PreferencesType.Genres)) },
            viewModel = screenModel
        )
    }
}

@Composable
expect fun SettingsScreen(
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
)

@Composable
fun SettingsScreen(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    onUpdateTheme: (Theme) -> Unit,
    onToggleDynamicColorValue: (Boolean) -> Unit,
    onOpenUrl: (url: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        val strings = LocalStrings.current
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsTitle(text = "Theme")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
                    .semantics {
                        isTraversalGroup = true
                        selectableGroup()
                    }
            ) {
                state.themes.forEach { theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClickLabel = null,
                                role = Role.RadioButton,
                                onClick = {
                                    if (theme != state.selectedTheme) {
                                        onUpdateTheme(theme)
                                    }
                                }
                            )
                            .semantics(mergeDescendants = true) {
                                selected = theme == state.selectedTheme
                                stateDescription = if (theme == state.selectedTheme) {
                                    strings.settings_page_theme_on_state_desc(theme.label)
                                } else {
                                    strings.settings_page_theme_off_state_desc(theme.label)
                                }
                            },
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        RadioButton(
                            selected = theme == state.selectedTheme,
                            onClick = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clearAndSetSemantics { }
                        )
                        Text(
                            text = theme.label,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clearAndSetSemantics { }
                        )
                    }
                }
            }
            HorizontalDivider()
            if (isDynamicColorSupported()) {
                val isEnabled = isDynamicColorEnabled()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .heightIn(min = 56.dp)
                        .semantics(mergeDescendants = true) {
                            contentDescription = if (!isEnabled) {
                                strings.settings_page_dynamic_colors_off_content_description
                            } else {
                                strings.settings_page_dynamic_colors_on_content_description
                            }
                        }
                ) {
                    if (!isEnabled) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clearAndSetSemantics { }
                        ) {
                            SettingsTitle(text = "Dynamic Colors")
                            Text(
                                text = "You need to be on android 12+ to enable this feature",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Start
                            )
                        }
                    } else {
                        SettingsTitle(
                            text = "Dynamic Colors",
                            modifier = Modifier
                                .weight(1f)
                                .clearAndSetSemantics { }
                        )
                    }
                    Switch(
                        checked = state.isUsingDynamicColor ?: true,
                        onCheckedChange = onToggleDynamicColorValue,
                        enabled = state.isUsingDynamicColor != null && isEnabled,
                        modifier = Modifier
                            .size(48.dp)
                            .semantics {
                                if (isEnabled) {
                                    stateDescription =
                                        if (state.isUsingDynamicColor == null || state.isUsingDynamicColor == true) {
                                            strings.settings_page_dynamic_colors_on_state_desc
                                        } else {
                                            strings.settings_page_dynamic_colors_off_state_desc
                                        }
                                }
                            }
                    )
                }
                HorizontalDivider()
            }
            Preference(
                label = "Followed News Data Sources",
                onClick = onFollowedNewsDataSourcesClick
            )
            HorizontalDivider()
            Preference(
                label = "Favourite Games",
                onClick = onFavouriteGamesClick
            )
            HorizontalDivider()
            Preference(
                label = "Favourite Genres",
                onClick = onFavouriteGenresClick
            )
            HorizontalDivider()
            var isDialogOpened by rememberSaveable(Unit) { mutableStateOf(false) }
            SettingsTitle(
                text = "Credits",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClickLabel = null,
                        role = Role.Button,
                        onClick = { isDialogOpened = true }
                    )
                    .wrapContentSize(Alignment.CenterStart)
            )
            if (isDialogOpened) {
                AlertDialog(
                    onDismissRequest = { isDialogOpened = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isDialogOpened = false
                            }
                        ) {
                            Text("Close")
                        }
                    },
                    dismissButton = null,
                    title = {
                        Text(text = "Credits", style = MaterialTheme.typography.titleLarge)
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    isTraversalGroup = true
                                }
                        ) {
                            HyperlinkText(
                                fullText = "Games data is provided by RAWG API",
                                url = "https://rawg.io/apidocs",
                                urlText = "RAWG API",
                                onOpenUrl = onOpenUrl
                            )
                            HyperlinkText(
                                fullText = "Deals are provided by CheapShark API",
                                url = "https://apidocs.cheapshark.com/",
                                urlText = "CheapShark API",
                                onOpenUrl = onOpenUrl
                            )
                            HyperlinkText(
                                fullText = "Giveaways are provided by GamerPower API",
                                url = "https://www.gamerpower.com/api-read",
                                urlText = "GamerPower API",
                                onOpenUrl = onOpenUrl
                            )
                        }
                    }
                )
            }
            HorizontalDivider()
            SettingsTitle(
                text = "Privacy Policy",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClickLabel = null,
                        role = Role.Button,
                        onClick = {
                            onOpenUrl("https://mr3y-the-programmer.github.io/Ludi/docs/PrivacyPolicy")
                        }
                    )
            )
        }
    }
}

expect fun isDynamicColorEnabled(): Boolean

@Composable
fun HyperlinkText(
    fullText: String,
    url: String,
    urlText: String,
    onOpenUrl: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        append(fullText)
        append(" ")
        withLink(
            LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                ),
                linkInteractionListener = {
                    onOpenUrl((it as LinkAnnotation.Url).url)
                }
            )
        ) {
            append(urlText)
        }
    }
    Text(
        text = annotatedString,
        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun Preference(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .heightIn(min = 56.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick
            )
            .clearAndSetSemantics {
                contentDescription = strings.settings_page_preference_content_description(label)
            }
    ) {
        SettingsTitle(text = label, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxHeight()
                .requiredWidth(36.dp)
        )
    }
}

@Composable
fun SettingsTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Start,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}
