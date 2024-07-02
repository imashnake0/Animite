package com.imashnake.animite.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.ui.BlankBox
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.ProgressIndicator
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.media.MediaPageArgs
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.imashnake.animite.core.R as coreR

@Destination
@Composable
@Suppress("LongMethod")
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val homeMediaType = rememberSaveable { mutableStateOf(MediaType.ANIME) }
    viewModel.setMediaType(homeMediaType.value)

    val trendingList by viewModel.trendingMedia.collectAsState()
    val popularList by viewModel.popularMediaThisSeason.collectAsState()
    val upcomingList by viewModel.upcomingMediaNextSeason.collectAsState()
    val allTimePopularList by viewModel.allTimePopular.collectAsState()

    val rows = listOf(
        trendingList to stringResource(R.string.trending_now),
        popularList to stringResource(R.string.popular_this_season),
        upcomingList to stringResource(R.string.upcoming_next_season),
        allTimePopularList to stringResource(R.string.all_time_popular)
    )

    when {
        rows.all { it.first is Resource.Success } -> {
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
                                    painter = painterResource(R.drawable.background),
                                    contentDescription = null,
                                    modifier = bannerModifier.bannerParallax(scrollState),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.TopCenter
                                )

                                BlankBox(
                                    modifier = bannerModifier
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.secondaryContainer.copy(
                                                        alpha = 0.5f
                                                    )
                                                )
                                            )
                                        )
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.okaeri),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        style = MaterialTheme.typography.displayMedium,
                                        modifier = Modifier
                                            .padding(
                                                start = LocalPaddings.current.large,
                                                bottom = LocalPaddings.current.medium
                                            )
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
                                HomeRow(
                                    list = row.first.data.orEmpty(),
                                    title = row.second,
                                    onItemClicked = {
                                        navigator.navigate(
                                            MediaPageDestination(
                                                MediaPageArgs(
                                                    it.id,
                                                    homeMediaType.value.rawValue
                                                )
                                            )
                                        ) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                        },
                        contentModifier = Modifier.padding(
                            bottom = dimensionResource(coreR.dimen.navigation_bar_height)
                                    + LocalPaddings.current.large
                        ),
                        verticalArrangement = Arrangement.Top
                    )
                }
            }
        }

        else -> {
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
}

@Composable
fun HomeRow(
    list: List<Media.Medium>,
    title: String,
    onItemClicked: (Media.Medium) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = list.isNotEmpty(),
        enter = fadeIn(tween(delayMillis = 150)) + expandVertically(clip = false),
        exit = fadeOut(spring(stiffness = Spring.StiffnessHigh))
                + shrinkVertically(tween(durationMillis = 500)),
        label = "animate_media_list_enter_exit"
    ) {
        Column(
            modifier = modifier.padding(top = LocalPaddings.current.large),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = LocalPaddings.current.large)
                    .landscapeCutoutPadding()
            )

            MediaSmallRow(list) { media ->
                MediaSmall(
                    image = media.coverImage,
                    label = media.title,
                    onClick = { onItemClicked(media) },
                    modifier = Modifier.width(dimensionResource(R.dimen.media_card_width))
                )
            }
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
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
    ) {
        val offset by animateDpAsState(
            targetValue = if (selectedOption.value == MediaType.ANIME) 0.dp else 40.dp,
            label = "media_switch"
        )

        BlankBox(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.media_type_selector_padding))
                .size(dimensionResource(R.dimen.media_type_choice_size))
                .offset { IntOffset(x = offset.roundToPx(), y = 0) }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        )

        Row(
            modifier = Modifier
                .height(dimensionResource(R.dimen.media_type_selector_height))
                .width(dimensionResource(R.dimen.media_type_selector_width))
                .padding(dimensionResource(R.dimen.media_type_selector_padding)),
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
                    modifier = Modifier.requiredWidth(dimensionResource(R.dimen.media_type_choice_size))
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
