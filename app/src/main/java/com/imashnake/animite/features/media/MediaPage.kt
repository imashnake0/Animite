package com.imashnake.animite.features.media

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.text.Html
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imashnake.animite.R
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.ui.ScrollableText
import com.imashnake.animite.core.ui.TranslucentStatusBarLayout
import com.imashnake.animite.dev.internal.Constants
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.ramcosta.composedestinations.annotation.Destination
import com.imashnake.animite.R as Res

@Destination(navArgsDelegate = MediaPageArgs::class)
@Composable
fun MediaPage(
    viewModel: MediaPageViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val bannerHeight = dimensionResource(Res.dimen.banner_height)

    val media = viewModel.uiState

    // TODO: [Add shimmer](https://google.github.io/accompanist/placeholder/).
    TranslucentStatusBarLayout(scrollState = scrollState, distanceUntilAnimated = bannerHeight) {
        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
        ) {
            MediaBanner(
                imageUrl = media.bannerImage,
                tintColor = Color(media.color ?: 0).copy(alpha = 0.25f),
                modifier = Modifier
                    .height(bannerHeight)
                    .fillMaxWidth()
                    .bannerParallax(scrollState)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = bannerHeight,
                        bottom = dimensionResource(Res.dimen.large_padding)
                    )
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(Res.dimen.large_padding))
            ) {
                MediaDetails(
                    title = media.title.orEmpty(),
                    description = Html
                        .fromHtml(media.description.orEmpty(), Html.FROM_HTML_MODE_COMPACT)
                        .toString(),
                    // TODO Can we do something about this Modifier chain?
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(Res.dimen.large_padding)
                                    + dimensionResource(Res.dimen.media_card_width)
                                    + dimensionResource(Res.dimen.large_padding),
                            top = dimensionResource(Res.dimen.medium_padding),
                            end = dimensionResource(Res.dimen.large_padding)
                        )
                        .landscapeCutoutPadding()
                        .height(
                            WindowInsets.statusBars
                                .asPaddingValues()
                                .calculateTopPadding()
                                    + dimensionResource(Res.dimen.media_card_top_padding)
                                    + dimensionResource(Res.dimen.media_card_height)
                                    - dimensionResource(Res.dimen.banner_height)
                                    - dimensionResource(Res.dimen.medium_padding)
                        )
                        .fillMaxSize()
                )

                if (!media.stats.isNullOrEmpty()) {
                    MediaStats(
                        stats = media.stats,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(Res.dimen.large_padding))
                            .landscapeCutoutPadding()
                    )
                }

                if (media.genres != null) {
                    MediaGenres(
                        genres = media.genres,
                        contentPadding = PaddingValues(
                            start = dimensionResource(Res.dimen.large_padding) + if (
                                LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
                            ) {
                                WindowInsets.displayCutout.asPaddingValues()
                                    .calculateLeftPadding(LayoutDirection.Ltr)
                            } else 0.dp,
                            end = dimensionResource(Res.dimen.large_padding)
                        ),
                        color = Color(media.color ?: (0xFF152232).toInt()),
                    )
                }

                if (!media.characters.isNullOrEmpty()) {
                    MediaCharacters(
                        characters = media.characters,
                        contentPadding = PaddingValues(horizontal = dimensionResource(Res.dimen.large_padding))
                    )
                }

                if (media.trailer != null) {
                    MediaTrailer(
                        trailer = media.trailer,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(Res.dimen.large_padding))
                            .landscapeCutoutPadding()
                    )
                }
            }

            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(
                        top = dimensionResource(Res.dimen.media_card_top_padding),
                        start = dimensionResource(Res.dimen.large_padding),
                        end = dimensionResource(Res.dimen.large_padding)
                    )
                    .landscapeCutoutPadding()
            ) {
                MediaSmall(
                    image = media.coverImage,
                    label = null,
                    onClick = {},
                    modifier = Modifier.width(dimensionResource(Res.dimen.media_card_width))
                )
            }
        }
    }
}

@Composable
fun MediaBanner(
    imageUrl: String?,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    if (!imageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(Constants.CROSSFADE_DURATION)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(
                color = tintColor,
                blendMode = BlendMode.SrcAtop
            )
        )
    } else {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
            alignment = Alignment.TopCenter,
            colorFilter = ColorFilter.tint(
                color = tintColor,
                blendMode = BlendMode.SrcAtop
            )
        )
    }
}

@Composable
fun MediaDetails(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        ScrollableText(text = description)
    }
}

@Composable
fun MediaStats(
    stats: List<Stat>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        stats.forEach { stat ->
            if (stat.label != StatLabel.UNKNOWN) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stat.label.value,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall
                    )

                    Text(
                        text = when (stat.label) {
                            StatLabel.SCORE -> "${stat.score}%"
                            StatLabel.RATING, StatLabel.POPULARITY -> "#${stat.score}"
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaGenres(
    genres: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    color: Color = MaterialTheme.colorScheme.primaryContainer
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.medium_padding)
        ),
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
                            vertical = dimensionResource(R.dimen.small_padding)
                        )
                    )
                },
                onClick = { },
                shape = CircleShape,
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = color.copy(alpha = 0.25f)
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    borderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun MediaCharacters(
    characters: List<Character>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.characters),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(contentPadding)
                .landscapeCutoutPadding()
        )

        Spacer(Modifier.size(dimensionResource(R.dimen.medium_padding)))

        MediaSmallRow(
            mediaList = characters
        ) { character ->
            MediaSmall(
                image = character.image,
                label = character.name,
                onClick = { Log.d("CharacterId", "${character.id}") },
                modifier = Modifier.width(dimensionResource(R.dimen.character_card_width))
            )
        }
    }
}

@Composable
fun MediaTrailer(
    trailer: Trailer,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.trailer),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.size(dimensionResource(R.dimen.medium_padding)))

        val context = LocalContext.current
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.trailer_corner_radius)))
                .clickable {
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.link))
                    context.startActivity(appIntent)
                }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(trailer.thumbnail)
                    .crossfade(Constants.CROSSFADE_DURATION)
                    .build(),
                contentDescription = stringResource(R.string.trailer),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.778f) // 16 : 9
                    .clip(
                        RoundedCornerShape(dimensionResource(R.dimen.trailer_corner_radius))
                    ),
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
