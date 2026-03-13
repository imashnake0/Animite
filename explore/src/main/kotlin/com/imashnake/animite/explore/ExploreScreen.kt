package com.imashnake.animite.explore

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.media.MediaMediumList
import kotlinx.collections.immutable.toImmutableList
import com.imashnake.animite.navigation.R as navigationR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars
        .exclude(WindowInsets.navigationBars)
        .union(WindowInsets.displayCutout),
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(bottom = dimensionResource(navigationR.dimen.navigation_bar_height))
        else -> PaddingValues(start = dimensionResource(navigationR.dimen.navigation_rail_width))
    }
    val insetAndNavigationPaddingValues = contentWindowInsets.asPaddingValues() + navigationComponentPaddingValues

    val exploreList by viewModel.exploreList.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (exploreList) {
            is Resource.Success -> {
                MediaMediumList(
                    mediaMediumList = exploreList.data.orEmpty().toImmutableList(),
                    onItemClick = { _, _ -> },
                    contentPadding = insetAndNavigationPaddingValues,
                )
            }
            is Resource.Loading -> {

            }
            is Resource.Error -> {

            }
        }
    }
}
