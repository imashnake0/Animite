package com.imashnake.animite.features.media

import android.content.res.Configuration
import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.core.extensions.given
import com.imashnake.animite.features.ui.MediaSmall
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
            MediaBanner(
                imageUrl = media.bannerImage,
                tintColor = Color(media.color ?: 0).copy(alpha = 0.25f),
                modifier = Modifier
                    .height(bannerHeight)
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = bannerHeight, bottom = dimensionResource(Res.dimen.large_padding))
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

                if (media.stats != null) {
                    MediaStats(
                        stats = media.stats,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(Res.dimen.large_padding))
                    )
                }

                if (media.genres != null) {
                    MediaGenres(
                        genres = media.genres,
                        contentPadding = PaddingValues(
                            horizontal = dimensionResource(Res.dimen.large_padding)
                        ),
                        color = Color(media.color ?: (0xFF152232).toInt()),
                    )
                }

                if (media.characters != null) {
                    MediaCharacters(
                        characters = media.characters,
                        contentPadding = PaddingValues(horizontal = dimensionResource(Res.dimen.large_padding))
                    )
                }

                if (media.trailer != null) {
                    MediaTrailer(
                        trailer = media.trailer,
                        modifier = Modifier.padding(horizontal = dimensionResource(Res.dimen.large_padding))
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
