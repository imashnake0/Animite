package com.imashnake.animite.features.searchbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.R
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.core.ui.Icon
import com.imashnake.animite.core.ui.IconButton
import com.imashnake.animite.core.ui.TextField
import com.imashnake.animite.dev.internal.Constants

/**
 * TODO: Kdoc
 * @param hasExtraPadding TODO: This causes recomposition! Probably wrap it in a lambda.
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@Composable
fun SearchFrontDrop(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(),
    hasExtraPadding: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }
    // This should ideally be in a modifier lambda to prevent recomposition.
    // Unfortunately, we have nothing of the sort for padding. Open an issue?
    val searchBarBottomPadding: Dp by animateDpAsState(
        targetValue = if (hasExtraPadding) {
            dimensionResource(R.dimen.navigation_bar_height)
        } else 0.dp
    )
    val frontDropColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background.copy(
            alpha = if (isExpanded) 0.95f else 0f
        ),
        animationSpec = tween(Constants.CROSSFADE_DURATION)
    )

    Box(Modifier.fillMaxSize().drawBehind { drawRect(frontDropColor) })

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .padding(bottom = searchBarBottomPadding)
            .navigationBarsPadding()
            .consumeWindowInsets(
                PaddingValues(bottom = searchBarBottomPadding)
            )
            .imePadding()
            .height(dimensionResource(R.dimen.search_bar_height)),
        shadowElevation = 20.dp,
        shape = CircleShape
    ) {
        AnimatedContent(targetState = isExpanded) { targetExpanded ->
            if (targetExpanded) {
                ExpandedSearchBarContent(
                    collapse = { isExpanded = !isExpanded },
                    clearText = { viewModel.clearList() },
                    searchText = { viewModel.searchAnime(it) }
                )
            } else {
                CollapsedSearchBarContent(
                    expand = { isExpanded = !isExpanded }
                )
            }
        }
    }
}

@Composable
fun CollapsedSearchBarContent(
    modifier: Modifier = Modifier,
    expand: () -> Unit
) {
    Icon(
        imageVector = ImageVector.vectorResource(R.drawable.search),
        contentDescription = stringResource(R.string.search),
        modifier = modifier.clickable { expand() }
    )
}


@Composable
fun ExpandedSearchBarContent(
    modifier: Modifier = Modifier,
    collapse: () -> Unit,
    clearText: () -> Unit,
    searchText: (String) -> Unit
) {
    // TODO: How does this work?
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {
            text = it
            searchText(it)
        },
        placeholder = stringResource(R.string.search),
        modifier = modifier
            .focusRequester(focusRequester)
            .height(dimensionResource(R.dimen.search_bar_height)),
        keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Search),
        leadingIcon = {
            IconButton(
                onClick = collapse,
                imageVector = Icons.Rounded.KeyboardArrowRight
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    text = ""
                    clearText()
                },
                imageVector = Icons.Rounded.Close
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchList(
    // TODO: Handle nullability in [#67](https://github.com/imashnake0/Animite/pull/67).
    searchList: List<SearchQuery.Medium?>?,
    modifier: Modifier = Modifier,
    onClick: (Int?) -> Unit
) {
    if (!searchList.isNullOrEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.medium_padding),
                end = dimensionResource(R.dimen.medium_padding),
                bottom = dimensionResource(R.dimen.search_bar_height)
                        + dimensionResource(R.dimen.large_padding)
                        + dimensionResource(R.dimen.large_padding)
            )
        ) {
            items(searchList.size, key = { searchList[it]!!.id }) {
                SearchItem(
                    item = searchList[it],
                    onClick = onClick,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}

@Composable
private fun SearchItem(
    item: SearchQuery.Medium?,
    onClick: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        // TODO: Do something about this chain.
        text = item?.title?.romaji ?:
        item?.title?.english ?:
        item?.title?.native.orEmpty(),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelLarge,
        maxLines = 1,
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                onClick(item?.id)
            }
            .padding(dimensionResource(R.dimen.search_list_padding))
            .fillMaxSize()
    )
}
