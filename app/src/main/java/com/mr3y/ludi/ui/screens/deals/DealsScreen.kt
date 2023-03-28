package com.mr3y.ludi.ui.screens.deals

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder
import com.mr3y.ludi.core.model.Deal
import com.mr3y.ludi.core.model.GamerPowerGiveawayEntry
import com.mr3y.ludi.core.model.MMOGiveawayEntry
import com.mr3y.ludi.core.model.Percent
import com.mr3y.ludi.core.network.model.CheapSharkDeal
import com.mr3y.ludi.core.network.model.GiveawayEntryStatus
import com.mr3y.ludi.core.network.model.toCoreGamerPowerGiveawayEntry
import com.mr3y.ludi.core.network.model.toDeal
import com.mr3y.ludi.ui.components.LudiSectionHeader
import com.mr3y.ludi.ui.components.shortDescription
import com.mr3y.ludi.ui.presenter.DealsViewModel
import com.mr3y.ludi.ui.presenter.model.DealsState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.presenter.model.UiResult
import com.mr3y.ludi.ui.presenter.model.actualResource
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun DealsScreen(
    modifier: Modifier = Modifier,
    viewModel: DealsViewModel = hiltViewModel()
) {
    val dealsState by viewModel.dealsState.collectAsStateWithLifecycle()
    DealsScreen(
        dealsState = dealsState
    )
}

