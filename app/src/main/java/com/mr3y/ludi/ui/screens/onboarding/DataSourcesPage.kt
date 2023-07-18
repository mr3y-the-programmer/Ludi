package com.mr3y.ludi.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.R
import com.mr3y.ludi.ui.presenter.model.NewsDataSource
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            allNewsDataSources.forEachIndexed { index, newsDataSource ->
                NewsSourceTile(
                    newsDataSource = newsDataSource,
                    selected = newsDataSource in selectedNewsSources,
                    onToggleSelection = onToggleNewsSourceTile
                )
                if (index != allNewsDataSources.lastIndex) {
                    Divider(modifier = Modifier.padding(horizontal = 8.dp))
                }
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected,
                role = Role.Button,
                onClick = { onToggleSelection(newsDataSource) }
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = newsDataSource.drawableId),
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

@LudiPreview
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

@LudiPreview
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
