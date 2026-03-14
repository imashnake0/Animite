package com.imashnake.animite.explore

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.media.MediaMediumList
import com.imashnake.animite.media.ext.icon
import com.imashnake.animite.media.ext.res
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.navigation.R as navigationR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    onItemClick: (Int, MediaType, String?) -> Unit,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars
        .exclude(WindowInsets.navigationBars)
        .union(WindowInsets.displayCutout),
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val sort by viewModel.selectedSort.collectAsState()
    val isDescending by viewModel.isDescending.collectAsState()

    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(bottom = dimensionResource(navigationR.dimen.navigation_bar_height))
        else -> PaddingValues(start = dimensionResource(navigationR.dimen.navigation_rail_width))
    }
    val insetAndNavigationPaddingValues = contentWindowInsets.asPaddingValues() + navigationComponentPaddingValues

    val exploreList by viewModel.exploreList.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (exploreList) {
            is Resource.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(insetAndNavigationPaddingValues)
                ) {
                    SearchBar(
                        onSearch = { viewModel.setSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = LocalPaddings.current.large)
                    )

                    MediaMediumList(
                        mediaMediumList = exploreList.data.orEmpty().toImmutableList(),
                        onItemClick = { id, title -> onItemClick(id, MediaType.ANIME, title) },
                        shouldShowRank = true,
                        modifier = Modifier
                            .consumeWindowInsets(
                                insetAndNavigationPaddingValues
                                        + PaddingValues(bottom = LocalPaddings.current.large)
                            )
                            .imePadding(),
                        contentPadding = PaddingValues(
                            LocalPaddings.current.large
                        ) + PaddingValues(bottom = LocalPaddings.current.large)
                    )
                }

                SortFab(
                    sorts = Media.Sort.entries.toImmutableList(),
                    selectedSort = sort,
                    onSortSelected = { viewModel.setMediaSort(it) },
                    isDescending = isDescending,
                    toggleOrder = { viewModel.setIsDescending(!isDescending) },
                    expanded = isDropdownExpanded,
                    setExpanded = { isDropdownExpanded = it },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(insetAndNavigationPaddingValues)
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                        .padding(LocalPaddings.current.large)
                )
            }
            // TODO: Add loading and error states.
            is Resource.Loading -> {}
            is Resource.Error -> {}
        }
    }
}

@Composable
private fun SortFab(
    sorts: ImmutableList<Media.Sort>,
    selectedSort: Media.Sort,
    onSortSelected: (Media.Sort) -> Unit,
    isDescending: Boolean,
    toggleOrder: () -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cascadeState = rememberCascadeState()
    val cornerRadius by animateIntAsState(
        targetValue = if (expanded) 10 else 50,
        label = "corner_radius_animation",
    )
    val haptic = LocalHapticFeedback.current

    Box(modifier) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = cornerRadius,
                bottomEndPercent = 50,
                bottomStartPercent = 50,
            ),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.sort),
                contentDescription = stringResource(R.string.sort),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable { setExpanded(!expanded) }
                    .padding(LocalPaddings.current.medium)
                    .size(LocalPaddings.current.large)
            )
        }
        val cornerRadius = LocalPaddings.current.large + LocalPaddings.current.tiny / 2
        CascadeDropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            state = cascadeState,
            shape = RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = cornerRadius,
                bottomEnd = LocalPaddings.current.small,
                bottomStart = cornerRadius,
            ),
            offset = DpOffset(x = 0.dp, y = LocalPaddings.current.tiny),
        ) {
            sorts.forEach { sort ->
                val backgroundColor by animateColorAsState(
                    targetValue = if (sort == selectedSort)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    else Color.Transparent
                )
                val textColor by animateColorAsState(
                    targetValue = if (sort == selectedSort)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onBackground
                )
                val iconSize by animateDpAsState(
                    targetValue = if (sort == selectedSort)
                        LocalPaddings.current.medium
                    else 0.dp
                )

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(sort.icon),
                                    contentDescription = stringResource(sort.res),
                                    modifier = Modifier.size(iconSize)
                                )
                                Text(stringResource(sort.res))
                            }
                            if (sort == selectedSort) {
                                val expandToCollapse = AnimatedImageVector.animatedVectorResource(
                                    R.drawable.order
                                )
                                Icon(
                                    painter = rememberAnimatedVectorPainter(
                                        animatedImageVector = expandToCollapse,
                                        atEnd = isDescending,
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(iconSize)
                                )
                            }
                        }
                    },
                    leadingIcon = null,
                    colors = MenuDefaults.itemColors(
                        textColor = textColor,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                        if (sort == selectedSort) {
                            toggleOrder()
                        } else {
                            onSortSelected(sort)
                        }
                    },
                    contentPadding = PaddingValues(
                        vertical = LocalPaddings.current.small,
                        horizontal = LocalPaddings.current.medium
                    ),
                    modifier = Modifier
                        .padding(LocalPaddings.current.tiny)
                        .clip(RoundedCornerShape(LocalPaddings.current.large))
                        .background(color = backgroundColor)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    onSearch: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it.ifEmpty { null })
        },
        modifier = modifier.focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.labelLarge,
        placeholder = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.labelLarge
            )
        },
        singleLine = true,
        colors = searchTextFieldColors(),
        keyboardOptions = KeyboardOptions(autoCorrectEnabled = false, imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        leadingIcon = {
            IconButton(
                onClick = {},
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    text = ""
                    onSearch(null)
                },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
                )
            }
        }
    )
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
