package com.imashnake.animite.features.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.R
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.extensions.thenIf
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaSmall
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.core.ui.ProgressIndicatorScreen
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.shaders.etherealShader
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.navigation.SharedContentKey.Component.Text
import com.materialkolor.ktx.hasEnoughContrast
import com.imashnake.animite.core.R as coreR
import com.imashnake.animite.media.R as mediaR
import com.imashnake.animite.navigation.R as navigationR

@Composable
@Suppress("LongMethod")
fun HomeScreen(
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeMediaType = rememberSaveable { mutableStateOf(MediaType.ANIME) }
    viewModel.setMediaType(homeMediaType.value)

    val trendingList by viewModel.trendingMedia.collectAsState()
    val popularList by viewModel.popularMediaThisSeason.collectAsState()
    val upcomingList by viewModel.upcomingMediaNextSeason.collectAsState()
    val allTimePopularList by viewModel.allTimePopular.collectAsState()

    val rows = listOf(
        trendingList,
        popularList,
        upcomingList,
        allTimePopularList,
    )

    val time = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        do {
            withFrameMillis {
                time.floatValue += 0.01f
            }
        } while (true)
    }
    val shader = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            RuntimeShader(etherealShader) else null
    }

    when {
        rows.all { it is Resource.Success } -> {
            val scrollState = rememberScrollState()
            TranslucentStatusBarLayout(scrollState) {
                Box(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .navigationBarsPadding()
                ) {
                    BannerLayout(
                        banner = { bannerModifier ->
                            Box {
                                Image(
                                    painter = painterResource(mediaR.drawable.background),
                                    contentDescription = null,
                                    modifier = bannerModifier.bannerParallax(scrollState),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.TopCenter
                                )

                                Row(
                                    modifier = bannerModifier.thenIf(shader != null) {
                                        drawWithCache @SuppressLint("NewApi") {
                                            shader!!.run {
                                                setFloatUniform(
                                                    "resolution",
                                                    size.width,
                                                    size.height
                                                )
                                                setFloatUniform("time", time.floatValue)
                                                setColorUniform(
                                                    "orb",
                                                    Color(0xFF6C408D).toArgb()
                                                )
                                                setColorUniform(
                                                    "bg",
                                                    android.graphics.Color.TRANSPARENT
                                                )
                                                onDrawBehind {
                                                    drawRect(ShaderBrush(this@run))
                                                }
                                            }
                                        }
                                    },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    Text(
                                        text = stringResource(R.string.okaeri),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.takeIf {
                                            // This color is what is behind "okaeri".
                                            it.hasEnoughContrast(Color(0xFF252B33))
                                        } ?: MaterialTheme.colorScheme.secondaryContainer,
                                        style = MaterialTheme.typography.displayMedium,
                                        modifier = Modifier
                                            .padding(
                                                start = LocalPaddings.current.large,
                                                bottom = LocalPaddings.current.medium
                                            )
                                            .thenIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                                padding(start = dimensionResource(navigationR.dimen.navigation_rail_width))
                                            }
                                            .landscapeCutoutPadding()
                                            .weight(1f, fill = false),
                                        maxLines = 1
                                    )

                                    MediaTypeSelector(
                                        modifier = Modifier
                                            .padding(
                                                end = LocalPaddings.current.large,
                                                bottom = LocalPaddings.current.medium
                                            )
                                            .landscapeCutoutPadding(),
                                        selectedOption = homeMediaType,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        },
                        content = {
                            rows.fastForEach { row ->
                                row.data?.let {
                                    AnimatedContent(
                                        targetState = it,
                                        transitionSpec = {
                                            fadeIn(tween(750))
                                                .togetherWith(fadeOut(tween(750)))
                                        },
                                        label = "animate_home_row"
                                    ) { mediaList ->
                                        if (mediaList.list.isNotEmpty())
                                            HomeRow(
                                                list = mediaList.list,
                                                type = mediaList.type,
                                                onItemClicked = { media ->
                                                    onNavigateToMediaItem(
                                                        MediaPage(
                                                            id = media.id,
                                                            // TODO: We can use the list's index instead.
                                                            source = mediaList.type.name,
                                                            mediaType = homeMediaType.value.rawValue,
                                                        )
                                                    )
                                                },
                                                sharedTransitionScope = sharedTransitionScope,
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                modifier = Modifier.padding(
                                                    vertical = LocalPaddings.current.large / 2
                                                )
                                            )
                                        else
                                            /* With this, AnimatedContent shrinks/expands the
                                            `HomeRow` vertically. */
                                            Box(Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        },
                        contentModifier = Modifier.padding(
                            top = LocalPaddings.current.large / 2,
                            bottom = LocalPaddings.current.large / 2,
                        ).thenIf(
                            condition = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE,
                            other = { padding(start = dimensionResource(navigationR.dimen.navigation_rail_width)) },
                            elseOther = { padding(bottom = dimensionResource(navigationR.dimen.navigation_bar_height)) }
                        ),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    )
                }
            }
        }
        else -> ProgressIndicatorScreen()
    }
}

@Composable
fun HomeRow(
    list: List<Media.Small>,
    type: MediaList.Type,
    onItemClicked: (Media.Small) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    MediaSmallRow(type.title, list, modifier) { media ->
        with(sharedTransitionScope) {
            MediaSmall(
                image = media.coverImage,
                label = media.title,
                onClick = { onItemClicked(media) },
                imageHeight = dimensionResource(coreR.dimen.media_image_height),
                cardWidth = dimensionResource(coreR.dimen.media_card_width),
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = type.name,
                            sharedComponents = Card to Page,
                        )
                    ),
                    animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                ),
                imageModifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = type.name,
                            sharedComponents = Image to Image,
                        )
                    ),
                    animatedVisibilityScope,
                ),
                textModifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = type.name,
                            sharedComponents = Text to Text,
                        )
                    ),
                    animatedVisibilityScope,
                ),
            )
        }
    }
}

