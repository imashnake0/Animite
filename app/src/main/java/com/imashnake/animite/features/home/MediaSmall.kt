package com.imashnake.animite.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.imashnake.animite.features.theme.mediaSmallShape

@Composable
fun MediaSmall(image: String?, anime: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .width(115.dp)
            .clickable(
                enabled = true,
                onClick = onClick
            ),
        shape = mediaSmallShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        AsyncImage(
            model = image,
            contentDescription = anime,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(198.dp)
                .clip(mediaSmallShape)
        )
        Text(
            text = anime.orEmpty(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.padding(14.dp)
        )
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
