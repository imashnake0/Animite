package com.imashnake.animite.features.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imashnake.animite.api.anilist.MediaListQuery
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.dev.internal.Constants.CROSSFADE_DURATION
import com.imashnake.animite.R
import com.imashnake.animite.core.ui.LocalPaddings

/**
 * A [LazyRow] of [MediaSmall]s.
 *
 * @param mediaList A list of [T]s.
 */
@Composable
fun <T> MediaSmallRow(
    mediaList: List<T>,
    content: @Composable (T) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        contentPadding = PaddingValues(
            start = LocalPaddings.current.large + if (
                LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
            ) {
                WindowInsets.displayCutout.asPaddingValues().calculateLeftPadding(LayoutDirection.Ltr)
            } else 0.dp,
            end = LocalPaddings.current.large
        )
    ) {
        items(mediaList) { media ->
            content(media)
        }
    }
}

/**
 * A [Card] to display a media image and a label.
 *
 * @param image A URL of the image to be shown in the card that this component is.
 * @param label A label for the [image], if this is `null`, the [label] is not shown.
 * @param onClick Action to happen when the card is clicked.
 */
@Composable
fun MediaSmall(
    image: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.media_card_corner_radius))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(CROSSFADE_DURATION)
                .build(),
            contentDescription = label,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.media_card_corner_radius)))
        )

        if (label != null)
            Box {
                Text(
                    text = " \n ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    modifier = Modifier.padding(
                        vertical = dimensionResource(R.dimen.media_card_text_padding_vertical)
                    )
                )

                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    // TODO: Add a custom overflow indicator:
                    //  https://proandroiddev.com/detect-text-overflow-in-jetpack-compose-56c0b83da5a5.
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.media_card_text_padding_horizontal),
                            vertical = dimensionResource(R.dimen.media_card_text_padding_vertical)
                        )
                )
            }
    }
}

@Preview
@Composable
fun PreviewMediaSmallRow() {
    MediaSmallRow(
        mediaList = List(10) {
            MediaListQuery.Medium(
                id = it,
                type = MediaType.ANIME,
                title = MediaListQuery.Title(
                    romaji = "Sono Bisque Doll wa Koi wo Suru",
                    english = null,
                    native = null
                ),
                coverImage = MediaListQuery.CoverImage(
                    extraLarge =
                    "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx132405-qP7FQYGmNI3d.jpg",
                    large = null
                )
            )
        },
        content = { media ->
            MediaSmall(
                image = media.coverImage?.extraLarge,
                // TODO: Do something about this chain.
                label = media.title?.romaji ?:
                media.title?.english ?:
                media.title?.native.orEmpty(),
                onClick = { },
                modifier = Modifier.width(dimensionResource(R.dimen.media_card_width))
            )
        }
    )
}

@Preview
@Composable
fun PreviewMediaSmall() {
    MediaSmall(
        image =
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx132405-qP7FQYGmNI3d.jpg",
        label =
        "Sono Bisque Doll wa Koi wo Suru",
        onClick = {  },
        modifier = Modifier.width(dimensionResource(R.dimen.media_card_width))
    )
}
