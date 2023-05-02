package com.mr3y.ludi.ui.screens.discover

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.FreeGame
import com.mr3y.ludi.core.model.RichInfoGame
import com.mr3y.ludi.ui.components.LudiSuggestionChip
import com.mr3y.ludi.ui.components.SingleRowLayout
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun FreeGameCard(
    freeGameWrapper: ResourceWrapper<FreeGame>,
    modifier: Modifier = Modifier
) {
    val freeGame = freeGameWrapper.actualResource
    SuggestionGameCardScaffold(
        modifier = modifier,
        imageUrl = freeGame?.thumbnailUrl,
        title = freeGame?.title,
        showPlaceholder = freeGameWrapper is ResourceWrapper.Placeholder
    ) {
        Text(
            text = freeGame?.publisher ?: "Publisher placeholder",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .defaultPlaceholder(freeGameWrapper is ResourceWrapper.Placeholder)
        )
        freeGame?.genre?.let {
            LudiSuggestionChip(
                label = it,
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .width(IntrinsicSize.Max)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RichInfoGameCard(
    richInfoGameWrapper: ResourceWrapper<RichInfoGame>,
    modifier: Modifier
) {
    val richInfoGame = richInfoGameWrapper.actualResource
    SuggestionGameCardScaffold(
        imageUrl = richInfoGame?.imageUrl,
        title = richInfoGame?.name,
        showPlaceholder = richInfoGameWrapper is ResourceWrapper.Placeholder,
        modifier = modifier
    ) {
        if (richInfoGame == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .defaultPlaceholder(richInfoGameWrapper is ResourceWrapper.Placeholder)
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                richInfoGame.platformsInfo?.map { it.name.mapPlatformNameToVectorDrawable() }?.distinct()?.forEach { drawableId ->
                    if (drawableId != null) {
                        Icon(
                            painter = painterResource(id = drawableId),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        richInfoGame?.rating?.let {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Star),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.star,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = it.toString().takeIf { it != "0.0" } ?: "N/A",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        richInfoGame?.suggestionsCount?.let {
            Text(
                text = "$it people Suggests this",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        SingleRowLayout(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .aspectRatio(8f),
            overflowIndicator = {
                Text(
                    text = "+$it",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(constraints.maxWidth, constraints.maxHeight) {
                            placeable.placeRelative(x = 0, y = (constraints.maxHeight - placeable.measuredHeight) / 2)
                        }
                    }
                )
            }
        ) {
            richInfoGame?.genres?.forEach {
                LudiSuggestionChip(label = it.name)
            }
        }
    }
}

private fun String.mapPlatformNameToVectorDrawable(): Int? {
    return when {
        contains("playstation", ignoreCase = true) -> R.drawable.playstation
        contains("pc", ignoreCase = true) -> R.drawable.pc
        contains("xbox", ignoreCase = true) -> R.drawable.xbox
        contains("Nintendo Switch", ignoreCase = true) -> R.drawable.nintendo_switch
        contains("ios", ignoreCase = true) -> R.drawable.ios
        contains("android", ignoreCase = true) -> R.drawable.android
        contains("linux", ignoreCase = true) -> R.drawable.linux
        else -> null
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchResultsGameCard(
    richInfoGameWrapper: ResourceWrapper<RichInfoGame>,
    modifier: Modifier = Modifier
) {
    val richInfoGame = richInfoGameWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = richInfoGame?.imageUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .defaultPlaceholder(richInfoGameWrapper is ResourceWrapper.Placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = richInfoGame?.name ?: "Name placeholder",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.defaultPlaceholder(richInfoGameWrapper is ResourceWrapper.Placeholder)
                )
                if (richInfoGame != null) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        richInfoGame.platformsInfo?.map { it.name.mapPlatformNameToVectorDrawable() }
                            ?.distinct()?.forEach { drawableId ->
                                if (drawableId != null) {
                                    Icon(
                                        painter = painterResource(id = drawableId),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                    }
                }
                richInfoGame?.rating.let {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Star),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.star,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = it.toString().takeIf { it != "0.0" } ?: "N/A",
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

val ColorScheme.star: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color(0xFFEAC54F) else Color(0xFFE3B341)

@Composable
fun SuggestionGameCardScaffold(
    imageUrl: String?,
    title: String?,
    modifier: Modifier = Modifier,
    showPlaceholder: Boolean = false,
    other: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .defaultPlaceholder(showPlaceholder),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title ?: "Title Placeholder",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .defaultPlaceholder(showPlaceholder)
                )

                this.other()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun FreeGameCardPreview() {
    LudiTheme {
        FreeGameCard(
            freeGameWrapper = freeGamesSamples.first(),
            modifier = Modifier
                .padding(16.dp)
                .size(width = 248.dp, height = 360.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun RichInfoGameCardPreview() {
    LudiTheme {
        RichInfoGameCard(
            richInfoGameWrapper = richInfoGamesSamples[2],
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Min)
                .aspectRatio(0.6f)
        )
    }
}

@Preview
@Composable
fun SearchResultsGameCardPreview() {
    LudiTheme {
        SearchResultsGameCard(
            richInfoGameWrapper = richInfoGamesSamples[1],
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun GameCardScaffoldPlaceholderPreview() {
    LudiTheme {
        SuggestionGameCardScaffold(
            imageUrl = null,
            title = null,
            modifier = Modifier
                .padding(16.dp)
                .size(width = 248.dp, height = 360.dp),
            showPlaceholder = true
        ) {}
    }
}
