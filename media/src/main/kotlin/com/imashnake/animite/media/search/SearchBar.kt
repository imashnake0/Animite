package com.imashnake.animite.media.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AdaptiveSearchBar(
    windowSizeClass: WindowSizeClass,
    searchTextState: TextFieldState,
    modifier: Modifier = Modifier,
    searchContent: @Composable ColumnScope.() -> Unit,
) {
    val canFitDockedSearch = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium
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
