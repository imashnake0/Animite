package com.imashnake.animite.media

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.Constants
import com.imashnake.animite.components.extensions.bannerParallax
import com.imashnake.animite.components.extensions.crossfadeModel
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.navigation.SharedContentKey.Component.Text
import kotlinx.serialization.Serializable
import com.imashnake.animite.core.R as coreR
import androidx.core.net.toUri
import com.imashnake.animite.components.LocalPaddings
import com.imashnake.animite.components.NestedScrollableContent
import com.imashnake.animite.components.StatsRow
import com.imashnake.animite.components.extensions.CROSSFADE_DURATION
import com.imashnake.animite.components.layout.BannerLayout
import com.imashnake.animite.components.layout.TranslucentStatusBarLayout
import com.imashnake.animite.components.media.MediaSmall
import com.imashnake.animite.components.media.MediaSmallRow

// TODO: Need to use WindowInsets to get device corner radius if available.
private const val DEVICE_CORNER_RADIUS = 30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress(
    "CognitiveComplexMethod",
    "LongMethod"
)
fun MediaPage(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: MediaPageViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()

    val media = viewModel.uiState

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    MaterialTheme(colorScheme = rememberColorSchemeFor(media.color)) {
        TranslucentStatusBarLayout(
            scrollState = scrollState,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            with(sharedTransitionScope) {
                Box(
                    Modifier
                        .sharedBounds(
                            rememberSharedContentState(
                                SharedContentKey(
                                    id = media.id,
                                    source = media.source,
                                    sharedComponents = Card to Page,
                                )
                            ),
                            animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            clipInOverlayDuringTransition = OverlayClip(
                                RoundedCornerShape(DEVICE_CORNER_RADIUS.dp)
                            ),
                        )
                        .clip(RoundedCornerShape(DEVICE_CORNER_RADIUS.dp))
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = LocalPaddings.current.large)
                ) {
                    BannerLayout(
                        banner = { bannerModifier ->
                            MediaBanner(
                                imageUrl = media.bannerImage,
                                color = Color(media.color ?: 0).copy(alpha = 0.5f),
                                modifier = bannerModifier.bannerParallax(scrollState)
                            )
                        },
                        content = {
                            MediaDetails(
                                title = media.title.orEmpty(),
                                description = media.description.orEmpty(),
                                modifier = Modifier
                                    .skipToLookaheadSize()
                                    .padding(
                                        start = LocalPaddings.current.large / 2
                                                + dimensionResource(coreR.dimen.media_card_width)
                                                + LocalPaddings.current.large,
                                        end = LocalPaddings.current.large / 2
                                    )
                                    .height(
                                        dimensionResource(R.dimen.media_details_height) + LocalPaddings.current.medium / 2
                                    ),
                                onClick = { showSheet = true },
                                textModifier = Modifier.sharedBounds(
                                    rememberSharedContentState(
                                        SharedContentKey(
                                            id = media.id,
                                            source = media.source,
                                            sharedComponents = Text to Text,
                                        )
                                    ),
                                    animatedVisibilityScope,
                                ),
                            )

                            if (!media.ranks.isNullOrEmpty()) {
                                StatsRow(
                                    stats = media.ranks,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = LocalPaddings.current.large)
                                ) {
                                    Text(
                                        text = it.type.name,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.labelSmall
                                    )

                                    Text(
                                        text = when (it.type) {
                                            Media.Ranking.Type.SCORE -> "${it.rank}%"
                                            Media.Ranking.Type.RATED,
                                            Media.Ranking.Type.POPULAR -> "#${it.rank}"
                                        },
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                }
                            }

                            if (!media.genres.isNullOrEmpty()) {
                                MediaGenres(
                                    genres = media.genres,
                                    contentPadding = PaddingValues(
                                        start = LocalPaddings.current.large + if (
                                            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
                                        ) {
                                            WindowInsets.displayCutout.asPaddingValues()
                                                .calculateLeftPadding(LayoutDirection.Ltr)
                                        } else 0.dp,
                                        end = LocalPaddings.current.large
                                    ),
                                    color = Color(media.color ?: 0xFF152232.toInt()),
                                )
                            }

                            if (!media.characters.isNullOrEmpty()) {
                                MediaCharacters(
                                    characters = media.characters,
                                )
                            }

                            if (media.trailer != null) {
                                MediaTrailer(
                                    trailer = media.trailer,
                                    modifier = Modifier
                                        .padding(horizontal = LocalPaddings.current.large)
                                )
                            }
                        },
                        contentModifier = Modifier.padding(top = LocalPaddings.current.medium / 2)
                    )

                    // TODO: https://developer.android.com/jetpack/compose/animation/quick-guide#concurrent-animations
                    val offset by animateDpAsState(
                        targetValue = if (scrollState.value == 0)
                            0.dp
                        else
                            dimensionResource(coreR.dimen.media_image_height) - dimensionResource(R.dimen.media_details_height),
                        animationSpec = tween(durationMillis = 750),
                        label = "media_card_height"
                    )

                    Box(
                        modifier = Modifier
                            .statusBarsPadding()
                            // TODO: Try using `AlignmentLine`s.
                            .padding(
                                top = dimensionResource(R.dimen.media_details_height)
                                        + LocalPaddings.current.medium
                                        + dimensionResource(coreR.dimen.banner_height)
                                        - WindowInsets.statusBars
                                    .asPaddingValues()
                                    .calculateTopPadding()
                                        - dimensionResource(coreR.dimen.media_image_height)
                                        + offset,
                                start = LocalPaddings.current.large
                            )
                            .height(dimensionResource(coreR.dimen.media_image_height) - offset)
                    ) {
                        MediaSmall(
                            image = media.coverImage,
                            onClick = {},
                            imageHeight = dimensionResource(coreR.dimen.media_image_height),
                            cardWidth = dimensionResource(coreR.dimen.media_card_width),
                            modifier = Modifier.sharedBounds(
                                rememberSharedContentState(
                                    SharedContentKey(
                                        id = media.id,
                                        source = media.source,
                                        sharedComponents = Image to Image,
                                    )
                                ),
                                animatedVisibilityScope,
                            )
                        )
                    }
                }
            }

            if (showSheet)
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = LocalPaddings.current.medium),
                    sheetState = sheetState,
                    onDismissRequest = { showSheet = false },
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = LocalPaddings.current.large),
                        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
                    ) {
                        Text(
                            text = media.title.orEmpty(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        MediaDescription(
                            html = media.description.orEmpty(),
                        )
                    }
                }
        }
    }
}

