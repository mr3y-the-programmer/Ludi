package com.mr3y.ludi.shared.ui.screens.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.imeAction
import androidx.compose.ui.semantics.performImeAction
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mr3y.ludi.shared.core.model.Article
import com.mr3y.ludi.shared.core.model.NewReleaseArticle
import com.mr3y.ludi.shared.di.getScreenModel
import com.mr3y.ludi.shared.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.shared.ui.components.LudiErrorBox
import com.mr3y.ludi.shared.ui.components.LudiSectionHeader
import com.mr3y.ludi.shared.ui.components.RefreshIconButton
import com.mr3y.ludi.shared.ui.navigation.BottomBarTab
import com.mr3y.ludi.shared.ui.navigation.PreferencesType
import com.mr3y.ludi.shared.ui.presenter.NewsViewModel
import com.mr3y.ludi.shared.ui.presenter.model.NewsState
import com.mr3y.ludi.shared.ui.presenter.model.NewsStateEvent
import com.mr3y.ludi.shared.ui.resources.isDesktopPlatform
import com.mr3y.ludi.shared.ui.screens.settings.EditPreferencesScreen

object NewsScreenTab : Screen, BottomBarTab {

    override val key: ScreenKey
        get() = "news"

    override val label: String
        get() = "News"
    override val icon: ImageVector
        get() = Icons.Outlined.Upcoming

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<NewsViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        NewsScreen(
            onTuneClick = { navigator.push(EditPreferencesScreen(PreferencesType.NewsDataSources)) },
            viewModel = screenModel
        )
    }
}

