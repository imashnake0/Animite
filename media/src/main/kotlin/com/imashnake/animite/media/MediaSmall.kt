package com.imashnake.animite.media

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import core.extensions.crossfadeModel
import core.extensions.landscapeCutoutPadding
import core.ui.LocalPaddings

/**
 * A [LazyRow] of [MediaSmall]s.
 *
 * @param mediaList A list of [T]s.
 */
@Composable
fun <T> MediaSmallRow(
    title: String?,
    mediaList: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
    ) {
        // TODO: Does this behave as expected if `title` is null?
        if (title != null) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = LocalPaddings.current.large)
                    .landscapeCutoutPadding()
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            contentPadding = PaddingValues(
                start = LocalPaddings.current.large + if (
                    LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
                ) {
                    WindowInsets.displayCutout.asPaddingValues()
                        .calculateLeftPadding(LayoutDirection.Ltr)
                } else 0.dp,
                end = LocalPaddings.current.large
            )
        ) {
            items(mediaList) { media ->
                content(media)
            }
        }
    }
}

/**
 * A [Card] to display a media image and a label. Note that [imageHeight] and [cardWidth] must be
 * set so that all cards have the same dimensions.
 *
 * @param image A URL of the image to be shown in the card that this component is.
 * @param label A label for the [image], if this is `null`, the [label] is not shown.
 * @param onClick Action to happen when the card is clicked.
 * @param imageHeight Fixed height of the images in the card.
 * @param cardWidth Width of the card.
 */
@Composable
fun MediaSmall(
    image: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageHeight: Dp,
    cardWidth: Dp,
    imageModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    label: String? = null,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.media_card_corner_radius)),
        modifier = modifier.width(cardWidth),
    ) {
        AsyncImage(
            model = crossfadeModel(image),
            contentDescription = label,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
                .height(imageHeight)
                .fillMaxWidth()
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

                Box(
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.media_card_text_padding_horizontal),
                            vertical = dimensionResource(R.dimen.media_card_text_padding_vertical)
                        )
                ) {
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 2,
                        // TODO: Add a custom overflow indicator:
                        //  https://proandroiddev.com/detect-text-overflow-in-jetpack-compose-56c0b83da5a5.
                        overflow = TextOverflow.Visible,
                        textAlign = TextAlign.Center,
                        modifier = textModifier.align(Alignment.Center),
                    )
                }
            }
    }
}
