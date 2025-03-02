package com.imashnake.animite.profile

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.Paddings
import com.imashnake.animite.core.extensions.animiteBlockQuoteStyle
import com.imashnake.animite.core.extensions.animiteCodeBlockStyle
import com.imashnake.animite.core.extensions.crossfadeModel
import com.imashnake.animite.core.extensions.maxHeight
import com.imashnake.animite.core.ui.FallbackMessage
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.ProgressIndicatorScreen
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.profile.tabs.AboutTab
import com.imashnake.animite.profile.tabs.FavouritesTab
import com.imashnake.animite.profile.tabs.MediaTab
import com.imashnake.animite.profile.tabs.ProfileTab
import kotlinx.coroutines.launch
import com.imashnake.animite.core.R as coreR
import com.imashnake.animite.navigation.R as navigationR

@Suppress("LongMethod")
@Composable
fun ProfileScreen(
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ProfileViewModel = hiltViewModel(),
    contentWindowInsets: WindowInsets = WindowInsets.safeDrawing,
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val layoutDirection = LocalLayoutDirection.current
    val horizontalInsets = Paddings(
        start = insetPaddingValues.calculateStartPadding(layoutDirection),
        end = insetPaddingValues.calculateEndPadding(layoutDirection),
    )

    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val viewer by viewModel.viewer.collectAsState()
    val viewerAnimeLists by viewModel.viewerAnimeLists.collectAsState()
    val viewerMangaLists by viewModel.viewerMangaLists.collectAsState()

    val data = listOf(viewer, viewerAnimeLists, viewerMangaLists)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoggedIn -> when {
                data.all { it is Resource.Success } -> viewer.data?.run {
                    BannerLayout(
                        banner = {
                            Box {
                                AsyncImage(
                                    model = crossfadeModel(banner),
                                    contentDescription = "banner",
                                    modifier = it,
                                    contentScale = ContentScale.Crop
                                )
                                AsyncImage(
                                    model = crossfadeModel(avatar),
                                    contentDescription = "avatar",
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(start = LocalPaddings.current.medium)
                                        .padding(horizontalInsets)
                                        .size(100.dp)
                                )
                            }
                        },
                        content = {
                            Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)) {
                                Column(Modifier.padding(horizontalInsets)) {
                                    Text(
                                        text = name,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.titleLarge,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = LocalPaddings.current.large)
                                    )
                                    UserDescription(
                                        description = description,
                                        modifier = Modifier
                                            .maxHeight(dimensionResource(R.dimen.user_about_height))
                                            .padding(horizontal = LocalPaddings.current.large)
                                    )
                                }
                                UserTabs(
                                    user = this@run,
                                    animeCollection = viewerAnimeLists.data,
                                    mangaCollection = viewerMangaLists.data,
                                    onNavigateToMediaItem = onNavigateToMediaItem,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    contentInsetPadding = horizontalInsets
                                )
                            }
                        },
                        contentModifier = Modifier.padding(
                            top = LocalPaddings.current.large / 2,
                            bottom = LocalPaddings.current.large / 2 +
                                    dimensionResource(navigationR.dimen.navigation_bar_height)
                        )
                    )
                }
                else -> ProgressIndicatorScreen()
            }
            else -> Login()
        }
    }
}

@Composable
private fun UserDescription(description: String?, modifier: Modifier = Modifier) {
    description?.let {
        Box(modifier) {
            NestedScrollableContent { contentModifier ->
                MarkdownDocument(
                    markdown = it,
                    // TODO: Fix typography and make this an `animiteTextStyle()`.
                    textStyles = m3TextStyles().copy(
                        textStyle = m3TextStyles().textStyle.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)
                        )
                    ),
                    blockQuoteStyle = animiteBlockQuoteStyle(),
                    codeBlockStyle = animiteCodeBlockStyle(),
                    modifier = contentModifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTabs(
    user: User,
    animeCollection: User.MediaCollection?,
    mangaCollection: User.MediaCollection?,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentInsetPadding: PaddingValues = PaddingValues(),
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { ProfileTab.entries.size })
    val titles = ProfileTab.entries
    val onBackground = MaterialTheme.colorScheme.onBackground

    Column(modifier) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {},
            modifier = Modifier.padding(contentInsetPadding)
        ) {
            titles.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            text = stringResource(tab.titleRes),
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = onBackground.copy(
                                alpha = if (pagerState.currentPage == index) 1f else 0.5f
                            ),
                            maxLines = 1
                        )
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = LocalPaddings.current.ultraTiny,
                            vertical = LocalPaddings.current.small
                        )
                        .clip(CircleShape)
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            onBackground.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    )
                )
        ) { page ->
            Box(Modifier.fillMaxSize()) {
                when (ProfileTab.entries[page]) {
                    ProfileTab.ABOUT -> AboutTab(
                        user = user,
                        contentPadding = contentInsetPadding,
                    )
                    ProfileTab.ANIME -> MediaTab(
                        mediaCollection = animeCollection,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        contentPadding = Paddings(
                            horizontal = LocalPaddings.current.large,
                            vertical = LocalPaddings.current.large / 2,
                        ) + Paddings(
                            start = contentInsetPadding.calculateStartPadding(LocalLayoutDirection.current),
                            top = contentInsetPadding.calculateTopPadding(),
                            end = contentInsetPadding.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = contentInsetPadding.calculateBottomPadding()
                        ),
                    )
                    ProfileTab.MANGA -> MediaTab(
                        mediaCollection = mangaCollection,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        contentPadding = Paddings(
                            horizontal = LocalPaddings.current.large,
                            vertical = LocalPaddings.current.large / 2,
                        ) + Paddings(
                            start = contentInsetPadding.calculateStartPadding(LocalLayoutDirection.current),
                            top = contentInsetPadding.calculateTopPadding(),
                            end = contentInsetPadding.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = contentInsetPadding.calculateBottomPadding()
                        ),
                    )
                    ProfileTab.FAVOURITES -> FavouritesTab(
                        favouriteLists = user.favourites,
                        contentPadding = Paddings(
                            horizontal = LocalPaddings.current.large,
                            vertical = LocalPaddings.current.large / 2,
                        ) + Paddings(
                            start = contentInsetPadding.calculateStartPadding(LocalLayoutDirection.current),
                            top = contentInsetPadding.calculateTopPadding(),
                            end = contentInsetPadding.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = contentInsetPadding.calculateBottomPadding()
                        ),
                    )
                    else -> FallbackMessage(
                        message = stringResource(coreR.string.coming_soon),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(contentInsetPadding),
                    )
                }
            }
        }
    }
}
