package com.imashnake.animite.profile.tabs

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.FallbackScreen
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.navigation.SharedContentKey.Component.Text
import com.imashnake.animite.profile.R

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
    val scrollState = rememberScrollState()

    when {
        mediaCollection?.namedLists?.isEmpty() == true -> {
            FallbackScreen(
                stringResource(
                    when (mediaCollection.type) {
                        Media.Small.Type.ANIME -> R.string.no_anime
                        Media.Small.Type.MANGA -> R.string.no_manga
                        Media.Small.Type.UNKNOWN -> R.string.no_media
                    }
                )
            )
        }
        else -> Column(
            modifier
                .verticalScroll(scrollState)
                .padding(vertical = LocalPaddings.current.large / 2)
                .padding(bottom = contentPadding.calculateBottomPadding())
        ) {
            if (!mediaCollection?.namedLists.isNullOrEmpty()) {
                UserMediaLists(
                    lists =  mediaCollection.namedLists,
                    onNavigateToMediaItem = onNavigateToMediaItem,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = modifier,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}

@Composable
private fun UserMediaLists(
    lists: List<User.MediaCollection.NamedList>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(modifier = modifier) {
        lists.fastForEach { namedList ->
            MediaSmallRow(
                title = namedList.name,
                mediaList = namedList.list,
                contentPadding = contentPadding,
            ) { item ->
                with(sharedTransitionScope) {
                    val media = item as Media.Small
                    MediaCard(
                        image = media.coverImage,
                        label = media.title,
                        onClick = {
                            onNavigateToMediaItem(
                                MediaPage(
                                    id = media.id,
                                    source = namedList.name.orEmpty(),
                                    mediaType = media.type.name,
                                )
                            )
                        },
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState(
                                SharedContentKey(
                                    id = media.id,
                                    source = namedList.name,
                                    sharedComponents = Card to Page,
                                )
                            ),
                            animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        ),
                        imageModifier = Modifier.sharedBounds(
                            rememberSharedContentState(
                                SharedContentKey(
                                    id = media.id,
                                    source = namedList.name,
                                    sharedComponents = Image to Image,
                                )
                            ),
                            animatedVisibilityScope,
                        ),
                        textModifier = Modifier.sharedBounds(
                            rememberSharedContentState(
                                SharedContentKey(
                                    id = media.id,
                                    source = namedList.name,
                                    sharedComponents = Text to Text,
                                )
                            ),
                            animatedVisibilityScope,
                        ),
                    )
                }
            }
        }
    }
}
