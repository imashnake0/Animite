package com.imashnake.animite.features.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.features.theme.Backdrop
import com.imashnake.animite.features.theme.Text
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MediaPage(
    id: Int?,
    mediaTypeArg: String,
    viewModel: MediaPageViewModel = hiltViewModel()
) {


    val mediaType = MediaType.safeValueOf(mediaTypeArg)

    val media = viewModel.uiState

    viewModel.populateMediaPage(id, mediaType)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Backdrop)
            .systemBarsPadding()
    ) {
        Text(
            text = """
                bannerImage: ${media.bannerImage}
                coverImage: ${media.coverImage}
                title: ${media.title}
                description: ${media.description}
                averageScore: ${media.averageScore}
                ranks: ${media.ranks}
                genres: ${media.genres}
                characters: ${media.characters}
                trailer: ${media.trailer}
            """.trimIndent(),
            color = Text,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(70.dp)
        )
    }
}
