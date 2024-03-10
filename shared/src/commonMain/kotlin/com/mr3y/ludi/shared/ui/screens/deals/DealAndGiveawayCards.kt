package com.mr3y.ludi.shared.ui.screens.deals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.core.model.Deal
import com.mr3y.ludi.shared.core.model.GiveawayEntry
import com.mr3y.ludi.shared.ui.components.AsyncImage
import com.mr3y.ludi.shared.ui.components.placeholder.defaultPlaceholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.roundToInt
import kotlin.time.toKotlinDuration

@Composable
fun Deal(
    deal: Deal?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val strings = LocalStrings.current
    val contentDescription = if (deal != null) {
        strings.deals_page_deal_content_description(
            deal.name,
            deal.salePriceInUsDollar,
            deal.normalPriceInUsDollar,
            deal.savings.toFloat()
        )
    } else {
        null
    }
    OfferScaffold(
        thumbnailUrl = deal?.thumbnailUrl,
        title = deal?.name,
        contentDesc = contentDescription,
        modifier = modifier,
        onClick = onClick
    ) {
        if (deal != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val salePrice = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.titleLarge.toSpanStyle().copy(color = MaterialTheme.colorScheme.onSurface)) {
                        append(deal.salePriceInUsDollar.toString())
                        withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                            append('$')
                        }
                    }
                }

                Text(
                    text = salePrice,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alignByBaseline()
                )

                val normalPrice = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.titleLarge.toSpanStyle().copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    ) {
                        append(deal.normalPriceInUsDollar.toString())
                    }
                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle().copy(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))) {
                        append('$')
                    }
                }
                Text(
                    text = normalPrice,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alignByBaseline()
                )
            }
            Text(
                text = "You save: ${deal.savings.roundToInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GamerPowerGameGiveaway(
    giveaway: GiveawayEntry?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val strings = LocalStrings.current
    val contentDescription = if (giveaway != null) {
        strings.deals_page_giveaway_content_description(giveaway.title)
    } else {
        null
    }
    OfferScaffold(
        thumbnailUrl = giveaway?.thumbnailUrl,
        title = giveaway?.title,
        contentDesc = contentDescription,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        onClick = onClick
    ) {
        if (giveaway != null) {
            if (giveaway.endDateTime != null) {
                var currentTime: ZonedDateTime by remember { mutableStateOf(ZonedDateTime.now()) }
                LaunchedEffect(Unit) {
                    while (isActive) {
                        delay(60 * 1000L)
                        currentTime = ZonedDateTime.now()
                    }
                }
                val (days, hours, minutes) = durationBetween(currentTime, giveaway.endDateTime)
                Text(
                    text = "Ends in $days d $hours h $minutes m",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "${giveaway.users} users have claimed this giveaway",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

internal fun durationBetween(start: ZonedDateTime, end: ZonedDateTime): Triple<String, String, String> {
    val duration = Duration.between(start, end).toKotlinDuration().toString()
    val days = duration.substringBefore('d')
    val hours = duration.substringAfter('d').substringBefore('h')
    val minutes = duration.substringAfter('h').substringBefore('m')
    return Triple(days, hours, minutes)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun OfferScaffold(
    thumbnailUrl: String?,
    title: String?,
    contentDesc: String?,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    onClick: () -> Unit,
    other: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                if (contentDesc != null) {
                    contentDescription = contentDesc
                }
                if (title != null) {
                    text = AnnotatedString(title)
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .defaultPlaceholder(isVisible = title == null),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                url = thumbnailUrl,
                placeholder = painterResource(DrawableResource("placeholder.xml")),
                error = painterResource(DrawableResource("placeholder.xml")),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.Medium
            )
            if (title != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 8.dp),
                    verticalArrangement = verticalArrangement
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                    this.other()
                }
            }
        }
    }
}
