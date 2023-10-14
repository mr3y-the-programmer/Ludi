package com.mr3y.ludi.shared.ui.screens.discover

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.shared.ui.adaptive.LocalWindowSizeClass
import com.mr3y.ludi.shared.ui.components.LudiFilterChip
import com.mr3y.ludi.shared.ui.presenter.model.DiscoverFiltersState
import com.mr3y.ludi.shared.ui.presenter.model.Platform
import com.mr3y.ludi.shared.ui.presenter.model.Store
import com.mr3y.ludi.shared.ui.presenter.model.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(
    filtersState: DiscoverFiltersState,
    onDismiss: () -> Unit,
    onSelectingPlatform: (Platform) -> Unit,
    onUnselectingPlatform: (Platform) -> Unit,
    onSelectingStore: (Store) -> Unit,
    onUnselectingStore: (Store) -> Unit,
    onSelectingTag: (Tag) -> Unit,
    onUnselectingTag: (Tag) -> Unit
) {
    val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
    // Use Dialog on Medium & Expanded screens, and ModalBottomSheet for compact screens.
    val useDialog = widthSizeClass >= WindowWidthSizeClass.Medium
    if (useDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                OutlinedButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                    border = BorderStroke(0.dp, Color.Transparent),
                    onClick = onDismiss
                ) {
                    Text(text = "Close")
                }
            },
            dismissButton = null,
            title = null,
            text = {
                FiltersContent(
                    filtersState = filtersState,
                    onSelectingPlatform,
                    onUnselectingPlatform,
                    onSelectingStore,
                    onUnselectingStore,
                    onSelectingTag,
                    onUnselectingTag
                )
            }
        )
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            dragHandle = null,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.End),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                border = BorderStroke(0.dp, Color.Transparent),
                onClick = onDismiss
            ) {
                Text(text = "Close")
            }
            FiltersContent(
                filtersState = filtersState,
                onSelectingPlatform,
                onUnselectingPlatform,
                onSelectingStore,
                onUnselectingStore,
                onSelectingTag,
                onUnselectingTag
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FiltersContent(
    filtersState: DiscoverFiltersState,
    onSelectingPlatform: (Platform) -> Unit,
    onUnselectingPlatform: (Platform) -> Unit,
    onSelectingStore: (Store) -> Unit,
    onUnselectingStore: (Store) -> Unit,
    onSelectingTag: (Tag) -> Unit,
    onUnselectingTag: (Tag) -> Unit
) {
    val chipModifier = Modifier
        .padding(vertical = 4.dp)
        .width(IntrinsicSize.Max)
        .animateContentSize()
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Platforms:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .semantics {
                    isTraversalGroup = true
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filtersState.allPlatforms.forEach {
                LudiFilterChip(
                    selected = it in filtersState.selectedPlatforms,
                    label = it.label,
                    modifier = chipModifier,
                    onClick = {
                        if (it in filtersState.selectedPlatforms) {
                            onUnselectingPlatform(it)
                        } else {
                            onSelectingPlatform(it)
                        }
                    }
                )
            }
        }
        Text(
            text = "Stores: ",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .semantics {
                    isTraversalGroup = true
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filtersState.allStores.forEach {
                LudiFilterChip(
                    selected = it in filtersState.selectedStores,
                    label = it.label,
                    modifier = chipModifier,
                    onClick = {
                        if (it in filtersState.selectedStores) {
                            onUnselectingStore(it)
                        } else {
                            onSelectingStore(it)
                        }
                    }
                )
            }
        }
        Text(
            text = "Tags:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .semantics {
                    isTraversalGroup = true
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filtersState.allTags.forEach {
                LudiFilterChip(
                    selected = it in filtersState.selectedTags,
                    label = it.label,
                    modifier = chipModifier,
                    onClick = {
                        if (it in filtersState.selectedTags) {
                            onUnselectingTag(it)
                        } else {
                            onSelectingTag(it)
                        }
                    }
                )
            }
        }
    }
}
