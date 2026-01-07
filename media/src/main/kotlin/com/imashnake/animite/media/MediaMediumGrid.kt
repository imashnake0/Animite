package com.imashnake.animite.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard

@Composable
fun MediaMediumGrid(
    modifier: Modifier = Modifier,
    mediaMediumList: List<Media.Medium>,
    onItemClick: (Int, String?) -> Unit,
    searchBarHeight: Dp = 0.dp,
    searchBarBottomPadding: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .consumeWindowInsets(
                PaddingValues(
                    bottom = searchBarBottomPadding + contentPadding.calculateBottomPadding()
                )
            )
            .imePadding(),
        contentPadding = PaddingValues(
            LocalPaddings.current.medium
        ) + PaddingValues(
            bottom = LocalPaddings.current.medium +
                    searchBarHeight +
                    searchBarBottomPadding
        ) + contentPadding,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
    ) {
        items(
            items = mediaMediumList,
            key = { it.id }
        ) { media ->
            MediaCard(
                image = media.coverImage,
                label = media.title,
                onClick = { onItemClick(media.id, media.title) }
            )
        }
    }
}