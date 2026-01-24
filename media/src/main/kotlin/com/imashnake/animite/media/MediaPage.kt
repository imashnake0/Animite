package com.imashnake.animite.media

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.Constants
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
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.layouts.banner.BannerLayout
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.media.ext.title
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.math.absoluteValue
import com.imashnake.animite.core.R as coreR

private const val RECOMMENDATIONS = "Recommendations"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress(
    "CognitiveComplexMethod",
    "LongMethod"
)
fun MediaPage(
    onBack: () -> Unit,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    deviceScreenCornerRadius: Int,
    useDarkTheme: Boolean,
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

    var showCharacterSheet by remember { mutableStateOf(false) }
    val characterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val characterPagerState = rememberPagerState(pageCount = { media.characters.orEmpty().size })
    val coroutineScope = rememberCoroutineScope()

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val deviceScreenCornerRadiusDp = with(LocalDensity.current) {
        deviceScreenCornerRadius.toDp()
    }

    var isList by remember { mutableStateOf(true) }

    MaterialTheme(colorScheme = rememberColorSchemeFor(media.color, useDarkTheme = useDarkTheme)) {
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
                                RoundedCornerShape(deviceScreenCornerRadiusDp)
                            ),
                        )
                        .clip(RoundedCornerShape(deviceScreenCornerRadiusDp))
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
                                textModifier = Modifier.skipToLookaheadSize(),
                                onClick = { showDetailsSheet = true },
                            )

                            if (media.nextEpisode != null && media.dayHoursToNextEpisode != null) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Chip(
                                        color = Color(0xFF80DF87),
                                        icon = ImageVector.vectorResource(R.drawable.hourglass),
                                        // TODO: Use string resources.
                                        text = "Episode ${media.nextEpisode} in ${media.dayHoursToNextEpisode}",
                                    )
                                }
                            }

                            MediaInfo(
                                info = media.info,
                                contentPadding = PaddingValues(
                                    horizontal = LocalPaddings.current.large
                                ) + horizontalInsets,
                            )

                            if (!media.ranks.isNullOrEmpty()) {
                                StatsRow(
                                    stats = media.ranks,
                                    modifier = Modifier
                                        .skipToLookaheadSize()
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
                                    onGenreClick = {
                                        isExpanded = true
                                        viewModel.getGenreMediaMediums(it)
                                    },
                                    contentPadding = PaddingValues(
                                        horizontal = LocalPaddings.current.large
                                    ) + horizontalInsets,
                                    color = Color(media.color ?: 0xFF152232.toInt()),
                                )
                            }

                            if (!media.characters.isNullOrEmpty()) {
                                MediaCharacters(
                                    characters = media.characters,
                                    onCharacterClick = { index, _ ->
                                        coroutineScope.launch {
                                            characterPagerState.scrollToPage(index)
                                        }
                                        showCharacterSheet = true
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
                                        .skipToLookaheadSize()
                                        .padding(horizontal = LocalPaddings.current.large)
                                        .padding(horizontalInsets)
                                )
                            }

                            if (!media.recommendations.isNullOrEmpty()) {
                                MediaRecommendations(
                                    recommendations = media.recommendations,
                                    onItemClicked = {
                                        onNavigateToMediaItem(
                                            MediaPage(
                                                id = it.id,
                                                source = RECOMMENDATIONS,
                                                mediaType = it.type.name,
                                                title = it.title,
                                            )
                                        )
                                    },
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    contentPadding = PaddingValues(
                                        horizontal = LocalPaddings.current.large
                                    ) + horizontalInsets,
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

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(insetPaddingValues)
                            .padding(
                                start = LocalPaddings.current.medium,
                                top = LocalPaddings.current.small
                            )
                            .clip(CircleShape)
                            .clickable(enabled = !isExpanded) { onBack() }
                            .padding(LocalPaddings.current.small),
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                }
            }

            if (showDetailsSheet) {
                BottomSheet(
                    sheetState = detailsSheetState,
                    onDismissRequest = { showDetailsSheet = false },
                    deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp,
                ) { paddingValues, modifier ->
                    Column(modifier) {
                        Text(
                            text = media.title.orEmpty(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                .padding(paddingValues)
                                .padding(vertical = LocalPaddings.current.medium)
                        )

                        MediaDescription(
                            html = media.description.orEmpty(),
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(top = LocalPaddings.current.medium)
                        )
                    }
                }
            }

            if (showCharacterSheet) {
                BottomSheet(
                    sheetState = characterSheetState,
                    dragHandleBackgroundColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    onDismissRequest = { showCharacterSheet = false },
                    deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp
                ) { paddingValues, modifier ->
                    HorizontalPager(state = characterPagerState) { page ->
                        Column(modifier = modifier) {
                            val currentCharacter = media.characters.orEmpty()[page]
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    .padding(paddingValues)
                                    .padding(bottom = LocalPaddings.current.large)
                                    .graphicsLayer {
                                        val pageOffset = (
                                            characterPagerState.currentPage - page + characterPagerState.currentPageOffsetFraction
                                        ).absoluteValue

                                        alpha = lerp(
                                            start = 0f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                        scaleY = lerp(
                                            start = 0.9f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                        scaleX = lerp(
                                            start = 0.9f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                    }
                            ) {
                                CharacterCard(
                                    image = currentCharacter.image,
                                    label = null,
                                    onClick = {},
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                                    modifier = Modifier.height(dimensionResource(coreR.dimen.character_image_height))
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)) {
                                        Text(
                                            text = currentCharacter.name.orEmpty(),
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.titleLarge,
                                        )

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                LocalPaddings.current.small
                                            )
                                        ) {
                                            currentCharacter.dob?.let { dob ->
                                                Chip(
                                                    color = Color(0xFF80DF87),
                                                    text = dob,
                                                    iconPadding = PaddingValues(bottom = 2.dp),
                                                    icon = ImageVector.vectorResource(R.drawable.cake),
                                                )
                                            }

                                            currentCharacter.favourites?.let { fav ->
                                                Chip(
                                                    color = Color(0xFFFF9999),
                                                    text = fav,
                                                    icon = Icons.Rounded.Favorite
                                                )
                                            }
                                        }
                                    }

                                    if (currentCharacter.alternativeNames.isNotBlank()) {
                                        MediaDescription(currentCharacter.alternativeNames)
                                    }
                                }
                            }

                            // TODO: Remove spoilers.
                            currentCharacter.description?.let { description ->
                                MediaDescription(
                                    html = description,
                                    onLinkClick = onLinkClick@{
                                        val id = it?.split("/")?.getOrNull(4)?.toIntOrNull() ?: return@onLinkClick null
                                        val character = media.characters?.find { character -> character.id == id } ?: return@onLinkClick null
                                        val index = media.characters.indexOf(character)
                                        if (index != -1) {
                                            coroutineScope.launch {
                                                characterPagerState.animateScrollToPage(index)
                                            }
                                        } else return@onLinkClick null
                                        return@onLinkClick Unit
                                    },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                        .padding(paddingValues)
                                        .padding(top = LocalPaddings.current.medium)
                                        .graphicsLayer {
                                            val pageOffset = (
                                                characterPagerState.currentPage - page + characterPagerState.currentPageOffsetFraction
                                            ).absoluteValue

                                            alpha = lerp(
                                                start = 0f,
                                                stop = 1f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                            scaleY = lerp(
                                                start = 0.9f,
                                                stop = 1f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                            scaleX = lerp(
                                                start = 0.9f,
                                                stop = 1f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                            translationY = lerp(
                                                start = size.height * -0.025f,
                                                stop = 0f,
                                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }

            val frontDropColor by animateColorAsState(
                targetValue = MaterialTheme.colorScheme.background.copy(
                    alpha = if (isExpanded) 0.95f else 0f
                ),
                animationSpec = tween(Constants.CROSSFADE_DURATION),
                label = "show_front_drop"
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind { drawRect(frontDropColor) }
            )

            // TODO: Add progress indicator.
            AnimatedVisibility(
                visible = media.genreTitleList?.second?.isNotEmpty() ?: false,
                enter = fadeIn(tween(750)),
                exit = fadeOut(tween(750)),
            ) {
                Column {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .padding(insetPaddingValues)
                            .padding(horizontal = LocalPaddings.current.large)
                            .padding(top = LocalPaddings.current.large)
                    ) {
                        Text(
                            text = media.genreTitleList?.first.orEmpty(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            val listToGrid =
                                AnimatedImageVector.animatedVectorResource(R.drawable.grid_list_anim)
                            Icon(
                                painter = rememberAnimatedVectorPainter(listToGrid, isList),
                                contentDescription = stringResource(R.string.list_to_grid),
                                modifier = Modifier
                                    .padding(end = LocalPaddings.current.small)
                                    .clip(CircleShape)
                                    .clickable { isList = !isList }
                                    .size(40.dp)
                                    .padding(LocalPaddings.current.small)
                                    .zIndex(2f)
                            )

                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(R.string.back),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        viewModel.getGenreMediaMediums(null)
                                        isExpanded = false
                                    }
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(LocalPaddings.current.small)
                                    .zIndex(2f)
                            )
                        }
                    }

                    // TODO: This list <-> grid pattern can probably be added to all the other
                    //  instances of media lists in a better way.
                    AnimatedContent(
                        targetState = isList,
                        transitionSpec = {
                            fadeIn(tween(750)).togetherWith(fadeOut(tween(750)))
                        },
                        modifier = Modifier.padding(horizontalInsets)
                    ) {
                        if (it) {
                            MediaMediumGrid(
                                mediaMediumList = media.genreTitleList?.second.orEmpty(),
                                onItemClick = { id, title ->
                                    onNavigateToMediaItem(
                                        MediaPage(
                                            id = id,
                                            source = MediaList.Type.GENRE_LIST.name,
                                            mediaType = media.type ?: MediaType.UNKNOWN__.rawValue,
                                            title = title
                                        )
                                    )
                                }
                            )
                        } else {
                            MediaMediumList(
                                mediaMediumList = media.genreTitleList?.second.orEmpty(),
                                onItemClick = { id, title ->
                                    onNavigateToMediaItem(
                                        MediaPage(
                                            id = id,
                                            source = MediaList.Type.GENRE_LIST.name,
                                            mediaType = media.type ?: MediaType.UNKNOWN__.rawValue,
                                            title = title
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    BackHandler(!media.genreTitleList?.second.isNullOrEmpty()) {
        viewModel.getGenreMediaMediums(null)
        isExpanded = false
    }
}

@Composable
private fun MediaBanner(
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
private fun MediaDetails(
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
private fun MediaDescription(
    html: String,
    modifier: Modifier = Modifier,
    onLinkClick: ((String?) -> Unit?)? = null,
) {
    val uriHandler = LocalUriHandler.current
    Text(
        text = AnnotatedString.fromHtml(
            htmlString = html,
            linkInteractionListener = LinkInteractionListener { link ->
                val url = (link as? LinkAnnotation.Url)?.url
                if (onLinkClick == null || onLinkClick(url) == null) {
                    url?.let { uriHandler.openUri(it) }
                } else {
                    onLinkClick(url)
                }
            }
        ),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MediaInfo(
    info: List<Media.Info>?,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val infiniteTransition = rememberInfiniteTransition(label = "divider")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(contentPadding)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(LocalPaddings.current.large),
            )
            .padding(horizontal = LocalPaddings.current.medium)
    ) {
        info?.fastForEach {
            when (it) {
                is Media.Info.Divider -> {
                    Box(
                        modifier = Modifier
                            .graphicsLayer { rotationZ = angle }
                            .padding(LocalPaddings.current.small)
                            .size(6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                                shape = MaterialShapes.Cookie4Sided.toShape()
                            )
                    )
                }
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                        modifier = Modifier
                            .clip(RoundedCornerShape(LocalPaddings.current.large))
                            .clickable {}
                            .padding(
                                vertical = LocalPaddings.current.medium,
                                horizontal = LocalPaddings.current.large / 2,
                            )
                    ) {
                        Text(
                            text = stringResource(it.item.title!!),
                            style = MaterialTheme.typography.labelSmallEmphasized
                        )
                        Text(
                            text = when(it) {
                                is Media.Info.Item -> it.value
                                is Media.Info.Season -> listOfNotNull(
                                    stringResource(it.season.res), it.year
                                ).joinToString(" ")
                                else -> stringResource(
                                    when(it) {
                                        is Media.Info.Format -> it.format.res
                                        is Media.Info.Status -> it.status.res
                                        is Media.Info.Source -> it.source.res
                                    }
                                )
                            },
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                            style = MaterialTheme.typography.labelSmallEmphasized
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaGenres(
    genres: List<String>,
    onGenreClick: (String) -> Unit,
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
                onClick = { onGenreClick(genre) },
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
private fun MediaCharacters(
    characters: List<Media.Character>,
    onCharacterClick: (Int, Media.Character) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    MediaSmallRow(
        title = stringResource(R.string.characters),
        mediaList = characters,
        modifier = modifier,
        contentPadding = contentPadding,
    ) { index, character ->
        CharacterCard(
            image = character.image,
            label = character.name,
            onClick = { onCharacterClick(index, character) },
        )
    }
}

@Composable
private fun Chip(
    color: Color,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconPadding: PaddingValues = PaddingValues(),
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = LocalPaddings.current.small)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .padding(iconPadding)
                .size(15.dp)
        )
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MediaTrailer(
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

@Composable
fun MediaRecommendations(
    recommendations: List<Media.Small>,
    onItemClicked: (Media.Small) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    MediaSmallRow(
        title = stringResource(R.string.recommendations),
        mediaList = recommendations,
        modifier = modifier,
        contentPadding = contentPadding,
    ) { _, media ->
        with(sharedTransitionScope) {
            MediaCard(
                image = media.coverImage,
                label = media.title,
                onClick = { onItemClicked(media) },
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = stringResource(R.string.recommendations),
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
                            source = stringResource(R.string.recommendations),
                            sharedComponents = Image to Image,
                        )
                    ),
                    animatedVisibilityScope,
                ),
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
