package com.imashnake.animite.media

import android.content.Intent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.Constants
import com.imashnake.animite.core.extensions.addNewlineAfterParagraph
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.extensions.crossfadeModel
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.BottomSheet
import com.imashnake.animite.core.ui.CharacterCard
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.StatsRow
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.navigation.SharedContentKey.Component.Text
import kotlinx.serialization.Serializable
import com.imashnake.animite.core.R as coreR

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
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: MediaPageViewModel = hiltViewModel(),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val horizontalInsets = insetPaddingValues.horizontalOnly

    val scrollState = rememberScrollState()

    val media = viewModel.uiState

    var showDetailsSheet by remember { mutableStateOf(false) }
    val detailsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val characterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

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
                                title = media.title,
                                description = media.description.orEmpty(),
                                modifier = Modifier
                                    .skipToLookaheadSize()
                                    .padding(horizontal = LocalPaddings.current.large / 2)
                                    .padding(start = dimensionResource(coreR.dimen.media_card_width) + LocalPaddings.current.large)
                                    .padding(horizontalInsets)
                                    .height(
                                        dimensionResource(R.dimen.media_details_height) + LocalPaddings.current.medium / 2
                                    ),
                                onClick = { showDetailsSheet = true },
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
                                        .padding(horizontalInsets)
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
                                        horizontal = LocalPaddings.current.large
                                    ) + horizontalInsets,
                                    color = Color(media.color ?: 0xFF152232.toInt()),
                                )
                            }

                            if (!media.characters.isNullOrEmpty()) {
                                MediaCharacters(
                                    characters = media.characters,
                                    onCharacterClick = {
                                        viewModel.setSelectedCharacter(it)
                                    },
                                    contentPadding = PaddingValues(
                                        horizontal = LocalPaddings.current.large
                                    ) + horizontalInsets,
                                )
                            }

                            if (media.trailer != null) {
                                MediaTrailer(
                                    trailer = media.trailer,
                                    modifier = Modifier
                                        .padding(horizontal = LocalPaddings.current.large)
                                        .padding(horizontalInsets)
                                )
                            }
                        },
                        contentPadding = PaddingValues(
                            top = LocalPaddings.current.medium / 2,
                            bottom = LocalPaddings.current.large +
                                    insetPaddingValues.calculateBottomPadding()
                        )
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
                                        - dimensionResource(coreR.dimen.media_image_height)
                                        - insetPaddingValues.calculateTopPadding()
                                        + offset,
                                start = LocalPaddings.current.large,
                            )
                            .padding(horizontalInsets)
                            .height(dimensionResource(coreR.dimen.media_image_height) - offset)
                    ) {
                        MediaCard(
                            image = media.coverImage,
                            label = null,
                            onClick = {},
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

            if (showDetailsSheet) {
                BottomSheet(
                    sheetState = detailsSheetState,
                    onDismissRequest = { showDetailsSheet = false },
                ) {
                    Text(
                        text = media.title.orEmpty(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    MediaDescription(html = media.description.orEmpty())
                }
            }

            viewModel.uiState.selectedCharacter?.let {
                BottomSheet(
                    sheetState = characterSheetState,
                    onDismissRequest = { viewModel.setSelectedCharacter(null) },
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)) {
                        CharacterCard(
                            image = it.image,
                            label = null,
                            onClick = {},
                        )

                        Column(modifier = Modifier.height(dimensionResource(coreR.dimen.character_image_height))) {
                            Text(
                                text = it.name.orEmpty(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                            )

                            if (it.alternativeNames.isNotBlank()) {
                                MediaDescription(it.alternativeNames)
                            }
                        }
                    }

                    // TODO: Remove spoilers.
                    it.description?.let { description ->
                        MediaDescription(description.addNewlineAfterParagraph())
                    }
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
    title: String?,
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
            androidx.compose.animation.AnimatedVisibility(
                visible = title != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Text(
                    text = title.orEmpty(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = textModifier.align(Alignment.CenterStart),
                )
            }
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
        overflow = TextOverflow.Ellipsis
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
    onCharacterClick: (Media.Character) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    MediaSmallRow(
        title = stringResource(R.string.characters),
        mediaList = characters,
        modifier = modifier,
        contentPadding = contentPadding,
    ) { character ->
        CharacterCard(
            image = character.image,
            label = character.name,
            onClick = { onCharacterClick(character) },
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
                    .crossfade(Constants.CROSSFADE_DURATION)
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
    val mediaType: String,
    val title: String?,
)
