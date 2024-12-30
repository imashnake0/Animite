package com.imashnake.animite.profile.tabs

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaSmall
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.core.R as coreR

@Composable
fun AnimeTab(
    mediaCollection: User.MediaCollection?,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier
            .verticalScroll(scrollState)
            .padding(vertical = LocalPaddings.current.large)
    ) {
        // TODO: Why is this not smart-casting?
        if (!mediaCollection?.namedLists.isNullOrEmpty()) {
            UserMediaList(
                lists =  mediaCollection!!.namedLists,
                onNavigateToMediaItem = onNavigateToMediaItem,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun UserMediaList(
    lists: List<User.MediaCollection.NamedList>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large),
        modifier = modifier
    ) {
        lists.fastForEach {
            MediaSmallRow(it.name, it.list) { media ->
                MediaSmall(
                    image = media.coverImage,
                    label = media.title,
                    onClick = {
                        onNavigateToMediaItem(
                            MediaPage(
                                id = media.id,
                                // TODO: Unhardcode these and make them unique.
                                source = media.title.orEmpty(),
                                mediaType = MediaType.ANIME.rawValue,
                            )
                        )
                    },
                    imageHeight = dimensionResource(coreR.dimen.media_image_height),
                    cardWidth = dimensionResource(coreR.dimen.media_card_width),
                )
            }
        }
    }
}
