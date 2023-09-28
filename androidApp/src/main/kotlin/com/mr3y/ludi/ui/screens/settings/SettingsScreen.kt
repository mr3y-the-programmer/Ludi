package com.mr3y.ludi.ui.screens.settings

import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.selectableGroup
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mr3y.ludi.R
import com.mr3y.ludi.di.getScreenModel
import com.mr3y.ludi.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.ui.components.launchChromeCustomTab
import com.mr3y.ludi.ui.navigation.BottomBarTab
import com.mr3y.ludi.ui.navigation.PreferencesType
import com.mr3y.ludi.ui.presenter.SettingsViewModel
import com.mr3y.ludi.ui.presenter.model.SettingsState
import com.mr3y.ludi.ui.presenter.model.Theme
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

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
fun SettingsScreen(
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()
    SettingsScreen(
        settingsState,
        modifier = modifier,
        onFollowedNewsDataSourcesClick = onFollowedNewsDataSourcesClick,
        onFavouriteGamesClick = onFavouriteGamesClick,
        onFavouriteGenresClick = onFavouriteGenresClick,
        onUpdateTheme = viewModel::setAppTheme,
        onToggleDynamicColorValue = viewModel::enableUsingDynamicColor
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    onUpdateTheme: (Theme) -> Unit,
    onToggleDynamicColorValue: (Boolean) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        val context = LocalContext.current
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
                                    context.getString(
                                        R.string.settings_page_theme_on_state_desc,
                                        theme.label
                                    )
                                } else {
                                    context.getString(
                                        R.string.settings_page_theme_off_state_desc,
                                        theme.label
                                    )
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
            Divider()
            val isRunningOnAPI30OrOlder = Build.VERSION.SDK_INT < 31
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
                    .semantics(mergeDescendants = true) {
                        contentDescription = if (isRunningOnAPI30OrOlder) {
                            context.getString(R.string.settings_page_dynamic_colors_off_content_description)
                        } else {
                            context.getString(R.string.settings_page_dynamic_colors_on_content_description)
                        }
                    }
            ) {
                if (isRunningOnAPI30OrOlder) {
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
                    enabled = state.isUsingDynamicColor != null && !isRunningOnAPI30OrOlder,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            if (!isRunningOnAPI30OrOlder) {
                                stateDescription =
                                    if (state.isUsingDynamicColor == null || state.isUsingDynamicColor == true) {
                                        context.getString(R.string.settings_page_dynamic_colors_on_state_desc)
                                    } else {
                                        context.getString(R.string.settings_page_dynamic_colors_off_state_desc)
                                    }
                            }
                        }
                )
            }
            Divider()
            Preference(
                label = "Followed News Data Sources",
                onClick = onFollowedNewsDataSourcesClick
            )
            Divider()
            Preference(
                label = "Favourite Games",
                onClick = onFavouriteGamesClick
            )
            Divider()
            Preference(
                label = "Favourite Genres",
                onClick = onFavouriteGenresClick
            )
            Divider()
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
                        Text(text = "Credits")
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
                            CreditedText(
                                preUrlText = "Games data is provided by ",
                                url = "https://rawg.io/apidocs",
                                urlText = "RAWG API"
                            )
                            CreditedText(
                                preUrlText = "Deals are provided by ",
                                url = "https://apidocs.cheapshark.com/",
                                urlText = "CheapShark API"
                            )
                            CreditedText(
                                preUrlText = "Giveaways are provided by ",
                                url = "https://www.gamerpower.com/api-read",
                                urlText = "GamerPower API"
                            )
                            CreditedText(
                                preUrlText = "Store listing Screenshots is made using ",
                                url = "https://theapplaunchpad.com/",
                                urlText = "AppLaunchPad"
                            )
                            CreditedText(
                                preUrlText = "Store feature graphic is made using ",
                                url = "https://hotpot.ai/",
                                urlText = "Hotpot.ai"
                            )
                        }
                    }
                )
            }
            val chromeToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
            Divider()
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
                            launchChromeCustomTab(
                                context,
                                Uri.parse("https://mr3y-the-programmer.github.io/Ludi/docs/PrivacyPolicy"),
                                chromeToolbarColor
                            )
                        }
                    )
            )
        }
    }
}

@Composable
fun CreditedText(
    preUrlText: String,
    url: String,
    urlText: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val chromeTabColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    Row(
        modifier = modifier
            .clearAndSetSemantics {
                contentDescription = "$preUrlText$urlText"
            }
    ) {
        Text(
            text = preUrlText,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = urlText,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            color = Color.Blue,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClickLabel = null,
                onClick = {
                    launchChromeCustomTab(
                        context,
                        Uri.parse(url),
                        chromeTabColor
                    )
                }
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun Preference(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                contentDescription =
                    context.getString(R.string.settings_page_preference_content_description, label)
            }
    ) {
        SettingsTitle(text = label, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.ArrowForward,
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

@LudiPreview
@Composable
fun SettingsScreenPreview() {
    val settingsState by remember {
        mutableStateOf(
            SettingsState(
                themes = Theme.values().toSet(),
                selectedTheme = Theme.SystemDefault,
                isUsingDynamicColor = true
            )
        )
    }
    LudiTheme {
        SettingsScreen(
            state = settingsState,
            modifier = Modifier.fillMaxSize(),
            {},
            {},
            {},
            {},
            {}
        )
    }
}