@Composable
fun DealsScreen(
    dealsState: DealsState,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable(Unit) { mutableStateOf(0) }
    Scaffold(
        topBar = {
            val tabsLabels = listOf("Deals", "Giveaways")
            DealsTabRow(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                selectedTabIndex = selectedTab
            ) {index ->
                DealsTab(
                    modifier = Modifier.height(48.dp),
                    label = tabsLabels[index],
                    onClick = { selectedTab = index }
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (selectedTab == 0) {
                        Card(
                            modifier = Modifier.weight(5f),
                            shape = RoundedCornerShape(50),
                            elevation = 8.dp
                        ) {
                            TextField(
                                value = "",
                                onValueChange = {},
                                placeholder = {
                                    Text(text = "Search for a specific deal")
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    cursorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent
                                ),
                                leadingIcon = {
                                    IconButton(
                                        onClick = { /*TODO*/ }
                                    ) {
                                        Icon(
                                            painter = rememberVectorPainter(image = Icons.Filled.Search),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxHeight(),
                                        )
                                    }
                                }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(5f))
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .weight(1f)
                            .size(48.dp)
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Tune),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            }
            if (selectedTab == 0) {
                when(val deals = dealsState.deals) {
                    is UiResult.Content -> {
                        itemsIndexed(deals.data) { index, deal ->
                            key(index) {
                                Deal(
                                    dealWrapper = deal,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                        .height(96.dp)
                                )
                            }
                        }
                    }
                    is UiResult.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.tabRow)
                            ) {
                                Text(
                                    text = "Unexpected Error happened. try to refresh, and see if the problem persist.",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
            } else {
                when(val mmoGiveaways = dealsState.mmoGamesGiveaways) {
                    is UiResult.Content -> {
                        itemsIndexed(mmoGiveaways.data) { index, mmoGiveaway ->
                            key(index) {
                                MMOGameGiveaway(
                                    giveawayWrapper = mmoGiveaway,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                        .height(120.dp)
                                )
                            }
                        }
                    }
                    is UiResult.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colors.tabRow)
                            ) {
                                Text(
                                    text = "Unexpected Error happened. try to refresh, and see if the problem persist.",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
                when(val otherGamesGiveaways = dealsState.otherGamesGiveaways) {
                    is UiResult.Content -> {
                        itemsIndexed(otherGamesGiveaways.data) { index, gameGiveaway ->
                            key(index) {
                                GamerPowerGameGiveaway(
                                    giveawayWrapper = gameGiveaway,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                        .height(120.dp)
                                )
                            }
                        }
                    }
                    is UiResult.Error -> {
                        item {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.tabRow)
                            ) {
                                Text(
                                    text = "Unexpected Error happened. try to refresh, and see if the problem persist.",
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Deal(
    dealWrapper: ResourceWrapper<Deal>,
    modifier: Modifier = Modifier
) {
    val deal = dealWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(8.dp),
        elevation = 8.dp
    ) {
        Row {
            AsyncImage(
                model = deal?.thumbnailUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .placeholder(
                        visible = dealWrapper is ResourceWrapper.Placeholder,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.15f
                            )
                        )
                    ),
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
                    text = deal?.name ?: "Deal Name Placeholder",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = dealWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )

                Text(
                    text = deal?.releaseDate?.toLocalDate()?.toString() ?: "Deal Release Date placeholder",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = dealWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )
            }
            if (deal != null) {
                Column(modifier = Modifier.weight(0.5f)) {
                    val salePrice = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Green, fontSize = 24.sp)) {
                            append(deal.salePriceInUsDollar.toString())
                            withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                append('$')
                            }
                        }
                    }
                    Text(
                        text = salePrice,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    val normalPrice = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                            withStyle(style = SpanStyle(color = Color.Red, fontSize = 16.sp)) {
                                append(deal.normalPriceInUsDollar.toString())
                                withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                    append('$')
                                }
                            }
                        }
                    }
                    Text(
                        text = normalPrice,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MMOGameGiveaway(
    giveawayWrapper: ResourceWrapper<MMOGiveawayEntry>,
    modifier: Modifier = Modifier,
) {
    val giveaway = giveawayWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(8.dp),
        elevation = 8.dp
    ) {
        Row {
            AsyncImage(
                model = giveaway?.thumbnailUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .placeholder(
                        visible = giveawayWrapper is ResourceWrapper.Placeholder,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.15f
                            )
                        )
                    ),
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
                    text = giveaway?.title ?: "Deal Name Placeholder",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = giveawayWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )

                val keysLeft = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colors.onSurface)) {
                        append("keys Left: ")
                    }
                    withStyle(style = SpanStyle(fontSize = 20.sp, color = MaterialTheme.colors.onSurface)) {
                        append("${giveaway?.keysLeftPercent?.value}%")
                    }
                }
                val placeholder = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 24.sp, color = MaterialTheme.colors.onSurface)) {
                        append("Deal Release Date placeholder")
                    }
                }
                Text(
                    text = if (giveaway != null) keysLeft else placeholder,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(
                            visible = giveawayWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun GamerPowerGameGiveaway(
    giveawayWrapper: ResourceWrapper<GamerPowerGiveawayEntry>,
    modifier: Modifier = Modifier
) {
    val giveaway = giveawayWrapper.actualResource
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(8.dp),
        elevation = 8.dp
    ) {
        Row {
            AsyncImage(
                model = giveaway?.thumbnailUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .placeholder(
                        visible = giveawayWrapper is ResourceWrapper.Placeholder,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = MaterialTheme.colors.surface.copy(
                                alpha = 0.15f
                            )
                        )
                    ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = giveaway?.title ?: "Deal Name Placeholder",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
//                        .fillMaxWidth()
                        .placeholder(
                            visible = giveawayWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )

                val giveawayEndDate = buildAnnotatedString {
                    if (giveaway?.endDate != null) {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.onSurface
                            )
                        ) {
                            append("Valid until: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 20.sp,
                                color = MaterialTheme.colors.onSurface
                            )
                        ) {
                            append("${giveaway.endDate.toLocalDate()}")
                        }
                    } else {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colors.onSurface
                            )
                        ) {
                            append("N/A")
                        }
                    }
                }
                val placeholder = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 24.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    ) {
                        append("Deal Release Date placeholder")
                    }
                }
                Text(
                    text = if (giveaway != null) giveawayEndDate else placeholder,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
//                        .fillMaxWidth()
                        .placeholder(
                            visible = giveawayWrapper is ResourceWrapper.Placeholder,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = MaterialTheme.colors.surface.copy(
                                    alpha = 0.15f
                                )
                            )
                        )
                )
            }
            if (giveaway?.worthInUsDollar != null) {
                val worth = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 20.sp
                        )
                    ) {
                        append("Worth: ")
                        withStyle(style = SpanStyle(fontSize = 24.sp)) {
                            append("${giveaway.worthInUsDollar}")
                            withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                append('$')
                            }
                        }
                    }
                }
                Text(
                    text = worth,
                    textAlign = TextAlign.Center,
//                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DealsTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.tabRow,
    shape: Shape = MaterialTheme.shapes.small,
    tabContent: @Composable RowScope.(index: Int) -> Unit,
) {
    val selectedTabOffsetX = remember { Animatable(0f) }
    var tabSize by remember { mutableStateOf(0f) }
    LaunchedEffect(selectedTabIndex) {
        selectedTabOffsetX.animateTo(tabSize * selectedTabIndex)
    }
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.Center)
                .background(color = backgroundColor, shape = shape)
                .padding(4.dp)
                .drawWithCache {
                    onDrawBehind {
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(selectedTabOffsetX.value, 0f),
                            size = size.copy(width = size.width / 2)
                        )
                        tabSize = size.width / 2
                    }
                }
        ) {
            repeat(2) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(),
                    propagateMinConstraints = true,
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.tabContent(index)
                }
            }
        }
    }
}

