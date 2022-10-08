package com.imashnake.animite.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.imashnake.animite.R as Res

@Composable
fun MediaSmall(image: String?, anime: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .width(dimensionResource(Res.dimen.media_card_width))
            .clip(RoundedCornerShape(dimensionResource(Res.dimen.media_card_corner_radius)))
            .clickable(
                enabled = true,
                onClick = onClick
            ),
        shape = RoundedCornerShape(dimensionResource(Res.dimen.media_card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        AsyncImage(
            model = image,
            contentDescription = anime,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(dimensionResource(Res.dimen.media_card_height))
                .clip(RoundedCornerShape(dimensionResource(Res.dimen.media_card_corner_radius)))
        )

        Box {
            Text(
                text = " \n ",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                modifier = Modifier.padding(
                    vertical = dimensionResource(Res.dimen.media_card_text_padding_vertical)
                )
            )

            Text(
                text = anime.orEmpty(),
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
                        horizontal = dimensionResource(Res.dimen.media_card_text_padding_horizontal),
                        vertical = dimensionResource(Res.dimen.media_card_text_padding_vertical)
                    )
            )
        }
    }
}

@Preview
@Composable
fun PreviewMediaSmall() {
    MediaSmall(
        image =
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx132405-qP7FQYGmNI3d.jpg",
        anime =
        "Sono Bisque Doll wa Koi wo Suru",
        onClick = {  }
    )
}
