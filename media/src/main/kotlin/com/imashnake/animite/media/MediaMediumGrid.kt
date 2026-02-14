package com.imashnake.animite.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MediaMediumGrid(
    mediaMediumList: ImmutableList<Media.Medium>,
    onItemClick: (Int, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(140.dp),
        modifier = modifier.imePadding(),
        contentPadding = PaddingValues(LocalPaddings.current.large) +
                PaddingValues(bottom = LocalPaddings.current.large),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {
        items(
            items = mediaMediumList,
            key = { it.id }
        ) { media ->
            MediaCard(
                image = media.coverImage,
                tag = null,
                label = media.title,
                onClick = { onItemClick(media.id, media.title) }
            )
        }
    }
}