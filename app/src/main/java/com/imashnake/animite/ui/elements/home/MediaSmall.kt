package com.imashnake.animite.ui.elements.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.imashnake.animite.ui.theme.Card
import com.imashnake.animite.ui.theme.Text
import com.imashnake.animite.ui.theme.manropeFamily
import com.imashnake.animite.ui.theme.mediaSmallShape

/**
 * TODO:
 *  - Kdoc.
 *  - Extract dimens to `ui.theme`.
 */
@ExperimentalMaterial3Api
@Composable
fun MediaSmall(image: String?, anime: String?) {
    Card(
        onClick = { },
        modifier = Modifier
            .wrapContentHeight()
            .width((115 + 12 + 12).dp)
            .padding(start = 12.dp, end = 12.dp),
        containerColor = Card,
        shape = mediaSmallShape
    ) {
        Card(shape = mediaSmallShape) {
            AsyncImage(
                model = image,
                contentDescription = anime,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(198.dp)
            )
        }
        Text(
            text = anime ?: "",
            color = Text,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .padding(14.dp),
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium
        )
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewMediaSmall() {
    MediaSmall(
        image =
        "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx132405-qP7FQYGmNI3d.jpg",
        anime =
        "Sono Bisque Doll wa Koi wo Suru"
    )
}
