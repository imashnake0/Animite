package com.imashnake.animite.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.imashnake.animite.core.R
import com.imashnake.animite.core.extensions.crossfadeModel

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
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (T) -> Unit
) {
    Column(
        modifier = modifier.padding(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
    ) {
        val startPadding = contentPadding.calculateStartPadding(LocalLayoutDirection.current)
        val endPadding = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
        if (title != null) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(
                        start = startPadding,
                        end = endPadding
                    )
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            contentPadding = PaddingValues(
                start = startPadding,
                end = endPadding
            )
        ) {
            items(mediaList) { media ->
                content(media)
            }
        }
    }
}

/**
 * A [Card] that displays media (anime or manga) thumbnail, and an optional label.
 *
 * @param image A URL of the image to be shown in the card that this component is.
 * @param label A label for the [image], if this is `null`, the [label] is not shown.
 * @param onClick Action to happen when the card is clicked.
 */
@Composable
fun MediaCard(
    image: String?,
    label: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
) {
    MediaSmall(
        image = image,
        onClick = onClick,
        imageHeight = 200.dp,
        cardWidth = 140.dp,
        modifier = modifier,
        imageModifier = imageModifier,
        textModifier = textModifier,
        label = label
    )
}

/**
 * A [Card] that displays a character thumbnail, and an optional label.
 *
 * @param image A URL of the image to be shown in the card that this component is.
 * @param label A label for the [image], if this is `null`, the [label] is not shown.
 * @param onClick Action to happen when the card is clicked.
 */
@Composable
fun CharacterCard(
    image: String?,
    label: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
) {
    MediaSmall(
        image = image,
        onClick = onClick,
        imageHeight = 137.dp,
        cardWidth = 96.dp,
        modifier = modifier,
        imageModifier = imageModifier,
        textModifier = textModifier,
        label = label
    )
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
internal fun MediaSmall(
    image: String?,
    label: String?,
    onClick: () -> Unit,
    imageHeight: Dp,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
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
