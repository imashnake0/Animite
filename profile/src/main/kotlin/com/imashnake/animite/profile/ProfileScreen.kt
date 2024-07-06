package com.imashnake.animite.profile

import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.extensions.animiteBlockQuoteStyle
import com.imashnake.animite.core.extensions.animiteCodeBlockStyle
import com.imashnake.animite.core.extensions.crossfadeModel
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.extensions.maxHeight
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaSmall
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.StatsRow
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlinx.coroutines.launch
import com.imashnake.animite.core.R as coreR

@Suppress("LongMethod", "UNUSED_PARAMETER")
@Destination(
    route = "user",
    deepLinks = [
        DeepLink(
            uriPattern = ANILIST_AUTH_DEEPLINK
        )
    ]
)
@RootNavGraph(start = true)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    accessToken: String? = null,
    tokenType: String? = null,
    // TODO: Log user out if token expires.
    expiresIn: Int? = null
) {
    accessToken?.let { viewModel.setAccessToken(it) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val viewer by viewModel.viewer.collectAsState()
    viewer.data?.id?.let { viewModel.setViewerId(it) }
    val viewerMediaLists by viewModel.viewerMediaList.collectAsState(initial = null)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoggedIn -> viewer.data?.run {
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
                                    .landscapeCutoutPadding()
                                    .padding(start = LocalPaddings.current.medium)
                                    .size(100.dp)
                            )
                        }
                    },
                    content = {
                        Column {
                            Text(
                                text = name,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(
                                        horizontal = LocalPaddings.current.large
                                    )
                                    .landscapeCutoutPadding()
                            )
                            AboutUser(
                                about,
                                modifier = Modifier
                                    .maxHeight(dimensionResource(R.dimen.user_about_height))
                                    .padding(horizontal = LocalPaddings.current.large)
                                    .landscapeCutoutPadding()
                            )
                            Spacer(Modifier.size(LocalPaddings.current.medium))
                            UserTabs(
                                user = this@run,
                                mediaCollection = viewerMediaLists?.data
                            )
                        }
                    },
                    contentModifier = Modifier
                        .padding(
                            top = LocalPaddings.current.large,
                            bottom = dimensionResource(coreR.dimen.navigation_bar_height)
                        )
                )
            }
            else -> Login()
        }
    }
}

@Composable
private fun AboutUser(about: String?, modifier: Modifier = Modifier) {
    about?.let {
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
                    modifier = contentModifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTabs(
    user: User,
    mediaCollection: User.MediaCollection?,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { ProfileTabs.entries.size })
    val titles = ProfileTabs.entries
    val onBackground = MaterialTheme.colorScheme.onBackground

    Column(modifier) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.landscapeCutoutPadding(),
            containerColor = MaterialTheme.colorScheme.background,
            divider = {}
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
            Box(
                Modifier
                    .fillMaxSize()
                    .landscapeCutoutPadding()) {
                when (ProfileTabs.entries[page]) {
                    ProfileTabs.ABOUT -> AboutTab(user, mediaCollection)
                    else -> Text(
                        text = stringResource(coreR.string.coming_soon),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

enum class ProfileTabs(@StringRes val titleRes: Int) {
    ABOUT(R.string.about),
    ANIME(R.string.anime),
    MANGA(R.string.manga),
    FAVOURITES(R.string.favourites),
    STATISTICS(R.string.statistics)
}

@Composable
private fun AboutTab(
    user: User,
    mediaCollection: User.MediaCollection?,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val statsLabelToValue = listOf(
        stringResource(R.string.total_anime) to user.count?.toString(),
        stringResource(R.string.days_watched) to user.daysWatched?.let { "%.1f".format(it) },
        stringResource(R.string.mean_score) to user.meanScore?.let { "%.1f".format(it) }
    ).filter { it.second != null }

    // TODO: Cleanup this layout.
    Column(modifier.verticalScroll(scrollState)) {
        Column(
            modifier = Modifier.padding(LocalPaddings.current.large),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
        ) {
            StatsRow(
                stats = statsLabelToValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalPaddings.current.large)
                    .landscapeCutoutPadding()
            ) {
                Text(
                    text = it.first,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )

                it.second?.let { value ->
                    Text(
                        text = value,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }

            if (user.genres.isNotEmpty()) Genres(user.genres)

        }
        // TODO: Why is this not smart-casting?
        if (!mediaCollection?.namedLists.isNullOrEmpty()) {
            UserMediaList(mediaCollection!!.namedLists)
        }
    }
}

@Composable
private fun Genres(
    genres: List<User.Genre>,
    modifier: Modifier = Modifier
) {
    val barWidthAnimation = remember { Animatable(0f) }
    val barAlphaAnimation = remember { Animatable(0f) }
    val barColor = MaterialTheme.colorScheme.primary
    LaunchedEffect("barAnimation") {
        launch {
            barWidthAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, delayMillis = 250)
            )
        }
        launch {
            barAlphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, delayMillis = 250)
            )
        }
    }

    Text(
        text = stringResource(R.string.genres),
        style = MaterialTheme.typography.titleMedium
    )
    Row(modifier.height(IntrinsicSize.Max)) {
        Column(horizontalAlignment = Alignment.End) {
            genres.forEach {
                Text(
                    text = it.genre,
                    style = m3TextStyles().textStyle.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    start = LocalPaddings.current.small,
                    end = LocalPaddings.current.large
                )
                .widthIn(max = 250.dp),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)
        ) {
            val highestCount = genres.maxOf { it.mediaCount }
            genres.forEach {
                val weight = it.mediaCount/highestCount.toFloat()
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = weight)
                        .weight(1f)
                        .graphicsLayer { alpha = weight * barAlphaAnimation.value }
                        .drawBehind {
                            drawRoundRect(
                                color = barColor,
                                size = Size(
                                    width = size.width * barWidthAnimation.value,
                                    height = size.height
                                ),
                                cornerRadius = CornerRadius(size.height)
                            )
                        },
                )
            }
        }
    }
}

@Composable
private fun UserMediaList(
    lists: List<User.MediaCollection.NamedList>,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large)) {
        lists.fastForEach {
            MediaSmallRow(it.name, it.list, modifier) { media ->
                MediaSmall(
                    image = media.coverImage,
                    label = media.title,
                    onClick = {},
                    modifier = Modifier.width(dimensionResource(coreR.dimen.media_card_width))
                )
            }
        }
    }
}
