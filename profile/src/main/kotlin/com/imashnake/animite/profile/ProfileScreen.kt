package com.imashnake.animite.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.api.anilist.sanitize.profile.Viewer
import com.imashnake.animite.core.extensions.animiteBlockQuoteStyle
import com.imashnake.animite.core.extensions.animiteCodeBlockStyle
import com.imashnake.animite.core.extensions.crossfadeModel
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.extensions.maxHeight
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
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
                                modifier = Modifier.padding(horizontal = LocalPaddings.current.large)
                            )
                            AboutUser(
                                about,
                                modifier = Modifier
                                    .maxHeight(dimensionResource(R.dimen.user_about_height))
                                    .padding(horizontal = LocalPaddings.current.large)
                            )
                            Spacer(Modifier.size(LocalPaddings.current.medium))
                            UserTabs(this@run)
                        }
                    },
                    contentModifier = Modifier
                        .landscapeCutoutPadding()
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
private fun UserTabs(viewer: Viewer, modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf(ProfileTabs.ABOUT) }
    val titles = ProfileTabs.entries
    val onBackground = MaterialTheme.colorScheme.onBackground

    Column(modifier) {
        PrimaryTabRow(
            selectedTabIndex = state.ordinal,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {}
        ) {
            titles.forEachIndexed { index, tab ->
                Tab(
                    selected = state.ordinal == index,
                    onClick = { state = ProfileTabs.entries[index] },
                    text = {
                        Text(
                            text = tab.title,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = onBackground.copy(
                                alpha = if (state.ordinal == index) 1f else 0.5f
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
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            onBackground.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            // TODO: Use `AnimatedContent`.
            when (state) {
                ProfileTabs.ABOUT -> AboutTab(viewer)
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

enum class ProfileTabs(val title: String) {
    ABOUT("About"),
    ANIME("Anime"),
    MANGA("Manga"),
    FAVOURITES("Fave"),
    STATISTICS("Stats")
}

@Composable
fun AboutTab(
    viewer: Viewer,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            top = LocalPaddings.current.large,
            start = LocalPaddings.current.large,
            end = LocalPaddings.current.large
        ),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
    ) {
        Genres(viewer.genres)
    }
}

@Composable
fun Genres(
    genres: List<Viewer.Genre>,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Genres",
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
                .padding(start = LocalPaddings.current.small),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)
        ) {
            val highestCount = genres.maxOf { it.mediaCount }
            genres.forEach {
                val weight = it.mediaCount/highestCount.toFloat() - 0.2f
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = weight)
                        .weight(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = weight + 0.2f)),
                ) {  }
            }
        }
    }
}
