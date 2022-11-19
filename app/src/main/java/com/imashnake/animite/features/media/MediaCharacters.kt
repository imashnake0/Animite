package com.imashnake.animite.features.media

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.imashnake.animite.R
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow

@Composable
fun MediaCharacters(
    characters: List<Character>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.characters),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(contentPadding)
        )

        Spacer(Modifier.size(dimensionResource(R.dimen.medium_padding)))

        MediaSmallRow(
            mediaList = characters
        ) { character ->
            MediaSmall(
                image = character.image,
                label = character.name,
                onClick = { Log.d("CharacterId", "${character.id}") },
                modifier = Modifier.width(dimensionResource(R.dimen.character_card_width))
            )
        }
    }
}
