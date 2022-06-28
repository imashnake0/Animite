package com.imashnake.animite.ui.elements.home

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.R
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.ui.state.SearchViewModel
import com.imashnake.animite.ui.theme.NavigationBar
import com.imashnake.animite.ui.theme.Text
import com.imashnake.animite.ui.theme.manropeFamily

// TODO: UX concern: This blocks content sometimes!
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun SearchBar(modifier: Modifier, viewModel: SearchViewModel = viewModel()) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // TODO: Customize this animation.
        AnimatedContent(targetState = isExpanded) { targetExpanded ->
            if (targetExpanded) {
                SearchList(
                    viewModel = hiltViewModel(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(NavigationBar.copy(alpha = 0.95F))
                        .align(Alignment.End)
                        .fillMaxWidth()
                )
            }
        }

        Surface(
            color = NavigationBar,
            onClick = {
                isExpanded = !isExpanded
                viewModel.run {
                    searchAnime("")
                }
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

@ExperimentalMaterial3Api
@Composable
fun CollapsedSearchBar() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.search),
            contentDescription = "Search",
            tint = Text
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ExpandedSearchBar(viewModel: SearchViewModel = viewModel()) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Collapse",
            tint = Text,
            modifier = Modifier.padding(16.dp)
        )

        var text by remember { mutableStateOf("") }
        val focusRequester = FocusRequester()
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            enabled = true,
            value = text,
            onValueChange = { input ->
                text = input
                viewModel.run {
                    searchAnime(input)
                }
            },
            placeholder = {
                Text(
                    text = "Search",
                    color = Text.copy(alpha = 0.5F),
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Medium
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    keyboardController?.show()
                },
            textStyle = TextStyle(
                color = Text,
                fontSize = 16.sp,
                fontFamily = manropeFamily,
                fontWeight = FontWeight.Medium
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = NavigationBar,
                cursorColor = Text,
                unfocusedIndicatorColor = NavigationBar,
                focusedIndicatorColor = Text
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            trailingIcon = {
                IconButton(
                    onClick = {
                        text = ""
                        viewModel.run {
                            searchAnime("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Text,
                        modifier = Modifier
                            .padding(16.dp)
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

@ExperimentalAnimationApi
@Composable
fun SearchList(
    viewModel: SearchViewModel = viewModel(),
    modifier: Modifier
) {
    val searchList = viewModel.uiState.searchList?.media

    // TODO: Animate this.
    if (searchList != null && searchList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(searchList) {
                SearchItem(item = it)
            }
        }
    } else {
        // TODO: Handle errors.
        Log.d("bruh", "bruh")
    }
}

@Composable
private fun SearchItem(item: SearchQuery.Medium?) {
    Text(
        text = item?.title?.romaji ?:
        item?.title?.english ?:
        item?.title?.native ?: "",
        color = Text,
        fontSize = 12.sp,
        maxLines = 1,
        modifier = Modifier
            .padding(11.dp),
        fontFamily = manropeFamily,
        fontWeight = FontWeight.Medium
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewSearchBar() {
    Column(modifier = Modifier.padding(200.dp)) {
        CollapsedSearchBar()
        Spacer(modifier = Modifier.height(20.dp))
        ExpandedSearchBar()
    }
}
