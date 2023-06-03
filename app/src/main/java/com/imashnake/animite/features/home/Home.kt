package com.imashnake.animite.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.ui.ProgressIndicator
import com.imashnake.animite.core.ui.TranslucentStatusBarLayout
import com.imashnake.animite.data.Resource
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.media.MediaPageArgs
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.imashnake.animite.R as Res

@Destination
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val homeMediaType = remember { mutableStateOf(MediaType.ANIME) }

    viewModel.setMediaType(homeMediaType.value)

    val trendingList by viewModel.trendingMedia.collectAsState()
    val popularList by viewModel.popularMediaThisSeason.collectAsState()
    val upcomingList by viewModel.upcomingMediaNextSeason.collectAsState()
    val allTimePopularList by viewModel.allTimePopular.collectAsState()

    // TODO: [Code Smells: If Statements](https://dzone.com/articles/code-smells-if-statements).
    when {
        trendingList is Resource.Success &&
        popularList is Resource.Success &&
        upcomingList is Resource.Success &&
        allTimePopularList is Resource.Success -> {
            val scrollState = rememberScrollState()
            TranslucentStatusBarLayout(
                scrollState = scrollState,
                distanceUntilAnimated = dimensionResource(Res.dimen.banner_height)
            ) {
                Box(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .navigationBarsPadding()
                ) {
                    Box {
                        Image(
                            painter = painterResource(Res.drawable.background),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(Res.dimen.banner_height))
                                .bannerParallax(scrollState),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )

                        Box(
                            modifier = Modifier
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
                                .fillMaxWidth()
                                .height(dimensionResource(Res.dimen.banner_height))
                        ) { }

                        Text(
                            text = stringResource(Res.string.okaeri),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(
                                    start = dimensionResource(Res.dimen.large_padding),
                                    bottom = dimensionResource(Res.dimen.medium_padding)
                                )
                                .landscapeCutoutPadding()
                        )

                        MediaTypeSelector(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(
                                    end = dimensionResource(Res.dimen.large_padding),
                                    bottom = dimensionResource(Res.dimen.medium_padding)
                                )
                                .landscapeCutoutPadding(),
                            selectedOption = homeMediaType,
                            viewModel = viewModel
                        )
                    }

                    Column {
                        Spacer(Modifier.size(dimensionResource(Res.dimen.banner_height)))

                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = dimensionResource(Res.dimen.large_padding))
                                // TODO: Move this one out of Home when we can pass modifiers in.
                                .padding(bottom = dimensionResource(Res.dimen.navigation_bar_height)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(Res.dimen.large_padding))
                        ) {
                            HomeRow(
                                list = trendingList.data.orEmpty(),
                                title = stringResource(Res.string.trending_now),
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

                            HomeRow(
                                list = popularList.data.orEmpty(),
                                title = stringResource(Res.string.popular_this_season),
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

                            HomeRow(
                                list = upcomingList.data.orEmpty(),
                                title = stringResource(Res.string.upcoming_next_season),
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

                            HomeRow(
                                list = allTimePopularList.data.orEmpty(),
                                title = stringResource(Res.string.all_time_popular),
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
                    }
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
    if (list.isNotEmpty()) {
        Column(modifier) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = dimensionResource(Res.dimen.large_padding))
                    .landscapeCutoutPadding()
            )

            Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

            MediaSmallRow(
                mediaList = list,
                content = { media ->
                    MediaSmall(
                        image = media.coverImage,
                        label = media.title,
                        onClick = { onItemClicked(media) },
                        modifier = Modifier.width(dimensionResource(Res.dimen.media_card_width))
                    )
                }
            )
        }
    }
}

@Composable
private fun MediaTypeSelector(
    modifier: Modifier = Modifier,
    selectedOption: MutableState<MediaType>,
    viewModel: HomeViewModel
) {
    Row(
        modifier = modifier
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(dimensionResource(id = Res.dimen.media_type_selector_round))
            )
            .padding(dimensionResource(id = Res.dimen.small_padding))
    ) {
        Icon(
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = Res.dimen.media_type_selector_size))
                .clickable {
                    if (selectedOption.value != MediaType.ANIME) {
                        viewModel.setMediaType(MediaType.ANIME)
                    }
                    selectedOption.value = MediaType.ANIME
                }
                .background(
                    color = if (selectedOption.value == MediaType.ANIME) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(dimensionResource(id = Res.dimen.media_type_selector_round))
                )
                .padding(dimensionResource(id = Res.dimen.small_padding)),
            tint = if (selectedOption.value == MediaType.ANIME) {
                Color.White
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = Res.dimen.tiny_padding)))
        Icon(
            imageVector = ImageVector.vectorResource(id = Res.drawable.manga),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = Res.dimen.media_type_selector_size))
                .clickable {
                    if (selectedOption.value != MediaType.MANGA) {
                        viewModel.setMediaType(MediaType.MANGA)
                    }
                    selectedOption.value = MediaType.MANGA
                }
                .background(
                    color = if (selectedOption.value == MediaType.MANGA) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(dimensionResource(id = Res.dimen.media_type_selector_round))
                )
                .padding(dimensionResource(id = Res.dimen.small_padding)),
            tint = if (selectedOption.value == MediaType.MANGA) {
                Color.White
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}
