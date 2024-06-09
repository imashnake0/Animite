package com.imashnake.animite.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.animiteBlockQuoteStyle
import com.imashnake.animite.core.extensions.animiteCodeBlockStyle
import com.imashnake.animite.core.extensions.crossfadeModel
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.extensions.maxHeight
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.ProgressIndicator
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.imashnake.animite.core.R as coreR

@OptIn(ExperimentalMaterialApi::class)
@Suppress("LongMethod", "UNUSED_PARAMETER")
@Destination(
    route = "user",
    deepLinks = [DeepLink(uriPattern = ANILIST_AUTH_DEEPLINK)]
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
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing && viewer !is Resource.Loading,
        onRefresh = {
            viewModel.setNetworkMode(useNetwork = true)
            viewModel.refresh()
        },
        refreshingOffset = PullRefreshDefaults.RefreshingOffset + with(LocalDensity.current) {
            WindowInsets.displayCutout.getTop(this).toDp()
        }
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pullRefresh(pullRefreshState)
            .verticalScroll(rememberScrollState())
    ) {
        when {
            isLoggedIn &&
            viewer is Resource.Success -> viewer.data?.run {
                BannerLayout(
                    banner = {
                        Box {
                            AsyncImage(
                                model = crossfadeModel(bannerImage),
                                contentDescription = "banner",
                                modifier = it,
                                contentScale = ContentScale.Crop
                            )
                            AsyncImage(
                                model = crossfadeModel(avatar?.large),
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
                                modifier = Modifier.padding(horizontal = LocalPaddings.current.large)
                            )
                            AboutUser(
                                about,
                                modifier = Modifier
                                    .maxHeight(dimensionResource(R.dimen.user_about_height))
                                    .padding(horizontal = LocalPaddings.current.large)
                            )
                            Spacer(Modifier.size(LocalPaddings.current.medium))
                            UserTabs()
                        }
                    },
                    contentModifier = Modifier
                        .landscapeCutoutPadding()
                        .padding(
                            top = LocalPaddings.current.large,
                            bottom = dimensionResource(coreR.dimen.navigation_bar_height)
                        ),
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            !isLoggedIn -> Login()
            isLoggedIn && viewer.data == null -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    ProgressIndicator()
                }
            }
        }

        if (isLoggedIn)
            // TODO: Replace with custom indicator.
            PullRefreshIndicator(
                refreshing = isRefreshing && viewer !is Resource.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary,
                scale = true
            )
    }
}

@Composable
fun AboutUser(about: String?, modifier: Modifier = Modifier) {
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
fun UserTabs(modifier: Modifier = Modifier) {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("About", "Anime", "Manga", "Fave", "Stats")
    val onBackground = MaterialTheme.colorScheme.onBackground

    Column(modifier) {
        PrimaryTabRow(
            selectedTabIndex = state,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {}
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            text = title,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = onBackground.copy(
                                alpha = if (state == index) 1f else 0.5f
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
        Box(
            Modifier
                .height(300.dp)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            onBackground.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Text(
                text = stringResource(coreR.string.coming_soon),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