@Composable
@Suppress("CognitiveComplexMethod")
private fun MediaTypeSelector(
    modifier: Modifier = Modifier,
    selectedOption: MutableState<MediaType>,
    viewModel: HomeViewModel
) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.onBackground,
            shape = CircleShape
        )
    ) {
        val offset by animateDpAsState(
            targetValue = if (selectedOption.value == MediaType.ANIME) 0.dp else 40.dp,
            label = "media_switch"
        )

        Box(
            modifier = Modifier
                .padding(dimensionResource(mediaR.dimen.media_type_selector_padding))
                .size(dimensionResource(mediaR.dimen.media_type_choice_size))
                .offset { IntOffset(x = offset.roundToPx(), y = 0) }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        )

        Row(
            modifier = Modifier
                .height(dimensionResource(mediaR.dimen.media_type_selector_height))
                .width(dimensionResource(mediaR.dimen.media_type_selector_width))
                .padding(dimensionResource(mediaR.dimen.media_type_selector_padding)),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaType.knownValues().forEach { mediaType ->
                IconButton(
                    onClick = {
                        if (selectedOption.value != mediaType) {
                            viewModel.setMediaType(mediaType)
                            selectedOption.value = mediaType
                        }
                    },
                    modifier = Modifier.requiredWidth(dimensionResource(mediaR.dimen.media_type_choice_size))
                ) {
                    Icon(
                        imageVector = when (mediaType) {
                            MediaType.ANIME -> Icons.Rounded.PlayArrow
                            else -> ImageVector.vectorResource(id = R.drawable.manga)
                        },
                        contentDescription = mediaType.name,
                        tint = animateColorAsState(
                            targetValue = when(selectedOption.value) {
                                mediaType -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.background
                            },
                            animationSpec = tween(400),
                            label = "icon_color"
                        ).value
                    )
                }
            }
        }
    }
}
