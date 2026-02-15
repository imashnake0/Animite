package com.imashnake.animite.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.imashnake.animite.core.R
import com.imashnake.animite.core.extensions.crossfadeModel
import kotlinx.collections.immutable.ImmutableList

/**
 * A [LazyRow] of [MediaSmall]s.
 *
 * @param mediaList A list of [T]s.
 */
@Composable
fun <T> MediaSmallRow(
    title: String?,
    mediaList: ImmutableList<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable LazyItemScope.(Int, T) -> Unit
) {
    Column(
        modifier = modifier.padding(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
    ) {
        val layoutDirection = LocalLayoutDirection.current
        val startPadding = contentPadding.calculateStartPadding(layoutDirection)
        val endPadding = contentPadding.calculateEndPadding(layoutDirection)
        if (title != null) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                    start = startPadding,
                    end = endPadding,
                )
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            contentPadding = PaddingValues(
                start = startPadding,
                end = endPadding,
            )
        ) {
            itemsIndexed(mediaList) { index, media ->
                content(index, media)
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
    tag: String?,
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
        tag = tag,
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
    tag: String?,
    tagMinLines: Int,
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
        tag = tag,
        tagMinLines = tagMinLines,
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
    tag: String?,
    label: String?,
    onClick: () -> Unit,
    imageHeight: Dp,
    cardWidth: Dp,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    tagMinLines: Int = 1,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(R.dimen.media_card_corner_radius)),
        modifier = modifier.width(cardWidth),
    ) {
        Box(
            modifier = imageModifier
                .height(imageHeight)
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.media_card_corner_radius)))
        ) {
            AsyncImage(
                model = crossfadeModel(image),
                contentDescription = label,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (tag != null)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = 7f
                        }
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        .padding(vertical = LocalPaddings.current.tiny)
                        // Because the cropping is being weird
                        .padding(bottom = LocalPaddings.current.tiny)
                        .padding(horizontal = LocalPaddings.current.medium)
                ) {
                    if (tagMinLines > 1) {
                        Text(
                            text = " \n ",
                            maxLines = tagMinLines,
                            minLines = tagMinLines,
                            lineHeight = 16.sp,
                        )
                    }
                    Text(
                        text = tag,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        maxLines = tagMinLines,
                        lineHeight = 16.sp,
                    )
                }
        }

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

@Preview
@Composable
private fun PreviewCharacterCard() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        CharacterCard(
            image = null,
            tag = "tag",
            label = "label",
            tagMinLines = 1,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewCharacterCardShortTag() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        CharacterCard(
            image = null,
            tag = "tag",
            tagMinLines = 2,
            label = "label",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewCharacterCardLongTag() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        CharacterCard(
            image = null,
            tag = "long\ntag",
            tagMinLines = 2,
            label = "long\nlabel",
            onClick = {},
        )
    }
}
