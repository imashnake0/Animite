package com.imashnake.animite.ui.elements.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imashnake.animite.MediaListQuery

/**
 * TODO: Extract dimens to `ui.theme`.
 */
@ExperimentalMaterial3Api
@Composable
fun MediaSmallRow(mediaList: List<MediaListQuery.Medium?>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        items(mediaList) { media ->
            MediaSmall(
                image = media?.coverImage?.large,
                anime = media?.title?.romaji ?:
                media?.title?.english ?:
                media?.title?.native
            )
        }
    }
}

/**
 * Example:
 *
 * **Anime:** Sono Bisque Doll wa Koi wo Suru;
 * **ID:** 132405.
 */
@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewMediaSmallRow() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        items(10) {
            MediaSmall(
                image =
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx132405-qP7FQYGmNI3d.jpg",
                anime =
                "Sono Bisque Doll wa Koi wo Suru"
            )
        }
    }
}
