package com.imashnake.animite.features.searchbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.R
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.Constants
import com.imashnake.animite.core.extensions.copy
import com.imashnake.animite.media.MediaMediumList
import com.imashnake.animite.navigation.R as navigationR

/**
 * Search bar along with a Front Drop list.
 *
 * @param hasExtraPadding if the search bar should have extra bottom padding to accommodate the
 * [com.imashnake.animite.navigation.NavigationBar].
 * @param onItemClick called when media with an ID and [MediaType] is clicked.
 * @param modifier the [Modifier] to be applied to this Front Drop.
 * @param viewModel [SearchViewModel] instance.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchFrontDrop(
    hasExtraPadding: Boolean,
    isFabVisible: Boolean,
    onItemClick: (Int, MediaType, String?) -> Unit,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: SearchViewModel = viewModel()
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()

    val searchMediaType = MediaType.ANIME
    viewModel.setMediaType(searchMediaType)
    val searchList by viewModel.searchList.collectAsState()

    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val searchBarBottomPadding by animateDpAsState(
        targetValue = if (hasExtraPadding) {
            dimensionResource(navigationR.dimen.navigation_bar_height)
        } else 0.dp,
        label = "translate_search_bar"
    )
    val frontDropColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background.copy(
            alpha = if (isExpanded) 0.95f else 0f
        ),
        animationSpec = tween(Constants.CROSSFADE_DURATION),
        label = "show_front_drop"
    )

    Box(
        Modifier
            .fillMaxSize()
            .drawBehind { drawRect(frontDropColor) }
    )

    searchList.data?.let {
        AnimatedVisibility(
            visible = it.isNotEmpty(),
            enter = fadeIn(tween(750)),
            exit = fadeOut(tween(750)),
        ) {
            MediaMediumList(
                mediaMediumList = it,
                onItemClick = { id, title ->
                    isExpanded = false
                    viewModel.setQuery(null)
                    onItemClick(id, searchMediaType, title)
                },
                modifier = Modifier.imeNestedScroll(),
                searchBarHeight = dimensionResource(R.dimen.search_bar_height),
                searchBarBottomPadding = searchBarBottomPadding,
                contentPadding = insetPaddingValues,
            )
        }
    }

    AnimatedVisibility(
        visible = isFabVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
            .padding(insetPaddingValues.copy(bottom = 0.dp))
            .padding(bottom = searchBarBottomPadding)
            .navigationBarsPadding()
            .consumeWindowInsets(PaddingValues(bottom = searchBarBottomPadding))
            .imePadding()
            .height(dimensionResource(R.dimen.search_bar_height))
    ) {
        SearchFab(
            isExpanded = isExpanded,
            setExpanded = { isExpanded = it },
            onSearched = viewModel::setQuery,
        )
    }
}
