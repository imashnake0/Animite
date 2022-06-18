package com.imashnake.animite.ui.elements.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.R
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.ui.state.SearchViewModel
import com.imashnake.animite.ui.theme.NavigationBar
import com.imashnake.animite.ui.theme.Text
import com.imashnake.animite.ui.theme.manropeFamily

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
            contentDescription = "Close",
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
            placeholder = { Text(text = "Search", fontSize = 16.sp, color = Text) },
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
                IconButton(onClick = { /* TODO: Add action. */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Text,
                        modifier = Modifier.padding(16.dp)
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

@Composable
fun SearchList(
    viewModel: SearchViewModel = viewModel(),
    modifier: Modifier
) {
    val searchList = viewModel.uiState.searchList?.media

    if (searchList != null && searchList.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
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
        text = item?.title?.romaji ?: item?.title?.english ?: item?.title?.native ?: "",
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
