package com.imashnake.animite.profile.tabs

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User.MediaCollection.NamedList
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.verticalOnly
import com.imashnake.animite.core.ui.CharacterCard
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
import kotlinx.collections.immutable.ImmutableList

/**
 * The viewer's favourite Anime, Manga, and Characters.
 *
 * @param favouriteLists List of the favourite items as [NamedList]s.
 */
@Composable
fun FavouritesTab(
    favouriteLists: ImmutableList<NamedList>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when {
        favouriteLists.isEmpty() -> FallbackScreen(
            message = stringResource(R.string.no_favourites),
            modifier = modifier.padding(contentPadding),
        )
        else -> UserFavouriteLists(
            lists = favouriteLists,
            onNavigateToMediaItem = onNavigateToMediaItem,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    }
}

@Composable
private fun UserFavouriteLists(
    lists: ImmutableList<NamedList>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(contentPadding.verticalOnly),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large),
    ) {
        lists.fastForEach { namedList ->
            MediaSmallRow(
                title = namedList.name,
                mediaList = namedList.list,
                contentPadding = contentPadding.horizontalOnly,
            ) { _, item ->
                when(item) {
                    is Media.Small -> {
                        with(sharedTransitionScope) {
                            MediaCard(
                                image = item.coverImage,
                                tag = null,
                                label = item.title,
                                onClick = {
                                    onNavigateToMediaItem(
                                        MediaPage(
                                            id = item.id,
                                            source = namedList.name.orEmpty(),
                                            mediaType = item.type.name,
                                            title = item.title,
                                        )
                                    )
                                },
                                modifier = Modifier.sharedBounds(
                                    rememberSharedContentState(
                                        SharedContentKey(
                                            id = item.id,
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
                                            id = item.id,
                                            source = namedList.name,
                                            sharedComponents = Image to Image,
                                        )
                                    ),
                                    animatedVisibilityScope,
                                ),
                                textModifier = Modifier.sharedBounds(
                                    rememberSharedContentState(
                                        SharedContentKey(
                                            id = item.id,
                                            source = namedList.name,
                                            sharedComponents = Text to Text,
                                        )
                                    ),
                                    animatedVisibilityScope,
                                ),
                            )
                        }
                    }
                    is Media.Credit -> {
                        CharacterCard(
                            image = item.image,
                            tag = null,
                            label = item.name,
                            onClick = { Log.d("CharacterId", "${item.id}") },
                        )
                    }
                }
            }
        }
    }
}
