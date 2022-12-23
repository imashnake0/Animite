package com.imashnake.animite.features.searchbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.R
import com.imashnake.animite.core.ui.Icon
import com.imashnake.animite.core.ui.IconButton
import com.imashnake.animite.core.ui.TextField

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchFrontDrop(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.height(dimensionResource(R.dimen.search_bar_height)),
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
