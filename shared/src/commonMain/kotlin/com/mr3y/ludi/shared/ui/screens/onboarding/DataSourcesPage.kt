package com.mr3y.ludi.shared.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.ui.presenter.model.NewsDataSource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewsSourcesPage(
    allNewsDataSources: List<NewsDataSource>,
    selectedNewsSources: List<NewsDataSource>,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onToggleNewsSourceTile: (NewsDataSource) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        val strings = LocalStrings.current
        val label = strings.on_boarding_data_sources_page_title
        val secondaryText = strings.on_boarding_secondary_text
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics {
                contentDescription = "$label\n$secondaryText"
            }
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = secondaryText,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    isTraversalGroup = true
                }
        ) {
            allNewsDataSources.forEachIndexed { index, newsDataSource ->
                NewsSourceTile(
                    newsDataSource = newsDataSource,
                    selected = newsDataSource in selectedNewsSources,
                    onToggleSelection = onToggleNewsSourceTile
                )
                if (index != allNewsDataSources.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NewsSourceTile(
    newsDataSource: NewsDataSource,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onToggleSelection: (NewsDataSource) -> Unit
) {
    val strings = LocalStrings.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected,
                role = Role.Checkbox,
                onClick = { onToggleSelection(newsDataSource) }
            )
            .padding(8.dp)
            .clearAndSetSemantics {
                stateDescription = if (selected) strings.data_sources_page_data_source_on_state_desc(newsDataSource.name) else strings.data_sources_page_data_source_off_state_desc(newsDataSource.name)
                this.selected = selected
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(DrawableResource(newsDataSource.iconResName)),
                contentDescription = null,
                tint = Color.Unspecified // instruct Icon to not override android:fillColor specified in the vector drawable
            )
            Text(
                text = newsDataSource.name,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Checkbox(
            checked = selected,
            onCheckedChange = null
        )
    }
}
