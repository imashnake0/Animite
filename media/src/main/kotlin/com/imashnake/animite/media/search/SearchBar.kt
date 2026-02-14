package com.imashnake.animite.media.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.imashnake.animite.media.MediaMediumList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@Composable
fun MediaSearchBar(
    mediaType: MediaType,
    onItemClick: (Media.Medium) -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchResults by viewModel.searchList.collectAsStateWithLifecycle()
    val searchTextState = rememberTextFieldState()

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
        modifier = modifier,
    ) {
        AnimatedContent(
            targetState = searchResults
        ) {
            when (it) {
                is Resource.Error -> {
                    Text(it.message ?: "Error")
                }
                is Resource.Loading -> {
                    Text("Loading")
                }
                is Resource.Success -> {
                    MediaMediumList(
                        mediaMediumList = it.data,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
fun AdaptiveSearchBar(
    windowSizeClass: WindowSizeClass,
    searchTextState: TextFieldState,
    modifier: Modifier = Modifier,
    searchContent: @Composable ColumnScope.() -> Unit,
) {
    val canFitDockedSearch = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    if (canFitDockedSearch) {
        DockedSearchBar(
            searchTextState = searchTextState,
            modifier = modifier,
            searchContent = searchContent
        )
    } else {
        FullscreenSearchBar(
            searchTextState = searchTextState,
            modifier = modifier,
            searchContent = searchContent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DockedSearchBar(
    searchTextState: TextFieldState,
    modifier: Modifier = Modifier,
    searchContent: @Composable ColumnScope.() -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                state = searchTextState,
                onSearch = { },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text("Search")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, null)
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
        content = searchContent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FullscreenSearchBar(
    searchTextState: TextFieldState,
    modifier: Modifier = Modifier,
    searchContent: @Composable ColumnScope.() -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                state = searchTextState,
                onSearch = { },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text("Search")
                },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, null)
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
        content = searchContent,
    )
}

@Preview
@Composable
internal fun DockedSearchBarPreview() {
    val searchState = rememberTextFieldState()
    MaterialTheme {
        Scaffold {
            Box(Modifier.fillMaxSize()) {
                DockedSearchBar(
                    searchTextState = searchState,
                    modifier = Modifier.padding(it).align(Alignment.Center)
                ) {
                    Text("Search results here")
                }
            }
        }
    }
}

@Preview
@Composable
internal fun FullscreenSearchBarPreview() {
    val searchState = rememberTextFieldState()
    MaterialTheme {
        Scaffold {
            Box(Modifier.fillMaxSize()) {
                FullscreenSearchBar(
                    searchTextState = searchState,
                    modifier = Modifier.padding(it).align(Alignment.Center)
                ) {
                    Text("Search results here")
                }
            }
        }
    }
}
