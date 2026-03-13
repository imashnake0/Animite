package com.imashnake.animite.explore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.media.MediaMediumList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val exploreList by viewModel.exploreList.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (exploreList) {
            is Resource.Success -> {
                MediaMediumList(
                    mediaMediumList = exploreList.data.orEmpty().toImmutableList(),
                    onItemClick = { _, _ -> },
                    modifier = Modifier.imeNestedScroll(),
                )
            }
            is Resource.Loading -> {

            }
            is Resource.Error -> {

            }
        }
    }
}
