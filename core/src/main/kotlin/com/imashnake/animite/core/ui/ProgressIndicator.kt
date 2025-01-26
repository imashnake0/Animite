package com.imashnake.animite.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import com.imashnake.animite.core.R

@Composable
fun ProgressIndicatorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { ProgressIndicator() }
}

@Composable
fun ProgressIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .width(dimensionResource(R.dimen.progress_indicator_width))
            .height(dimensionResource(R.dimen.progress_indicator_height)),
        strokeCap = StrokeCap.Round
    )
}