@Composable
fun DealsTab(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colors.onTab,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            color = contentColor
        )
    }
}

val Colors.tabRow: Color
    get() = if (isLight) Color(0xFFF4F5FA) else Color.DarkGray

val Colors.onTab: Color
    get() = if (isLight) Color.Black else Color.White

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = "id:pixel_6")
@Composable
fun DealsScreenPreview() {
    val dealsState = remember {
        mutableStateOf(
            DealsState(
                deals = UiResult.Content(
                    listOf(
                        ResourceWrapper.ActualResource(            CheapSharkDeal(
                "DEUSEXHUMANREVOLUTIONDIRECTORSCUT",
                "Deus Ex: Human Revolution - Director's Cut",
                "/game/pc/deus-ex-human-revolution---directors-cut",
                "HhzMJAgQYGZ%2B%2BFPpBG%2BRFcuUQZJO3KXvlnyYYGwGUfU%3D",
                1,
                102249,
                2.99f,
                19.99f,
                true,
                85.042521,
                91,
                "Very Positive",
                92,
                17993,
                238010,
                1382400000,
                1621536418,
                9.6f,
                "https://cdn.cloudflare.steamstatic.com/steam/apps/238010/capsule_sm_120.jpg?t=1619788192"
            ).toDeal()),
                        ResourceWrapper.ActualResource(            CheapSharkDeal(
                "THIEFDEADLYSHADOWS",
                "Thief: Deadly Shadows",
                "/game/pc/thief-deadly-shadows",
                "EX0oH20b7A1H2YiVjvVx5A0HH%2F4etw3x%2F6YMGVPpKbA%3D",
                1,
                396,
                0.98f,
                8.99f,
                true,
                89.098999,
                85,
                "Very Positive",
                81,
                1670,
                6980,
                1085443200,
                1621540561,
                9.4f,
                "https://cdn.cloudflare.steamstatic.com/steam/apps/6980/capsule_sm_120.jpg?t=1592493801"
            ).toDeal()),
                        ResourceWrapper.ActualResource(            CheapSharkDeal(
                "CYBERNETICAFINAL",
                "Cybernetica: Final",
                "/game/pc/cybernetica-final",
                "YAm7vyXnmK5YWsFyTy22SFn5p%2F4Lrpu2eCCrrfvNO44%3D",
                1,
                226893,
                1.89f,
                18.99f,
                true,
                90.047393,
                0,
                null,
                0,
                0,
                1549710,
                1614643200,
                1621359373,
                9.1f,
                "https://cdn.cloudflare.steamstatic.com/steam/apps/1549710/capsule_sm_120.jpg?t=1614708882"
            ).toDeal()
),
                    )
                ),
                mmoGamesGiveaways = UiResult.Content(
                    listOf(
                        ResourceWrapper.ActualResource(
                                        MMOGiveawayEntry(
                128061,
                "Castle Clash $200 Bundle Key Giveaway (New Players Only)",
                Percent(21),
                "https://www.mmobomb.com/file/2022/3/castle-clash-box-218x150.png",
                "https://www.mmobomb.com/file/2022/3/castle-clash-box.png",
                "To unlock your key instantly you just need to log in and click the button on the top.",
                "https://www.mmobomb.com/giveaway/castle-clash-starter-keys"
            )
                        ),
                        ResourceWrapper.ActualResource(
                                        MMOGiveawayEntry(
                128077,
                "Doomsday: Last Survivors Gift Key Giveaway (New Players)",
                Percent(24),
                "https://www.mmobomb.com/file/2022/8/doomday-box-218x150.png",
                "https://www.mmobomb.com/file/2022/8/doomday-box.png",
                "To unlock your key instantly you just need to log in and click the button on the top.",
                "https://www.mmobomb.com/giveaway/doomsday-last-survivors-gift-key"
            )
                        ),
                        ResourceWrapper.ActualResource(
                                        MMOGiveawayEntry(
                119744,
                "Eudemons Online Gift Pack Key Giveaway",
                Percent(89),
                "https://www.mmobomb.com/file/2023/3/eudemons-online-gift-pack-key-giveaway-218x150.png",
                "https://www.mmobomb.com/file/2023/3/eudemons-online-gift-pack-key-giveaway.png",
                "To unlock your key instantly you just need to complete all the steps on the top.",
                "https://www.mmobomb.com/giveaway/eudemons-online-gift-pack-key-giveaway"
            )
                        ),
                    )
                ),
                otherGamesGiveaways = UiResult.Content(
                    listOf(
                        ResourceWrapper.ActualResource(
                                        com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry(
                2301,
                "World of Warships - Starter Pack: Ishizuchi",
                "$23.47",
                "https://www.gamerpower.com/offers/1/641df70fc9883.jpg",
                "https://www.gamerpower.com/offers/1b/641df70fc9883.jpg",
                "Claim your free World of Warships - Starter Pack and unlock the Premium battleship Ishizuchi, Japanese Tier IV, plus camouflages and bonuses.",
                "1- Download this DLC content directly via Epic Games Store before the offer expires.\r\n2 - Please note the base game World of Warships (free-to-play) is required to enjoy this content.",
                "https://www.gamerpower.com/open/world-of-warships-starter-pack-ishizuchi",
                "2023-03-24 15:16:31",
                "DLC",
                "PC, Epic Games Store",
                "2023-03-30 23:59:00",
                930,
                GiveawayEntryStatus.Active,
                "https://www.gamerpower.com/world-of-warships-starter-pack-ishizuchi",
            ).toCoreGamerPowerGiveawayEntry()
                        ),
                        ResourceWrapper.ActualResource(
                                        com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry(
                2300,
                "Spandex Force: Champion Rising",
                "$9.99",
                "https://www.gamerpower.com/offers/1/641de37e3e819.jpg",
                "https://www.gamerpower.com/offers/1b/641de37e3e819.jpg",
                "Grab Spandex Force: Champion Rising for free and create your own hero today!",
                "1. Login into your free IndieGala account.\r\n2. Scroll down and click the \"Add to Your Library\" button to add the game to your library.\r\n3. Go to \"My Library\" to find your game.",
                "https://www.gamerpower.com/open/spandex-force-champion-rising",
                "2023-03-24 13:53:02",
                "Game",
                "PC, DRM-Free",
                null,
                1480,
                GiveawayEntryStatus.Active,
                "https://www.gamerpower.com/spandex-force-champion-rising",
            ).toCoreGamerPowerGiveawayEntry()
                        ),
                        ResourceWrapper.ActualResource(
                            com.mr3y.ludi.core.network.model.GamerPowerGiveawayEntry(
                1178,
                "Dungeons and Dragons Online: Free Quest Packs",
                null,
                "https://www.gamerpower.com/offers/1/61698e2ca90cf.jpg",
                "https://www.gamerpower.com/offers/1b/61698e2ca90cf.jpg",
                "Claim the code below and unlock lots of Dungeons and Dragons quest packs, raids, and adventure packs permanently on your account for free! This special code is available to redeem through April 23rd, 2023 so you will need to act fast!\r\n\r\nCode: DUNGEONCRAWL",
                "1. Redeem your code before April 23rd, 2023.\r\n2. Enjoy!",
                "https://www.gamerpower.com/open/dungeons-and-dragons-online-all-quest-packs",
                "2023-03-24 13:46:12",
                "DLC",
                "PC",
                "2023-04-23 23:59:00",
                3540,
                GiveawayEntryStatus.Active,
                "https://www.gamerpower.com/dungeons-and-dragons-online-all-quest-packs",
            ).toCoreGamerPowerGiveawayEntry()
                        ),
                    )
                )
            )
        )
    }
    LudiTheme {
        DealsScreen(dealsState = dealsState.value, modifier = Modifier.fillMaxSize())
    }
}