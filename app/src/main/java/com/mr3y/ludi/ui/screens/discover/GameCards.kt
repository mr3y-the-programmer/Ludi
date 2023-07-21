package com.mr3y.ludi.ui.screens.discover

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.Game
import com.mr3y.ludi.ui.components.LudiSuggestionChip
import com.mr3y.ludi.ui.components.chromeCustomTabToolbarColor
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.components.launchChromeCustomTab
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameCard(
    game: Game?,
    modifier: Modifier = Modifier,
    showGenre: Boolean = false
) {
    val context = LocalContext.current
    val chromeToolbarColor = MaterialTheme.colorScheme.chromeCustomTabToolbarColor
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                enabled = game != null,
                role = Role.Button,
                onClick = {
                    game?.slug?.let {
                        launchChromeCustomTab(
                            context,
                            Uri.parse("https://rawg.io/games/$it"),
                            chromeToolbarColor
                        )
                    }
                }
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = game?.imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPlaceholder(game == null),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            if (showGenre) {
                game?.genres?.firstOrNull()?.name?.let {
                    LudiSuggestionChip(
                        label = it,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            if (game != null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.1f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                                0.3f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                0.5f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                                0.7f to MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                                0.9f to MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primaryContainer),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Star),
                            contentDescription = null,
                            tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = game.rating.toString().takeIf { it != "0.0" } ?: "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        game.platformsInfo?.map { it.name.mapPlatformNameToVectorDrawable() }
                            ?.distinct()?.forEach { drawableId ->
                                if (drawableId != null) {
                                    Icon(
                                        painter = painterResource(id = drawableId),
                                        contentDescription = null,
                                        tint = contentColorFor(MaterialTheme.colorScheme.primaryContainer),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                    }
                }
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

@LudiPreview
@Composable
fun RichInfoGameCardPreview() {
    LudiTheme {
        GameCard(
            game = gamesSamples[2].actualResource,
            modifier = Modifier
                .padding(16.dp)
                .width(IntrinsicSize.Min)
                .aspectRatio(0.6f),
            showGenre = true
        )
    }
}

@LudiPreview
@Composable
fun RichInfoGameCardPlaceholderPreview() {
    LudiTheme {
        GameCard(
            game = null,
            modifier = Modifier
                .padding(16.dp)
                .size(width = 248.dp, height = 360.dp),
            showGenre = true
        )
    }
}
