package com.imashnake.animite.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.MediaQuery
import com.imashnake.animite.type.MediaType
import com.imashnake.animite.R as Res

/**
 * TODO: This component is hardly reusable, make changes to the API.
 * A [LazyRow] of [MediaSmall]s.
 *
 * @param mediaList A list of [MediaListQuery.Medium]s.
 * @param onItemClick Action that is propagated to the [MediaSmall]s, it takes a unique id.
 */
@Composable
fun MediaSmallRow(mediaList: List<MediaListQuery.Medium?>, onItemClick: (itemId: Int?) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(Res.dimen.small_padding)),
        contentPadding = PaddingValues(
            start = dimensionResource(Res.dimen.large_padding),
            end = dimensionResource(Res.dimen.large_padding)
        )
    ) {
        items(mediaList.filterNotNull()) { media ->
            MediaSmall(
                image = media.coverImage?.extraLarge,
                // TODO: Do something about this chain.
                label = media.title?.romaji ?:
                media.title?.english ?:
                media.title?.native ?: "",
                onClick = {
                    onItemClick(media.id)
                }
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
                     extraLarge = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx132405-qP7FQYGmNI3d.jpg",
                     large = null
                 )
             )
        },
        onItemClick = {  }
    )
}
