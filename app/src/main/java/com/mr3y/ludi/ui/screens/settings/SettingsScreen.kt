package com.mr3y.ludi.ui.screens.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mr3y.ludi.ui.presenter.SettingsViewModel
import com.mr3y.ludi.ui.presenter.model.SettingsState
import com.mr3y.ludi.ui.presenter.model.Theme
import com.mr3y.ludi.ui.theme.LudiTheme
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()
    SettingsScreen(
        settingsState,
        modifier = modifier,
        onUpdateTheme = viewModel::setAppTheme,
        onToggleDynamicColorValue = viewModel::enableUsingDynamicColor
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onUpdateTheme: (Theme) -> Unit,
    onToggleDynamicColorValue: (Boolean) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = theme == state.selectedTheme,
                            onClick = {
                                if (theme != state.selectedTheme)
                                    onUpdateTheme(theme)
                            }
                        )
                        Text(
                            text = theme.label,
                            style = MaterialTheme.typography.titleMedium,
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
                SettingsTitle(text = "Dynamic Colors")
                Switch(
                    checked = state.isUsingDynamicColor ?: true,
                    onCheckedChange = onToggleDynamicColorValue,
                    enabled = state.isUsingDynamicColor != null
                )
            }
            Divider()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
                    .clickable(
                        role = Role.Button,
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Feature isn't yet implemented",
                                    actionLabel = "Dismiss",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
            ) {
                SettingsTitle(text = "Followed News Data Sources")
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(24.dp)
                )
            }
            Divider()
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .heightIn(min = 56.dp)
                    .clickable(
                        role = Role.Button,
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Feature isn't yet implemented",
                                    actionLabel = "Dismiss",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
            ) {
                SettingsTitle(text = "Favourite Games")
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(24.dp)
                )
            }
        }
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
        maxLines = 2,
        modifier = modifier
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = "id:pixel_6")
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
            {}
        )
    }
}
