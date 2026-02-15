package com.imashnake.animite.media.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.ExpandedFullScreenContainedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaSearchBar(
    mediaType: MediaType,
    onItemClick: (Media.Medium) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val searchResults by viewModel.searchList.collectAsStateWithLifecycle()
    val searchTextState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(mediaType, searchTextState) {
        snapshotFlow { searchTextState.text }
            .debounce(300.milliseconds)
            .collect {
                viewModel.setQuery(mediaType, it.toString())
            }
    }

    AdaptiveSearchBar(
        windowSizeClass = windowSizeClass,
        searchTextState = searchTextState,
        searchBarState = searchBarState,
        onSearch = { viewModel.setQuery(mediaType, it) },
        modifier = modifier,
    ) {
        SearchResults(
            items = searchResults.data.orEmpty(),
            onItemClick = {
                coroutineScope.launch {
                    searchBarState.animateToCollapsed()
                }
                onItemClick(it)
            },
            loading = searchResults is Resource.Loading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AdaptiveSearchBar(
    windowSizeClass: WindowSizeClass,
    searchTextState: TextFieldState,
    searchBarState: SearchBarState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchContent: @Composable ColumnScope.() -> Unit,
) {
    val canFitDockedSearch = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    SearchBar(
        state = searchBarState,
        inputField = {
            SearchInputField(
                searchTextState = searchTextState,
                searchBarState = searchBarState,
                onSearch = onSearch
            )
        },
        modifier = modifier,
    )
    if (canFitDockedSearch) {
        ExpandedDockedSearchBarWithGap(
            inputField = {
                SearchInputField(
                    searchTextState = searchTextState,
                    searchBarState = searchBarState,
                    onSearch = onSearch
                )
            },
            state = searchBarState,
            content = searchContent,
        )
    } else {
        ExpandedFullScreenContainedSearchBar(
            state = searchBarState,
            inputField = {
                SearchInputField(
                    searchTextState = searchTextState,
                    searchBarState = searchBarState,
                    onSearch = onSearch
                )
            },
            content = searchContent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchInputField(
    searchTextState: TextFieldState,
    searchBarState: SearchBarState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SearchBarDefaults.InputField(
        textFieldState = searchTextState,
        onSearch = onSearch,
        searchBarState = searchBarState,
        placeholder = {
            Text("Search")
        },
        leadingIcon = {
            Icon(Icons.Rounded.Search, null)
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun DockedSearchBarPreview() {
    val searchState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    MaterialTheme {
        Scaffold {
            Box(Modifier.fillMaxSize()) {
                AdaptiveSearchBar(
                    windowSizeClass = WindowSizeClass(600, 600),
                    searchTextState = searchState,
                    searchBarState = searchBarState,
                    onSearch = {},
                    modifier = Modifier.padding(it).align(Alignment.Center)
                ) {
                    Text("Search results here")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun FullscreenSearchBarPreview() {
    val searchState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    MaterialTheme {
        Scaffold {
            Box(Modifier.fillMaxSize()) {
                AdaptiveSearchBar(
                    windowSizeClass = WindowSizeClass(300, 300),
                    searchTextState = searchState,
                    searchBarState = searchBarState,
                    onSearch = {},
                    modifier = Modifier.padding(it).align(Alignment.Center)
                ) {
                    Text("Search results here")
                }
            }
        }
    }
}