@Composable
fun MediaBanner(
    imageUrl: String?,
    color: Color,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = crossfadeModel(imageUrl),
        contentDescription = null,
        placeholder = ColorPainter(color),
        error = painterResource(R.drawable.background),
        fallback = painterResource(R.drawable.background),
        contentScale = ContentScale.Crop,
        modifier = modifier,
        alignment = Alignment.Center,
        colorFilter = ColorFilter.tint(
            color = color.copy(alpha = 0.25f),
            blendMode = BlendMode.SrcAtop
        )
    )
}

@Composable
fun MediaDetails(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(LocalPaddings.current.small))
            .clickable { onClick() }
            .padding(horizontal = LocalPaddings.current.large / 2)
            .padding(top = LocalPaddings.current.medium / 2)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = textModifier.align(Alignment.CenterStart),
            )
        }

        NestedScrollableContent { contentModifier ->
            MediaDescription(description, contentModifier)
        }
    }
}

@Composable
fun MediaDescription(
    html: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = AnnotatedString.fromHtml(html),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
    )
}

@Composable
fun MediaGenres(
    genres: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    color: Color = MaterialTheme.colorScheme.primaryContainer
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(genres) { genre ->
            SuggestionChip(
                label = {
                    Text(
                        text = genre.lowercase(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            vertical = LocalPaddings.current.small
                        )
                    )
                },
                onClick = { },
                shape = CircleShape,
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = color.copy(alpha = 0.25f)
                ),
                border = BorderStroke(width = 0.dp, color = Color.Transparent)
            )
        }
    }
}

@Composable
fun MediaCharacters(
    characters: List<Media.Character>,
    modifier: Modifier = Modifier,
) {
    MediaSmallRow(
        title = stringResource(R.string.characters),
        mediaList = characters,
        modifier = modifier
    ) { character ->
        MediaSmall(
            image = character.image,
            label = character.name,
            onClick = { Log.d("CharacterId", "${character.id}") },
            imageHeight = dimensionResource(R.dimen.character_image_height),
            cardWidth = dimensionResource(R.dimen.character_card_width),
        )
    }
}

@Composable
fun MediaTrailer(
    trailer: Media.Trailer,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
    ) {
        Text(
            text = stringResource(R.string.trailer),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        val context = LocalContext.current
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.trailer_corner_radius)))
                .background(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
                .clickable {
                    val appIntent = Intent(Intent.ACTION_VIEW, trailer.url?.toUri())
                    context.startActivity(appIntent)
                }
        ) {
            var bestThumbnail by rememberSaveable { mutableStateOf(trailer.thumbnail.maxResDefault) }

            val model = remember(bestThumbnail) {
                ImageRequest.Builder(context)
                    .data(bestThumbnail)
                    .apply {
                        listener(
                            onError = { _, _ ->
                                bestThumbnail = if (bestThumbnail?.contains("maxresdefault") == true) {
                                    trailer.thumbnail.sdDefault
                                } else trailer.thumbnail.defaultThumbnail
                            }
                        )
                    }
                    .crossfade(CROSSFADE_DURATION)
                    .build()
            }

            AsyncImage(
                model = model,
                contentDescription = stringResource(R.string.trailer),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9)
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.trailer_corner_radius))),
                alignment = Alignment.Center
            )

            Image(
                painter = painterResource(R.drawable.youtube),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Serializable
data class MediaPage(
    val id: Int,
    val source: String,
    val mediaType: String
)
