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
import androidx.compose.foundation.layout.displayCutoutPadding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imashnake.animite.core.extensions.given
import com.imashnake.animite.core.extensions.isNullOrZero
import com.imashnake.animite.dev.internal.Constants.CROSSFADE_DURATION
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination
import com.imashnake.animite.R as Res

@Destination(navArgsDelegate = MediaPageArgs::class)
@Composable
fun MediaPage(
    viewModel: MediaPageViewModel = hiltViewModel()
) {
    val media = viewModel.uiState

    Box {
        val scrollState = rememberScrollState()
        val bannerHeight = dimensionResource(Res.dimen.banner_height)
        // TODO: [Add shimmer](https://google.github.io/accompanist/placeholder/).
        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
        ) {
            if (!media.bannerImage.isNullOrEmpty()) {
                Box {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(media.bannerImage)
                            .crossfade(CROSSFADE_DURATION)
                            .build(),
                        contentDescription = null,
                        contentScale = if (
                            LocalConfiguration.current.orientation
                            != Configuration.ORIENTATION_LANDSCAPE
                        ) ContentScale.FillHeight else ContentScale.FillWidth,
                        modifier = Modifier
                            .given(
                                LocalConfiguration.current.orientation
                                        != Configuration.ORIENTATION_LANDSCAPE
                            ) {
                                height(bannerHeight)
                            }
                            .given(
                                LocalConfiguration.current.orientation
                                        == Configuration.ORIENTATION_LANDSCAPE
                            ) {
                                fillMaxWidth()
                            },
                        alignment = Alignment.Center
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .given(
                                LocalConfiguration.current.orientation
                                        != Configuration.ORIENTATION_LANDSCAPE
                            ) {
                                height(bannerHeight)
                            },
                        color = Color(media.color?.let { android.graphics.Color.parseColor(it) } ?: 0).copy(alpha = 0.25f)
                    ) { }
                }
            } else {
                Image(
                    painter = painterResource(Res.drawable.background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(Res.dimen.banner_height)),
                    alignment = Alignment.TopCenter
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = dimensionResource(Res.dimen.banner_height))
                    .background(MaterialTheme.colorScheme.background)
                    .given(
                        LocalConfiguration.current.orientation
                                == Configuration.ORIENTATION_LANDSCAPE
                    ) {
                        displayCutoutPadding()
                    }
            ) {
                Column {
                    Row {
                        Spacer(
                            Modifier.width(
                                dimensionResource(Res.dimen.media_card_width)
                                        + dimensionResource(Res.dimen.large_padding)
                            )
                        )

                        Column(
                            modifier = Modifier
                                .padding(
                                    start = dimensionResource(Res.dimen.large_padding),
                                    top = dimensionResource(Res.dimen.medium_padding),
                                    end = dimensionResource(Res.dimen.large_padding)
                                )
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
                        ) {
                            Text(
                                text = media.title.orEmpty(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )

                            Box {
                                Text(
                                    // TODO: Some attributes are not applied.
                                    text = Html
                                        .fromHtml(media.description, Html.FROM_HTML_MODE_COMPACT)
                                        .toString(),
                                    color = MaterialTheme.colorScheme.onBackground.copy(
                                        alpha = 0.6f
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .verticalScroll(rememberScrollState())
                                        .padding(top = dimensionResource(Res.dimen.small_padding))
                                )

                                Box(
                                    modifier = Modifier
                                        .height(dimensionResource(Res.dimen.small_padding))
                                        .fillMaxWidth()
                                        .align(Alignment.TopCenter)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.background,
                                                    Transparent
                                                )
                                            )
                                        )
                                ) { }

                                Box(
                                    modifier = Modifier
                                        .height(dimensionResource(Res.dimen.small_padding))
                                        .fillMaxWidth()
                                        .align(Alignment.BottomCenter)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(
                                                    Transparent,
                                                    MaterialTheme.colorScheme.background
                                                )
                                            )
                                        )
                                ) { }
                            }
                        }
                    }

                    if (!media.stats.all { it.score.isNullOrZero() }) {
                        Spacer(Modifier.height(dimensionResource(Res.dimen.large_padding)))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = dimensionResource(Res.dimen.large_padding),
                                    end = dimensionResource(Res.dimen.large_padding)
                                )
                        ) {
                            media.stats.forEach { stat ->
                                if (stat.score != null && stat.label != StatLabel.UNKNOWN)
                                    Stat(
                                        label = stat.label.value,
                                        score = stat.score
                                    ) {
                                        when(stat.label) {
                                            StatLabel.SCORE -> {
                                                "$it%"
                                            }
                                            StatLabel.RATING, StatLabel.POPULARITY -> {
                                                "#$it"
                                            }
                                            else -> { "" }
                                        }
                                    }
                            }
                        }
                    }

                    Spacer(Modifier.height(dimensionResource(Res.dimen.large_padding)))

                    // TODO: Monet where?
                    if (!media.genres.isNullOrEmpty()) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(
                                dimensionResource(Res.dimen.medium_padding)
                            ),
                            contentPadding = PaddingValues(
                                horizontal = dimensionResource(Res.dimen.large_padding)
                            )
                        ) {
                            items(media.genres) { genre ->
                                Genre(
                                    genre = genre,
                                    color = Color(media.color?.let { android.graphics.Color.parseColor(it) } ?: (0xFF152232).toInt()),
                                    // TODO: Make genres clickable.
                                    onClick = {  }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

                if (!media.characters.isNullOrEmpty()) {
                    Text(
                        text = stringResource(Res.string.characters),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            start = dimensionResource(Res.dimen.large_padding)
                        )
                    )

                    Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                    MediaSmallRow(
                        mediaList = media.characters
                    ) { character ->
                        MediaSmall(
                            image = character.image,
                            label = character.name,
                            onClick = { Log.d("CharacterId", "${character.id}") },
                            modifier = Modifier.width(dimensionResource(Res.dimen.character_card_width))
                        )
                    }
                }

                Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

                if (!(media.trailer.link.isNullOrEmpty() || media.trailer.thumbnail.isNullOrEmpty())) { // De Morgan's law
                    Text(
                        text = stringResource(Res.string.trailer),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            start = dimensionResource(Res.dimen.large_padding)
                        )
                    )

                    Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                    val context = LocalContext.current
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = dimensionResource(Res.dimen.large_padding))
                            .clip(
                                RoundedCornerShape(
                                    dimensionResource(Res.dimen.trailer_corner_radius)
                                )
                            )
                            .clickable {
                                val appIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(media.trailer.link))
                                context.startActivity(appIntent)
                            }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(media.trailer.thumbnail)
                                .crossfade(CROSSFADE_DURATION)
                                .build(),
                            contentDescription = stringResource(Res.string.trailer),
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.778f) // 16 : 9
                                .clip(
                                    RoundedCornerShape(
                                        dimensionResource(Res.dimen.trailer_corner_radius)
                                    )
                                ),
                            alignment = Alignment.Center
                        )

                        Image(
                            painter = painterResource(Res.drawable.youtube),
                            contentDescription = stringResource(Res.string.trailer),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))
            }

            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(
                        top = dimensionResource(Res.dimen.media_card_top_padding),
                        start = dimensionResource(Res.dimen.large_padding),
                        end = dimensionResource(Res.dimen.large_padding)
                    )
                    .given(
                        LocalConfiguration.current.orientation
                                == Configuration.ORIENTATION_LANDSCAPE
                    ) {
                        displayCutoutPadding()
                    }
            ) {
                MediaSmall(
                    image = media.coverImage,
                    label = null,
                    onClick = {},
                    modifier = Modifier.width(dimensionResource(Res.dimen.media_card_width))
                )
            }
        }

        // Translucent status bar.
        val bannerHeightPx = with(LocalDensity.current) { bannerHeight.toPx() }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = 0.75f * if (scrollState.value < bannerHeightPx) {
                        scrollState.value.toFloat() / bannerHeightPx
                    } else 1f
                }
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .height(
                    WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                )
                .align(Alignment.TopCenter)
        ) { }
    }
}

@Composable
fun Stat(label: String, score: Int, format: (Int) -> String) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = format(score),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Genre(genre: String?, color: Color, onClick: () -> Unit) {
    SuggestionChip(
        label = {
            Text(
                text = genre?.lowercase().orEmpty(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    vertical = dimensionResource(Res.dimen.small_padding)
                )
            )
        },
        onClick = onClick,
        shape = CircleShape,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color.copy(alpha = 0.25f)
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderColor = Transparent
        )
    )
}
