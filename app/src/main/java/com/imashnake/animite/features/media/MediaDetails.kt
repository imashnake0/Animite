package com.imashnake.animite.features.media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.imashnake.animite.R
import com.imashnake.animite.core.ui.scrim

@Composable
fun MediaDetails(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = ContentAlpha.medium
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .scrim(
                    color = MaterialTheme.colorScheme.background,
                    height = dimensionResource(R.dimen.small_padding)
                )
                .padding(vertical = dimensionResource(R.dimen.small_padding))
        )
    }
}