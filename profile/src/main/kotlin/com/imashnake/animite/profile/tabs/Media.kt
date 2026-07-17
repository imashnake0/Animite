package com.imashnake.animite.profile.tabs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.screen.FallbackScreen
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.ui.MediaTrackingLists
import kotlinx.collections.immutable.ImmutableList

/**
 * This can either be the anime tab or manga tab, it has a collection of lists for different
 * statuses.
 *
 * @param mediaCollection Collection of status lists for anime and manga.
 */
@Composable
fun MediaTab(
    mediaCollection: User.MediaCollection?,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when {
        mediaCollection?.namedLists?.isEmpty() == true -> {
            FallbackScreen(
                message = stringResource(
                    when (mediaCollection.type) {
                        Media.Small.Type.ANIME -> R.string.no_anime
                        Media.Small.Type.MANGA -> R.string.no_manga
                        Media.Small.Type.UNKNOWN -> R.string.no_media
                    }
                ),
                modifier = modifier.padding(contentPadding),
            )
        }

        else -> if (!mediaCollection?.namedLists.isNullOrEmpty()) {
            UserMediaLists(
                type = mediaCollection.type,
                lists = mediaCollection.namedLists,
                onNavigateToMediaItem = onNavigateToMediaItem,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
private fun UserMediaLists(
    type: Media.Small.Type,
    lists: ImmutableList<User.MediaCollection.NamedTrackingList>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    MediaTrackingLists(
        type = type,
        namedLists = lists,
        onNavigateToMediaItem = onNavigateToMediaItem,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxSize()
    )
}
