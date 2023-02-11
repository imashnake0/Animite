package com.imashnake.animite.features.searchbar

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imashnake.animite.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun SearchFab(
    isExpanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    onSearched: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        shadowElevation = 20.dp,
        shape = CircleShape
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        AnimatedContent(targetState = isExpanded) { targetExpanded ->
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
fun CollapsedSearchBarContent(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedSearchBarContent(
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
                color = LocalContentColor.current.copy(alpha = 0.5F),
                style = MaterialTheme.typography.labelLarge
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            cursorColor = LocalContentColor.current,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            selectionColors = TextSelectionColors(
                handleColor = LocalContentColor.current,
                backgroundColor = LocalContentColor.current.copy(alpha = 0.3f)
            ),
            focusedLeadingIconColor = LocalContentColor.current,
            unfocusedLeadingIconColor = LocalContentColor.current,
            textColor = LocalContentColor.current,
            unfocusedTrailingIconColor = LocalContentColor.current,
            focusedTrailingIconColor = LocalContentColor.current,
            placeholderColor = LocalContentColor.current.copy(alpha = 0.5F)
        ),
        keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Search),
        leadingIcon = {
            IconButton(
                onClick = collapse,
                modifier = Modifier.size(dimensionResource(com.imashnake.animite.core.R.dimen.icon_size))
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
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
