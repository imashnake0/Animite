package com.imashnake.animite.features.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.imashnake.animite.R as Res

@Composable
fun ProgressIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .clip(CircleShape)
            .width(dimensionResource(Res.dimen.progress_indicator_width))
            .height(dimensionResource(Res.dimen.progress_indicator_height)),
        color = MaterialTheme.colorScheme.background,
        trackColor = MaterialTheme.colorScheme.primary
    )
}
