package com.mr3y.ludi.shared.ui.screens.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.ui.components.placeholder.defaultPlaceholder
import java.time.format.DateTimeFormatter

@Composable
fun NewReleaseTile(
    newReleaseArticle: NewReleaseArticle?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    val title = newReleaseArticle?.title?.text?.removePrefix("<![CDATA[ ")?.removeSuffix(" ]]>")
    val releaseDate = remember(newReleaseArticle) {
        newReleaseArticle?.releaseDate?.toLocalDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClickLabel = null,
                enabled = newReleaseArticle != null,
                onClick = onClick
            )
            .defaultPlaceholder(isVisible = newReleaseArticle == null)
            .clearAndSetSemantics {
                contentDescription = strings.news_page_new_release_content_description(title ?: "", releaseDate ?: "")
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            if (newReleaseArticle != null) {
                Text(
                    text = title ?: "Placeholder",
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
                        text = releaseDate ?: "N/A",
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
