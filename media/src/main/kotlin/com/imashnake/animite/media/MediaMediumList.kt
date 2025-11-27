package com.imashnake.animite.media

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imashnake.animite.api.anilist.sanitize.media.MediaMedium
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.core.R as coreR

@Composable
fun MediaMediumList(
    mediaMediumList: List<MediaMedium>,
    onItemClick: (Int, String?) -> Unit,
    modifier: Modifier = Modifier,
    searchBarHeight: Dp = 0.dp,
    searchBarBottomPadding: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier
            .consumeWindowInsets(
                PaddingValues(
                    bottom = searchBarBottomPadding + contentPadding.calculateBottomPadding()
                )
            )
            .imePadding(),
        contentPadding = PaddingValues(
            LocalPaddings.current.large
        ) + PaddingValues(
            bottom = LocalPaddings.current.large +
                    searchBarHeight +
                    searchBarBottomPadding
        ) + contentPadding,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
    ) {
        items(mediaMediumList.size, key = { mediaMediumList[it].id }) { index ->
            MediaMediumItem(
                item = mediaMediumList[index],
                onClick = onItemClick,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun MediaMediumItem(
    item: MediaMedium,
    onClick: (Int, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.media_card_corner_radius)))
            .clickable { onClick(item.id, item.title) }
    ) {
        MediaCard(
            image = item.coverImage,
            label = null,
            onClick = { onClick(item.id, item.title) },
        )

        Column(Modifier.padding(horizontal = LocalPaddings.current.small)) {
            Text(
                text = item.title.orEmpty(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2
            )
            if (item.seasonYear != null) {
                Text(
                    text = item.seasonYear.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.size(LocalPaddings.current.medium))

            Text(
                text = item.studios.joinToString(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = listOfNotNull(
                    item.format.string.takeIf { it.isNotEmpty() },
                    item.episodes?.let { "$it episodes" }
                ).joinToString(" ꞏ "),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
