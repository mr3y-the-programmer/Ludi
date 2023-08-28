package com.mr3y.ludi.ui.screens.deals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import coil.compose.AsyncImage
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GiveawayEntry
import com.mr3y.ludi.ui.components.defaultPlaceholder
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.roundToInt
import kotlin.time.toKotlinDuration

@Composable
fun Deal(
    dealWrapper: ResourceWrapper<Deal>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val deal = dealWrapper.actualResource
    val contentDescription = if (deal != null) {
        stringResource(
            id = R.string.deals_page_deal_content_description,
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
                    modifier = Modifier
                )

                val normalPrice = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.titleLarge.toSpanStyle().copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        append(deal.normalPriceInUsDollar.toString())
                    }
                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle().copy(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append('$')
                    }
                }
                Text(
                    text = normalPrice,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "You save: ${deal.savings.roundToInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GamerPowerGameGiveaway(
    giveawayWrapper: ResourceWrapper<GiveawayEntry>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val giveaway = giveawayWrapper.actualResource
    val contentDescription = if (giveaway != null) {
        stringResource(R.string.deals_page_giveaway_content_description, giveaway.title)
    } else {
        null
    }
    OfferScaffold(
        thumbnailUrl = giveaway?.thumbnailUrl,
        title = giveaway?.title,
        contentDesc = contentDescription,
        modifier = modifier,
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
            Box {
                Text(
                    text = "\n\n\n",
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${giveaway.users} users have claimed this giveaway",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun durationBetween(start: ZonedDateTime, end: ZonedDateTime): Triple<String, String, String> {
    val duration = Duration.between(start, end).toKotlinDuration().toString()
    val days = duration.substringBefore('d')
    val hours = duration.substringAfter('d').substringBefore('h')
    val minutes = duration.substringAfter('h').substringBefore('m')
    return Triple(days, hours, minutes)
}

@Composable
private fun OfferScaffold(
    thumbnailUrl: String?,
    title: String?,
    contentDesc: String?,
    modifier: Modifier = Modifier,
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .defaultPlaceholder(isVisible = title == null),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = thumbnailUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            if (title != null) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )
                    this.other()
                }
            }
        }
    }
}

@LudiPreview
@Composable
fun DealPreview() {
    LudiTheme {
        Deal(
            dealSamples.first(),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            onClick = {}
        )
    }
}

@LudiPreview
@Composable
fun GamerPowerGiveawayPreview() {
    LudiTheme {
        GamerPowerGameGiveaway(
            giveawayWrapper = otherGamesGiveawaysSamples.first(),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            onClick = {}
        )
    }
}
