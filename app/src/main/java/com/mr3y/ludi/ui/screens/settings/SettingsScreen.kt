package com.mr3y.ludi.ui.screens.settings

import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.ui.components.launchChromeCustomTab
import com.mr3y.ludi.ui.presenter.SettingsViewModel
import com.mr3y.ludi.ui.presenter.model.SettingsState
import com.mr3y.ludi.ui.presenter.model.Theme
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun SettingsScreen(
    onFollowedNewsDataSourcesClick: () -> Unit,
    onFavouriteGamesClick: () -> Unit,
    onFavouriteGenresClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
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
        containerColor = MaterialTheme.colorScheme.surface
    ) { contentPadding ->
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
            ) {
                state.themes.forEach { theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
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
                            ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RadioButton(
                            selected = theme == state.selectedTheme,
                            onClick = null
                        )
                        Text(
                            text = theme.label,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
            Divider()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
            ) {
                val isRunningOnAPI30OrOlder = Build.VERSION.SDK_INT < 31
                if (isRunningOnAPI30OrOlder) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
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
                    SettingsTitle(text = "Dynamic Colors")
                }
                Switch(
                    checked = state.isUsingDynamicColor ?: true,
                    onCheckedChange = onToggleDynamicColorValue,
                    enabled = state.isUsingDynamicColor != null && !isRunningOnAPI30OrOlder
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
                            modifier = Modifier.fillMaxWidth()
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
    Row(modifier = modifier) {
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
            maxLines = 1,
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
    ) {
        SettingsTitle(text = label)
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
