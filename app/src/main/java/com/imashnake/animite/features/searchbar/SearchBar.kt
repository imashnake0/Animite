package com.imashnake.animite.features.searchbar

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavHostController
import com.imashnake.animite.R
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.theme.NavigationBar
import com.imashnake.animite.features.theme.Text
import com.imashnake.animite.features.theme.manropeFamily
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.navigation.navigate

// TODO: UX concern: This blocks content sometimes!
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun SearchBar(modifier: Modifier, viewModel: SearchViewModel = viewModel(), navController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // TODO: Customize this animation.
        AnimatedContent(targetState = isExpanded) { targetExpanded ->
            if (targetExpanded) {
                SearchList(
                    viewModel = hiltViewModel(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95F))
                        .align(Alignment.End)
                        .fillMaxWidth(),
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
        }

        Surface(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            tint = MaterialTheme.colorScheme.primaryContainer
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
            tint = MaterialTheme.colorScheme.primaryContainer,
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
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F),
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
                color = MaterialTheme.colorScheme.primaryContainer,
                fontSize = 16.sp,
                fontFamily = manropeFamily,
                fontWeight = FontWeight.Medium
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedIndicatorColor = Color.Transparent
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
                        tint = MaterialTheme.colorScheme.primaryContainer,
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

@ExperimentalAnimationApi
@Composable
fun SearchList(
    viewModel: SearchViewModel = viewModel(),
    modifier: Modifier,
    onClick: (Int?) -> Unit
) {
    val searchList = viewModel.uiState.searchList?.media

    // TODO: Animate this.
    if (searchList != null && searchList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(searchList) {
                SearchItem(
                    item = it,
                    onClick = onClick
                )
            }
        }
    } else {
        // TODO: Handle errors.
        Log.d("bruh", "bruh")
    }
}

@Composable
private fun SearchItem(item: SearchQuery.Medium?, onClick: (Int?) -> Unit) {
    Text(
        text = item?.title?.romaji ?:
        item?.title?.english ?:
        item?.title?.native ?: "",
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontSize = 12.sp,
        maxLines = 1,
        modifier = Modifier
            .padding(11.dp)
            .fillMaxSize()
            .clickable {
                onClick(item?.id)
            },
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
