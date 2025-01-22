package com.imashnake.animite.profile.tabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User.MediaCollection.NamedList
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaSmall
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.media.R as mediaR
import com.imashnake.animite.core.R as coreR

@Composable
fun FavouritesTab(
    favouriteLists: List<NamedList>,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier
            .verticalScroll(scrollState)
            .padding(vertical = LocalPaddings.current.large)
    ) {
        UserFavouriteLists(favouriteLists)
    }

}

@Composable
private fun UserFavouriteLists(
    lists: List<NamedList>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large),
        modifier = modifier
    ) {
        lists.fastForEach { namedList ->
            MediaSmallRow(namedList.name, namedList.list) { item ->
                when(item) {
                    is Media.Small -> {
                        MediaSmall(
                            image = item.coverImage,
                            label = item.title,
                            onClick = {},
                            imageHeight = dimensionResource(coreR.dimen.media_image_height),
                            cardWidth = dimensionResource(coreR.dimen.media_card_width),
                        )
                    }
                    is Media.Character -> {
                        MediaSmall(
                            image = item.image,
                            label = item.name,
                            onClick = { Log.d("CharacterId", "${item.id}") },
                            imageHeight = dimensionResource(mediaR.dimen.character_image_height),
                            cardWidth = dimensionResource(mediaR.dimen.character_card_width),
                        )
                    }
                }
            }
        }
    }
}
