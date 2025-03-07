package com.imashnake.animite.profile.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import com.imashnake.animite.api.anilist.sanitize.profile.User.MediaCollection.NamedList
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.verticalOnly
import com.imashnake.animite.core.ui.CharacterCard
import com.imashnake.animite.core.ui.FallbackScreen
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.profile.R

/**
 * The viewer's favourite Anime, Manga, and Characters.
 *
 * @param favouriteLists List of the favourite items as [NamedList]s.
 */
@Composable
fun FavouritesTab(
    favouriteLists: List<NamedList>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    when {
        favouriteLists.isEmpty() -> FallbackScreen(stringResource(R.string.no_favourites))
        else -> UserFavouriteLists(
            lists = favouriteLists,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    }
}

@Composable
private fun UserFavouriteLists(
    lists: List<NamedList>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(contentPadding.verticalOnly),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large),
    ) {
        lists.fastForEach { namedList ->
            MediaSmallRow(
                title = namedList.name,
                mediaList = namedList.list,
                contentPadding = contentPadding.horizontalOnly,
            ) { item ->
                when(item) {
                    is Media.Small -> {
                        MediaCard(
                            image = item.coverImage,
                            label = item.title,
                            onClick = {},
                        )
                    }
                    is Media.Character -> {
                        CharacterCard(
                            image = item.image,
                            label = item.name,
                            onClick = { Log.d("CharacterId", "${item.id}") },
                        )
                    }
                }
            }
        }
    }
}
