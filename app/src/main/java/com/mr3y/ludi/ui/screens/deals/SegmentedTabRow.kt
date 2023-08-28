package com.mr3y.ludi.ui.screens.deals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.R
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme
import kotlin.math.max

@Composable
fun SegmentedTabRow(
    numOfTabs: Int,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    colors: SegmentedTabRowColors = SegmentedTabRowDefaults.colors(),
    outlineWidth: Dp = SegmentedTabRowDefaults.OutlineWidth,
    tabContent: @Composable (index: Int) -> Unit
) {
    require(numOfTabs in 2..5) {
        "SegmentedTabRow should be used to display 2 to 5 tabs as per material design guidelines," +
            " read more at: https://m3.material.io/components/segmented-buttons/guidelines#1a7a67bb-a2ee-4bbe-a375-8d78fc6391ff"
    }
    val density = LocalDensity.current
    val outlineWidthInPx = with(density) { outlineWidth.roundToPx() }
    val minHeightInPx = with(density) { SegmentedTabRowDefaults.MinRowHeight.roundToPx() }
    Layout(
        modifier = modifier
            .clip(shape = RoundedCornerShape(percent = 50))
            .border(
                outlineWidth,
                colors.outlineColor().value,
                shape = RoundedCornerShape(percent = 50)
            )
            .background(colors.containerColor(selected = false).value),
        content = {
            CompositionLocalProvider(
                LocalContentColor provides colors.contentColor(selected = true).value
            ) {
                repeat(numOfTabs) { index ->
                    Box(
                        modifier = Modifier
                            .layoutId(SegmentedTabRowSlots.Tab)
                            .padding(
                                start = if (index == 0) SegmentedTabRowDefaults.StartPadding else 0.dp,
                                end = if (index == numOfTabs - 1) SegmentedTabRowDefaults.EndPadding else 0.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        tabContent(index)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .layoutId(SegmentedTabRowSlots.SelectedTabOverlay)
                    .clip(
                        shape = RoundedCornerShape(
                            topStartPercent = if (selectedTabIndex == 0) 50 else 0,
                            bottomStartPercent = if (selectedTabIndex == 0) 50 else 0,
                            topEndPercent = if (selectedTabIndex == numOfTabs - 1) 50 else 0,
                            bottomEndPercent = if (selectedTabIndex == numOfTabs - 1) 50 else 0
                        )
                    )
                    .background(colors.containerColor(selected = true).value)
            )
            repeat(numOfTabs - 1) {
                Box(
                    modifier = Modifier
                        .layoutId(SegmentedTabRowSlots.Outline)
                        .width(outlineWidth)
                        .background(colors.outlineColor().value)
                )
            }
        }
    ) { measurables, constraints ->
        val height = max(constraints.maxHeight, minHeightInPx)
        val outlineConstraints = Constraints.fixed(width = outlineWidthInPx, height = height)

        val tabWidth = (constraints.maxWidth - (numOfTabs - 1) * outlineWidthInPx) / numOfTabs
        val tabConstraints = Constraints.fixed(width = tabWidth, height = height)

        val tabPlaceables = measurables
            .filter { measurable -> measurable.layoutId == SegmentedTabRowSlots.Tab }
            .map { measurable -> measurable.measure(tabConstraints) }
        val selectedTabOverlayPlaceable = measurables
            .first { measurable -> measurable.layoutId == SegmentedTabRowSlots.SelectedTabOverlay }
            .measure(tabConstraints)
        val outlinePlaceables = measurables
            .filter { measurable -> measurable.layoutId == SegmentedTabRowSlots.Outline }
            .map { measurable -> measurable.measure(outlineConstraints) }
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            selectedTabOverlayPlaceable.placeRelative(
                x = selectedTabIndex * (tabWidth + outlineWidthInPx),
                y = 0
            )
            tabPlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = if (index == 0) 0 else (tabWidth + outlineWidthInPx) * index,
                    y = 0
                )
            }
            outlinePlaceables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = tabWidth * (index + 1) + (index * placeable.measuredWidth),
                    y = 0
                )
            }
        }
    }
}

@Composable
fun SegmentedTab(
    selected: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    selectedContentColor: Color = LocalContentColor.current,
    unSelectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClickLabel = null,
                role = Role.Tab,
                onClick = onClick
            )
            .semantics(mergeDescendants = true) {
                this.selected = selected
                stateDescription = if (selected) context.getString(R.string.deals_page_tab_on_state_desc, label) else context.getString(R.string.deals_page_tab_off_state_desc, label)
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = selectedContentColor,
                modifier = Modifier.size(18.dp).clearAndSetSemantics { }
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) selectedContentColor else unSelectedContentColor,
            maxLines = 1,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private enum class SegmentedTabRowSlots {
    Tab,
    SelectedTabOverlay,
    Outline
}

object SegmentedTabRowDefaults {

    val OutlineWidth = 1.dp

    val StartPadding = 12.dp

    val EndPadding = 12.dp

    val MinRowHeight = 40.dp

    @Composable
    fun colors(
        selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        selectedContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        unSelectedContainerColor: Color = MaterialTheme.colorScheme.surface,
        unSelectedContentColor: Color = MaterialTheme.colorScheme.onSurface,
        outlineColor: Color = MaterialTheme.colorScheme.outline
    ): SegmentedTabRowColors = SegmentedTabRowColors(
        selectedContainerColor,
        selectedContentColor,
        unSelectedContainerColor,
        unSelectedContentColor,
        outlineColor
    )
}

@Immutable
data class SegmentedTabRowColors internal constructor(
    private val selectedContainerColor: Color,
    private val selectedContentColor: Color,
    private val unSelectedContainerColor: Color,
    private val unSelectedContentColor: Color,
    private val outlineColor: Color
) {
    @Composable
    internal fun containerColor(selected: Boolean): State<Color> {
        return rememberUpdatedState(if (selected) selectedContainerColor else unSelectedContainerColor)
    }

    @Composable
    internal fun contentColor(selected: Boolean): State<Color> {
        return rememberUpdatedState(if (selected) selectedContentColor else unSelectedContentColor)
    }

    @Composable
    internal fun outlineColor(): State<Color> {
        return rememberUpdatedState(outlineColor)
    }
}

@LudiPreview
@Composable
fun PreviewMultiSelector() {
    LudiTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val options1 = listOf("Lorem", "Ipsum", "Dolor")
            var selectedTab1 by remember { mutableStateOf(0) }
            val options2 = listOf("Sit", "Amet", "Consectetur", "Elit", "Quis")
            var selectedTab2 by remember { mutableStateOf(0) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SegmentedTabRow(
                    selectedTabIndex = selectedTab1,
                    numOfTabs = 2,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                ) { index ->
                    SegmentedTab(
                        selected = index == selectedTab1,
                        label = options1[index],
                        onClick = { selectedTab1 = index }
                    )
                }
                SegmentedTabRow(
                    selectedTabIndex = selectedTab1,
                    numOfTabs = 3,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                ) { index ->
                    SegmentedTab(
                        selected = index == selectedTab1,
                        label = options1[index],
                        onClick = { selectedTab1 = index }
                    )
                }

                SegmentedTabRow(
                    selectedTabIndex = selectedTab2,
                    numOfTabs = 5,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                ) { index ->
                    SegmentedTab(
                        selected = index == selectedTab2,
                        label = options2[index],
                        onClick = { selectedTab2 = index },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}
