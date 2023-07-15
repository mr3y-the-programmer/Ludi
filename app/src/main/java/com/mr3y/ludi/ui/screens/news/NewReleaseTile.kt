package com.mr3y.ludi.ui.screens.news

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.Title
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.theme.LudiTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewReleaseTile(
    newReleaseArticleWrapper: ResourceWrapper<NewReleaseArticle>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val newReleaseArticle = newReleaseArticleWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClickLabel = null,
                enabled = newReleaseArticleWrapper is ResourceWrapper.ActualResource,
                role = Role.Button,
                onClick = onClick
            )
            .defaultPlaceholder(
                isVisible = newReleaseArticleWrapper is ResourceWrapper.Placeholder,
                highlight = PlaceholderHighlight.shimmer(),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            if (newReleaseArticle != null) {
                Text(
                    text = newReleaseArticle.title.text.removePrefix("<![CDATA[ ").removeSuffix(" ]]>"),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = newReleaseArticle.releaseDate.toLocalDate()
                            ?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "N/A",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = newReleaseArticle.source.name,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Start
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "\n\n",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, backgroundColor = 0xFF121212, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewReleasePlaceholderPreview() {
    LudiTheme {
        NewReleaseTile(
            newReleaseArticleWrapper = ResourceWrapper.Placeholder,
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, backgroundColor = 0xFF121212, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewReleasePreview() {
    LudiTheme {
        NewReleaseTile(
            newReleaseArticleWrapper = ResourceWrapper.ActualResource(
                NewReleaseArticle(
                    title = Title.Plain("CAT Interstellar: Episode II"),
                    description = null,
                    sourceLinkUrl = "https://www.gamespot.com/games/cat-interstellar-episode-ii/",
                    releaseDate = ZonedDateTime.now(),
                    source = Source.GameSpot
                )
            ),
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        )
    }
}
