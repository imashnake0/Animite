package com.imashnake.animite.ui.elements

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.ui.state.MediaPageViewModel

@Composable
fun MediaPage(
    viewModel: MediaPageViewModel = viewModel()
) {
    Text(text = "Hello there")
}
