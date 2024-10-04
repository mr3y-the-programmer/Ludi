package com.mr3y.ludi.shared.ui.screens.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.ui.components.AsyncImage
import com.mr3y.ludi.shared.ui.components.State
import com.mr3y.ludi.shared.ui.components.placeholder.defaultPlaceholder
import com.mr3y.ludi.shared.ui.components.rememberParallaxAlignment
import ludi.shared.generated.resources.Res
import ludi.shared.generated.resources.placeholder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.time.toKotlinDuration

@Composable
fun ArticleCardTile(
    article: Article?,
    lazyListState: LazyListState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClickLabel = null,
                enabled = article != null,
                onClick = onClick
            )
            .defaultPlaceholder(isVisible = article == null)
            .clearAndSetSemantics {
                if (article != null) {
                    contentDescription = strings.news_page_article_content_description(
                        article.title.text.removeCDATA(),
                        article.author ?: "",
                        article.source.name
                    )
                }
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var imageLoaded by remember { mutableStateOf(false) }
            AsyncImage(
                url = article?.imageUrl,
                placeholder = painterResource(Res.drawable.placeholder),
                error = painterResource(Res.drawable.placeholder),
                onState = { state ->
                    if (state is State.Success) {
                        imageLoaded = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f),
                contentDescription = null,
                alignment = rememberParallaxAlignment(lazyListState, key = article?.sourceLinkUrl),
                contentScale = if (imageLoaded) ContentScale.Crop else ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (article != null) {
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "\n\n\n",
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = article?.title?.text?.removeCDATA() ?: "Title placeholder",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\n\n\n\n\n\n",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    val richState = rememberRichTextState()
                    LaunchedEffect(article) {
                        val markupText = (article?.description?.text ?: article?.content?.text)?.removeCDATA()
                        richState.setHtml(markupText ?: "No description available")
                    }
                    RichText(
                        state = richState,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (article != null) {
                    Row(
                        horizontalArrangement = if (article.author == null && article.publicationDate != null) Arrangement.End else Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        article.author?.let {
                            Text(
                                text = it.removeCDATA(),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.alignByBaseline()
                            )
                        }
                        article.publicationDate?.let {
                            val durationSincePubDate = Duration.between(it, ZonedDateTime.now()).toKotlinDuration()
                            val timePassed = when {
                                durationSincePubDate.inWholeDays > 0 -> "${durationSincePubDate.inWholeDays}d"
                                durationSincePubDate.inWholeHours > 0 -> "${durationSincePubDate.inWholeHours}h"
                                durationSincePubDate.inWholeMinutes > 0 -> "${durationSincePubDate.inWholeMinutes}m"
                                else -> strings.now
                            }
                            Text(
                                text = timePassed,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .alignByBaseline()
                                    .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun String.removeCDATA() = removePrefix("<![CDATA[ ").removeSuffix(" ]]>")
