package com.imashnake.animite.features.searchbar

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.imashnake.animite.R
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.features.appCurrentDestinationAsState
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.navigationbar.NavigationBarPaths
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.navigation.navigate
import com.imashnake.animite.R as Res

// TODO:
//  - UX concern: This blocks content sometimes!
//  - `SearchList` goes beyond the status bar.
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Search(
    modifier: Modifier,
    viewModel: SearchViewModel = viewModel(),
    navController: NavHostController
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isMaskVisible by remember { mutableStateOf(false) }
    val maskAlpha: Float by animateFloatAsState(
        targetValue = if (isExpanded) 0.95f else 0f,
        animationSpec = tween(500),
        // Mask waits for the alpha to decrease.
        finishedListener = {
            isMaskVisible = false
        }
    )

    // TODO: This hack still results in a very slight jitter (still an improvement); remove this
    //  after we start using custom `Scaffold`s.
    val searchBarBottomPadding: Dp by animateDpAsState(
        targetValue = dimensionResource(R.dimen.large_padding) + if (
            NavigationBarPaths.values().any {
                it.direction == navController.appCurrentDestinationAsState().value
            } && !isExpanded
        ) dimensionResource(R.dimen.navigation_bar_height) else 0.dp,
        animationSpec = tween(delayMillis = 100)
    )

    if(isExpanded || isMaskVisible) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = maskAlpha))
                .fillMaxSize()
        )
    }

    Column(
        modifier = modifier
            .padding(bottom = searchBarBottomPadding)
            .navigationBarsPadding()
            .imePadding()
    ) {
        if(isExpanded) {
            SearchList(
                viewModel = hiltViewModel(),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    isExpanded = false
                    // TODO: Double clicking makes the navigation happen twice.
                    navController.navigate(
                        MediaPageDestination(
                            id = it,
                            mediaTypeArg = MediaType.ANIME.rawValue
                        )
                    )
                }
            )
        }

        Surface(
            color = MaterialTheme.colorScheme.primary,
            onClick = {
                isExpanded = !isExpanded
                isMaskVisible = !isMaskVisible
                viewModel.clearList()
            },
            modifier = Modifier
                .align(Alignment.End)
                .wrapContentSize(),
            shadowElevation = 20.dp,
            shape = CircleShape
        ) {
            // TODO: Customize this animation.
            AnimatedContent(targetState = isExpanded) { targetExpanded ->
                if (targetExpanded) {
                    ExpandedSearchBar(hiltViewModel())
                } else {
                    CollapsedSearchBar()
                }
            }
        }
    }
}

@Composable
fun CollapsedSearchBar() {
    Row(
        modifier = Modifier.padding(dimensionResource(Res.dimen.search_bar_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = Res.drawable.search),
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExpandedSearchBar(viewModel: SearchViewModel = viewModel()) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = stringResource(Res.string.collapse),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(dimensionResource(Res.dimen.search_bar_padding))
        )

        var text by remember { mutableStateOf("") }
        val focusRequester = FocusRequester()
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            enabled = true,
            value = text,
            onValueChange = { input ->
                text = input
                viewModel.searchAnime(input)
            },
            placeholder = {
                Text(
                    text = stringResource(Res.string.search),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5F),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    keyboardController?.show()
                },
            textStyle = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            trailingIcon = {
                IconButton(
                    onClick = {
                        text = ""
                        viewModel.clearList()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(Res.string.close),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            dimensionResource(Res.dimen.search_bar_padding)
                        )
                    )
                }
            }
        )
        // TODO: How does this work?
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchList(
    viewModel: SearchViewModel = viewModel(),
    modifier: Modifier,
    onClick: (Int?) -> Unit
) {
    val searchList = viewModel.uiState.searchList?.media

    // TODO: Improve this animation.
    if (!searchList.isNullOrEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(searchList, key = { it!!.id }) {
                SearchItem(
                    item = it,
                    onClick = onClick,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    } else {
        // TODO: Handle errors.
        Log.d("bruh", "bruh")
    }
}

@Composable
private fun SearchItem(item: SearchQuery.Medium?, onClick: (Int?) -> Unit, modifier: Modifier) {
    Text(
        // TODO: Do something about this chain.
        text = item?.title?.romaji ?:
        item?.title?.english ?:
        item?.title?.native.orEmpty(),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelLarge,
        maxLines = 1,
        modifier = modifier
            .clickable {
                onClick(item?.id)
            }
            .padding(dimensionResource(Res.dimen.search_list_padding))
            .fillMaxSize()
    )
}

@Preview
@Composable
fun PreviewSearchBar() {
    Column(modifier = Modifier.padding(200.dp)) {
        CollapsedSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        ExpandedSearchBar()
    }
}
