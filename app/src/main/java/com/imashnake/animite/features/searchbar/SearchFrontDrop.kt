package com.imashnake.animite.features.searchbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.R
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.core.ui.Icon
import com.imashnake.animite.core.ui.IconButton
import com.imashnake.animite.core.ui.TextField
import com.imashnake.animite.dev.ext.string
import com.imashnake.animite.dev.internal.Constants
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.type.MediaFormat
import com.imashnake.animite.type.MediaType

/**
 * TODO: Kdoc
 * @param hasExtraPadding TODO: This causes recomposition! Probably wrap it in a lambda.
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@Composable
fun SearchFrontDrop(
    hasExtraPadding: Boolean,
    onItemClick: (Int?, MediaType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val searchMediaType = MediaType.ANIME

    viewModel.setMediaType(searchMediaType)

    val searchList by viewModel.searchList.collectAsState()

    var isExpanded by rememberSaveable { mutableStateOf(false) }
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

    Box(
        Modifier
            .fillMaxSize()
            .drawBehind { drawRect(frontDropColor) })

    AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(tween(Constants.CROSSFADE_DURATION)),
        exit = fadeOut(tween(Constants.CROSSFADE_DURATION))
    ) {
        SearchList(
            searchList = searchList.data?.media,
            modifier = Modifier
                .imeNestedScroll()
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize(),
            onItemClick = {
                isExpanded = !isExpanded
                viewModel.setQuery(null)
                onItemClick(it, searchMediaType)
            }
        )
    }

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
                    collapse = {
                        isExpanded = !isExpanded
                        viewModel.setQuery(null)
                    },
                    clearText = { viewModel.setQuery(null) },
                    searchText = { viewModel.setQuery(it) }
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

    var text by rememberSaveable { mutableStateOf("") }
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
    onItemClick: (Int?) -> Unit
) {
    if (!searchList.isNullOrEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.large_padding),
                end = dimensionResource(R.dimen.large_padding),
                top = dimensionResource(R.dimen.large_padding),
                bottom = dimensionResource(R.dimen.search_bar_height)
                        + dimensionResource(R.dimen.large_padding)
                        + dimensionResource(R.dimen.large_padding)
                        + dimensionResource(R.dimen.navigation_bar_height)
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small_padding))
        ) {
            items(searchList.size, key = { searchList[it]!!.id }) {
                SearchItem(
                    item = searchList[it],
                    onClick = onItemClick,
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.media_card_corner_radius)))
            .clickable { onClick(item?.id) }
    ) {
        MediaSmall(
            image = item?.coverImage?.extraLarge,
            onClick = { onClick(item?.id) },
            modifier = Modifier.width(dimensionResource(R.dimen.character_card_width))
        )

        Column(Modifier.padding(horizontal = dimensionResource(R.dimen.small_padding))) {
            Text(
                // TODO: Do something about this chain.
                text = item?.title?.romaji ?:
                item?.title?.english ?:
                item?.title?.native.orEmpty(),
                color = MaterialTheme.colorScheme.onBackground,
                // TODO: Why does this not use manrope?
                style = MaterialTheme.typography.labelMedium.copy(
                    letterSpacing = TextUnit.Unspecified
                ),
                maxLines = 2
            )
            if (item?.season?.rawValue != null || item?.seasonYear != null) {
                Text(
                    text = item.season?.string?.plus(" ").orEmpty()
                            + item.seasonYear?.toString().orEmpty(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.size(dimensionResource(R.dimen.medium_padding)))

            if (item?.studios?.nodes?.isEmpty() == false) {
                Text(
                    text = item.studios.nodes.map { it?.name }.joinToString(separator = ", "),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                if (item?.format != null && item.format != MediaFormat.UNKNOWN__) {
                    Text(
                        text = "${item.format.rawValue.replace("_", " ")} ꞏ " ,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = ContentAlpha.medium
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (item?.episodes != null) {
                    Text(
                        text = "${item.episodes} episodes",
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = ContentAlpha.medium
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
