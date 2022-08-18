package com.imashnake.animite.features.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.features.theme.Backdrop
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MediaPage(
    viewModel: MediaPageViewModel = hiltViewModel()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Backdrop)
    ) {
        Text(text = "Media where?")
    }
}
