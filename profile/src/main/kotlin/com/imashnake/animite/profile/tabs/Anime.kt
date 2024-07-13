package com.imashnake.animite.profile.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaSmall
import com.imashnake.animite.core.ui.MediaSmallRow

@Composable
fun AnimeTab(
    mediaCollection: User.MediaCollection?,
    modifier: Modifier = Modifier
) {
    // TODO: Why is this not smart-casting?
    if (!mediaCollection?.namedLists.isNullOrEmpty()) {
        UserMediaList(mediaCollection!!.namedLists, modifier)
    }
}

@Composable
private fun UserMediaList(
    lists: List<User.MediaCollection.NamedList>,
    modifier: Modifier = Modifier
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
                    onClick = {},
                    modifier = Modifier.width(dimensionResource(com.imashnake.animite.core.R.dimen.media_card_width))
                )
            }
        }
    }
}
