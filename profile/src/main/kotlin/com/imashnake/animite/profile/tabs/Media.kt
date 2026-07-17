package com.imashnake.animite.profile.tabs

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.ext.horizontalOnly
import com.imashnake.animite.core.ui.ext.verticalOnly
import com.imashnake.animite.core.ui.screen.FallbackScreen
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.MediaCard
import com.imashnake.animite.core.ui.component.MediaSmallRow
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.navigation.SharedContentKey.Component.Text
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
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
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
                lists = mediaCollection.namedLists,
                onNavigateToMediaItem = onNavigateToMediaItem,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = modifier,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
private fun UserMediaLists(
    lists: ImmutableList<User.MediaCollection.NamedTrackingList>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large),
    ) {
        MediaTrackingLists(
            namedLists = lists,
            onItemClick = { id, title ->  },
            contentPadding = contentPadding
        )
    }
}