@Composable
expect fun NewsScreen(onTuneClick: () -> Unit, modifier: Modifier = Modifier, viewModel: NewsViewModel)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NewsScreen(
    newsState: NewsState,
    searchQuery: String,
    onSearchQueryValueChanged: (String) -> Unit,
    onTuneClick: () -> Unit,
    onRefresh: () -> Unit,
    onOpenUrl: (url: String) -> Unit,
    onConsumeEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPullRefreshState(
        refreshing = newsState.isRefreshing,
        onRefresh = onRefresh
    )
    val strings = LocalStrings.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(newsState.currentEvent) {
        when (newsState.currentEvent) {
            is NewsStateEvent.FailedToFetchNetworkResults -> {
                snackbarHostState.showSnackbar(
                    message = "Couldn't fetch feed updates from server"
                )
                onConsumeEvent()
            }
            null -> {}
        }
    }
    Scaffold(
        modifier = modifier.pullRefresh(state),
        topBar = {
            TopAppBar(
                title = {
                    val contentDesc = strings.news_page_search_bar_content_description
                    val softwareKeyboard = LocalSoftwareKeyboardController.current
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryValueChanged,
                        shape = RoundedCornerShape(50),
                        placeholder = {
                            Text(text = strings.news_page_search_bar_placeholder)
                        },
                        colors = TextFieldDefaults.colors(
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            IconButton(
                                onClick = { /*TODO*/ },
                                modifier = Modifier.clearAndSetSemantics { }
                            ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Icons.Filled.Search),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { softwareKeyboard?.hide() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clearAndSetSemantics {
                                focused = true
                                contentDescription = contentDesc
                                imeAction = ImeAction.Search
                                performImeAction {
                                    softwareKeyboard?.hide() ?: return@performImeAction false
                                    true
                                }
                            }
                    )
                },
                actions = {
                    if (isDesktopPlatform()) {
                        RefreshIconButton(onClick = onRefresh)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    IconButton(
                        onClick = onTuneClick,
                        modifier = Modifier
                            .requiredSize(48.dp)
                            .semantics {
                                contentDescription = strings.news_page_filter_icon_content_description
                            }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Filled.Tune),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedNoInternetBanner()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp)
                ) {
                    val headerModifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .padding(bottom = 8.dp)

                    val cardModifier = Modifier
                        .width(width = 248.dp)
                        .height(IntrinsicSize.Min)
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = headerModifier
                        ) {
                            Icon(
                                imageVector = Icons.Filled.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clearAndSetSemantics { }
                            )
                            LudiSectionHeader(
                                text = "Latest",
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                        FeedSectionScaffold(
                            sectionFeed = newsState.newsFeed.collectAsLazyPagingItems(),
                            modifier = Modifier.fillMaxWidth(),
                            onEmptySuccessfulResultLabel = strings.news_no_news_to_show
                        ) { lazyRowState, article ->
                            ArticleCardTile(
                                article = article,
                                lazyListState = lazyRowState,
                                onClick = {
                                    if (article != null) {
                                        onOpenUrl(article.sourceLinkUrl)
                                    }
                                },
                                modifier = cardModifier
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        LudiSectionHeader(
                            text = "Reviews",
                            modifier = headerModifier
                        )
                        FeedSectionScaffold(
                            sectionFeed = newsState.reviewsFeed.collectAsLazyPagingItems(),
                            modifier = Modifier.fillMaxWidth(),
                            onEmptySuccessfulResultLabel = strings.news_no_reviews_to_show
                        ) { lazyRowState, article ->
                            ArticleCardTile(
                                article = article,
                                lazyListState = lazyRowState,
                                onClick = {
                                    if (article != null) {
                                        onOpenUrl(article.sourceLinkUrl)
                                    }
                                },
                                modifier = cardModifier
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        LudiSectionHeader(
                            text = "Upcoming releases",
                            modifier = headerModifier
                        )
                    }
                    item {
                        NewReleasesSection(
                            newReleases = newsState.newReleasesFeed.collectAsLazyPagingItems(),
                            modifier = Modifier.fillMaxWidth(),
                            onEmptySuccessfulResultLabel = strings.news_no_releases_to_show
                        ) {
                            NewReleaseTile(
                                newReleaseArticle = it,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                onClick = {
                                    if (it != null) {
                                        onOpenUrl(it.sourceLinkUrl)
                                    }
                                }
                            )
                            Divider(modifier = Modifier.padding(end = 16.dp))
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = newsState.isRefreshing,
                state = state,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NewReleasesSection(
    newReleases: LazyPagingItems<NewReleaseArticle>,
    onEmptySuccessfulResultLabel: String,
    modifier: Modifier = Modifier,
    itemContent: @Composable (NewReleaseArticle?) -> Unit
) {
    Column(modifier = modifier) {
        if (newReleases.loadState.refresh is LoadStateLoading) {
            repeat(10) {
                itemContent(null)
            }
        }

        if (newReleases.loadState.refresh is LoadStateNotLoading) {
            if (newReleases.itemSnapshotList.isEmpty()) {
                Empty(
                    label = onEmptySuccessfulResultLabel,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                repeat(newReleases.itemCount) { index ->
                    key(index) {
                        itemContent(newReleases[index])
                    }
                }
            }
        }

        if (newReleases.loadState.refresh is LoadStateError) {
            LudiErrorBox(modifier = Modifier.fillMaxWidth())
        }

        if (newReleases.loadState.append is LoadStateLoading) {
            repeat(10) {
                itemContent(null)
            }
        }

        if (newReleases.loadState.append is LoadStateError) {
            LudiErrorBox(modifier = Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Article> FeedSectionScaffold(
    sectionFeed: LazyPagingItems<T>,
    onEmptySuccessfulResultLabel: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    itemContent: @Composable (LazyListState, T?) -> Unit
) {
    val state = rememberLazyListState()
    LazyRow(
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        if (sectionFeed.loadState.refresh is LoadStateLoading) {
            items(10) {
                itemContent(state, null)
            }
        }

        if (sectionFeed.loadState.refresh is LoadStateNotLoading) {
            if (sectionFeed.itemSnapshotList.isEmpty()) {
                item {
                    Empty(
                        label = onEmptySuccessfulResultLabel,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(
                    count = sectionFeed.itemCount,
                    key = sectionFeed.itemKey { it.sourceLinkUrl },
                    contentType = sectionFeed.itemContentType { it }
                ) { index ->
                    itemContent(state, sectionFeed[index])
                }
            }
        }

        if (sectionFeed.loadState.refresh is LoadStateError) {
            item {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }

        if (sectionFeed.loadState.append is LoadStateLoading) {
            items(10) {
                itemContent(state, null)
            }
        }

        if (sectionFeed.loadState.append is LoadStateError) {
            item {
                LudiErrorBox()
            }
        }
    }
}

@Composable
fun Empty(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}
