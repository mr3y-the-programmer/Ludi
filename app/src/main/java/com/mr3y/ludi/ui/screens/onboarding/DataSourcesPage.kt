package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.R
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.theme.LudiTheme

@OptIn(ExperimentalLayoutApi::class)
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
        Column(
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.on_boarding_data_sources_page_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.align(Alignment.End),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.on_boarding_secondary_text),
                modifier = Modifier.align(Alignment.End),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allNewsDataSources.forEach {
                NewsSourceTile(
                    newsDataSource = it,
                    selected = it in selectedNewsSources,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(IntrinsicSize.Max),
                    onToggleSelection = onToggleNewsSourceTile
                )
            }
        }
    }
}

@Composable
fun NewsSourceTile(
    newsDataSource: NewsDataSource,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onToggleSelection: (NewsDataSource) -> Unit
) {
    Card(
        modifier = modifier
            .selectable(
                selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = { onToggleSelection(newsDataSource) }
            )
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = newsDataSource.drawableId),
                contentDescription = null
            )
            Text(
                text = newsDataSource.name,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge
            )
            val unselectedColor = MaterialTheme.colorScheme.background
            val circleModifier = Modifier
                .size(20.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clip(CircleShape)
            Box(
                modifier = circleModifier
                    .then(
                        if (selected) circleModifier else circleModifier.drawBehind { drawCircle(color = unselectedColor) }
                    ),
                contentAlignment = Alignment.Center,
                propagateMinConstraints = true
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun NewsSourcesPagePreview() {
    LudiTheme {
        NewsSourcesPage(
            allNewsDataSources = PreviewNewsDataSources,
            selectedNewsSources = emptyList(),
            onToggleNewsSourceTile = {}
        )
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun NewsSourceTilePreview() {
    LudiTheme {
        NewsSourceTile(
            newsDataSource = PreviewNewsDataSources.first(),
            selected = true,
            onToggleSelection = {}
        )
    }
}