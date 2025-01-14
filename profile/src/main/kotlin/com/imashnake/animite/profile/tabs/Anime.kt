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
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaSmall
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.navigation.SharedContentKey.Component.Text
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
        lists.fastForEach { namedList ->
            MediaSmallRow(namedList.name, namedList.list) { media ->
                with(sharedTransitionScope) {
                    MediaSmall(
                        image = media.coverImage,
                        label = media.title,
                        onClick = {
                            onNavigateToMediaItem(
                                MediaPage(
                                    id = media.id,
                                    // TODO: We can use the list's index instead.
                                    source = namedList.name.orEmpty(),
                                    mediaType = media.type.name,
                                )
                            )
                        },
                        imageHeight = dimensionResource(coreR.dimen.media_image_height),
                        cardWidth = dimensionResource(coreR.dimen.media_card_width),
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
