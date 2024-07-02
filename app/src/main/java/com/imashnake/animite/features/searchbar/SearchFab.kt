package com.imashnake.animite.features.searchbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
        shape = CircleShape
    ) {
        AnimatedContent(targetState = isExpanded, label = "expand_search_fab") { targetExpanded ->
            if (targetExpanded) {
                ExpandedSearchBarContent(
                    collapse = {
                        setExpanded(false)
                        onSearched(null)
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
        imageVector = ImageVector.vectorResource(R.drawable.search),
        contentDescription = stringResource(R.string.search),
        modifier = Modifier
            .clickable(onClick = expand)
            .then(modifier)
    )
}

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
        keyboardOptions = KeyboardOptions(autoCorrectEnabled = false, imeAction = ImeAction.Search),
        leadingIcon = {
            IconButton(
                onClick = collapse,
                modifier = Modifier.size(dimensionResource(com.imashnake.animite.core.R.dimen.icon_size))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null
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
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
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

@Composable
fun searchTextFieldColors(
    contentColor: Color = LocalContentColor.current
): TextFieldColors {
    return TextFieldDefaults.colors(
        unfocusedTextColor = contentColor,
        focusedTextColor = contentColor,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        cursorColor = contentColor,
        selectionColors = TextSelectionColors(
            handleColor = contentColor,
            backgroundColor = contentColor.copy(alpha = 0.3f)
        ),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = contentColor,
        unfocusedLeadingIconColor = contentColor,
        focusedTrailingIconColor = contentColor,
        unfocusedTrailingIconColor = contentColor,
        unfocusedPlaceholderColor = contentColor.copy(alpha = 0.5F),
        focusedPlaceholderColor = contentColor.copy(alpha = 0.5F),
    )
}
