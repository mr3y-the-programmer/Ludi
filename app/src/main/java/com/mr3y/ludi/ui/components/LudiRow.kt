package com.mr3y.ludi.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.core.model.NewReleaseArticle
import com.mr3y.ludi.core.model.Source
import com.mr3y.ludi.core.model.Title
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.theme.LudiTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LudiNewRelease(
    newReleaseArticleWrapper: ResourceWrapper<NewReleaseArticle>,
    modifier: Modifier = Modifier
) {
    val newReleaseArticle = newReleaseArticleWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = newReleaseArticle?.title?.text?.removePrefix("<![CDATA[ ")?.removeSuffix(" ]]>") ?: "Title placeholder",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .defaultPlaceholder(isVisible = newReleaseArticleWrapper is ResourceWrapper.Placeholder)
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = newReleaseArticle?.releaseDate?.toLocalDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "N/A",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .defaultPlaceholder(isVisible = newReleaseArticleWrapper is ResourceWrapper.Placeholder)
                )
                if (newReleaseArticle != null) {
                    Text(
                        text = newReleaseArticle.source.name.let { "Source: $it" },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, backgroundColor = 0xFF121212, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LudiNewReleasePlaceholderPreview() {
    LudiTheme {
        LudiNewRelease(
            newReleaseArticleWrapper = ResourceWrapper.Placeholder,
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
fun LudiNewReleasePreview() {
    LudiTheme {
        LudiNewRelease(
            newReleaseArticleWrapper = ResourceWrapper.ActualResource(
                NewReleaseArticle(
                    title = Title.Plain("CAT Interstellar: Episode II"),
                    description = null,
                    sourceLinkUrl = "https://www.gamespot.com/games/cat-interstellar-episode-ii/",
                    releaseDate = ZonedDateTime.now(),
                    source = Source.GameSpot
                )
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        )
    }
}