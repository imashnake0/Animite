package com.imashnake.animite.core.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import com.imashnake.animite.core.R

@Composable
fun ProgressIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .width(dimensionResource(R.dimen.progress_indicator_width))
            .height(dimensionResource(R.dimen.progress_indicator_height)),
        strokeCap = StrokeCap.Round
    )
}
