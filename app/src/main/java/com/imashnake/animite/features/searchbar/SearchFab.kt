package com.imashnake.animite.features.searchbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.imashnake.animite.R

/**
 * A Floating Action Button-esque collapsible search bar. When collapsed, it displays a search icon.
 * Expanding it reveals a text field with collapse and clear buttons.
 *
 * @param isExpanded Whether the search FAB is currently "expanded".
 * @param setExpanded Called when [isExpanded] should be updated.
 * @param onSearched Called when the user searches for something. The query may be null, which
 * suggests nothing was entered in the text field.
 * @param modifier [Modifier].
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun SearchFab(
    isExpanded: Boolean,
    setExpanded: (expanded: Boolean) -> Unit,
    onSearched: (query: String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        shadowElevation = 20.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        AnimatedContent(targetState = isExpanded, label = "Animated search bar") { targetExpanded ->
            if (targetExpanded) {
                ExpandedSearchBarContent(
                    collapse = {
                        setExpanded(false)
                        onSearched(null)
                        keyboardController?.hide()
                    },
                    clearText = { onSearched(null) },
                    searchText = { onSearched(it) }
                )
            } else {
                CollapsedSearchBarContent(
                    expand = { setExpanded(true) },
                    modifier = Modifier.padding(dimensionResource(R.dimen.search_bar_icon_padding))
                )
            }
        }
    }
}

@Composable
internal fun CollapsedSearchBarContent(
    expand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Rounded.Search,
        contentDescription = stringResource(R.string.search),
        modifier = Modifier
            .clickable(onClick = expand)
            .then(modifier),
        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExpandedSearchBarContent(
    collapse: () -> Unit,
    clearText: () -> Unit,
    searchText: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
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
        modifier = modifier.focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.labelLarge,
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.labelLarge
            )
        },
        singleLine = true,
        colors = searchTextFieldColors(),
        keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Search),
        leadingIcon = {
            IconButton(
                onClick = collapse,
                modifier = Modifier.size(dimensionResource(com.imashnake.animite.core.R.dimen.icon_size))
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    text = ""
                    clearText()
                },
                modifier = Modifier.size(dimensionResource(com.imashnake.animite.core.R.dimen.icon_size))
            ) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                )
            }
        }
    )

    BackHandler {
        text = ""
        clearText()
        collapse()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchTextFieldColors(
    contentColor: Color = LocalContentColor.current
): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        cursorColor = contentColor,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = contentColor,
            backgroundColor = contentColor.copy(alpha = 0.3f)
        ),
        focusedLeadingIconColor = contentColor,
        unfocusedLeadingIconColor = contentColor,
        unfocusedTrailingIconColor = contentColor,
        focusedTrailingIconColor = contentColor,
    )
}
