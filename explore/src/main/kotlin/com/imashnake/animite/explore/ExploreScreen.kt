package com.imashnake.animite.explore

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.BottomSheet
import com.imashnake.animite.core.ui.ext.copy
import com.imashnake.animite.media.MediaMediumList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import com.imashnake.animite.navigation.R as navigationR

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ExploreScreen(
    onItemClick: (Int, MediaType, String?) -> Unit,
    listState: LazyListState,
    deviceScreenCornerRadius: Int,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars
        .exclude(WindowInsets.navigationBars)
        .union(WindowInsets.displayCutout),
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(bottom = dimensionResource(navigationR.dimen.navigation_bar_height))
        else -> PaddingValues(start = dimensionResource(navigationR.dimen.navigation_rail_width))
    }
    val insetAndNavigationPaddingValues = contentWindowInsets.asPaddingValues() + navigationComponentPaddingValues

    val exploreList by viewModel.exploreList.collectAsState()

    val fabSize = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(200)
        fabSize.animateTo(1f)
    }

    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val deviceScreenCornerRadiusDp = with(LocalDensity.current) {
        deviceScreenCornerRadius.toDp()
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (exploreList) {
            is Resource.Success -> {
                val isNotAtTop by remember {
                    derivedStateOf {
                        listState.firstVisibleItemScrollOffset != 0
                                || listState.firstVisibleItemIndex != 0
                    }
                }
                val barBackgroundColor by animateColorAsState(
                    targetValue = if (!isNotAtTop) {
                        MaterialTheme.colorScheme.background
                    } else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.9f),
                    animationSpec = tween(500)
                )

                Box(modifier = Modifier.padding(insetAndNavigationPaddingValues.copy(top = 0.dp))) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(barBackgroundColor)
                            .padding(top = insetAndNavigationPaddingValues.calculateTopPadding())
                            .fillMaxWidth()
                            .padding(
                                horizontal = LocalPaddings.current.large,
                                vertical = LocalPaddings.current.small
                            )
                            .zIndex(Float.MAX_VALUE)
                            .height(IntrinsicSize.Max)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        SearchBar(
                            onSearch = { viewModel.setSearchQuery(it) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .padding(horizontal = LocalPaddings.current.small)
                                .weight(1f)
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.tune),
                                contentDescription = stringResource(R.string.tune),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .clickable { showFilterBottomSheet = true }
                                    .padding(8.dp)
                                    .size(24.dp)
                            )
                        }
                    }

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
                        state = listState,
                        contentPadding = PaddingValues(
                            top = LocalPaddings.current.medium,
                            bottom = LocalPaddings.current.large,
                            start = LocalPaddings.current.large,
                            end = LocalPaddings.current.large,
                        ) + PaddingValues(
                            bottom = LocalPaddings.current.large
                        ) + PaddingValues(
                            top = insetAndNavigationPaddingValues.calculateTopPadding()
                                    + TextFieldDefaults.MinHeight
                                    + 2 * LocalPaddings.current.small
                        )
                    )
                }

                if (showFilterBottomSheet) {
                    BottomSheet(
                        sheetState = filterSheetState,
                        onDismissRequest = { showFilterBottomSheet = false },
                        deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp,
                    ) { paddingValues, modifier ->
                        Column(modifier) {

                        }
                    }
                }
            }
            // TODO: Add loading and error states.
            is Resource.Loading -> {}
            is Resource.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun SearchBar(
    onSearch: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val closeIconSize by animateDpAsState(
        targetValue = if (text.isNotEmpty()) 20.dp else 0.dp
    )

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
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            Row {
                CompositionLocalProvider(
                    LocalRippleConfiguration provides RippleConfiguration(
                        rippleAlpha = RippleAlpha(
                            draggedAlpha = 0f,
                            focusedAlpha = 0f,
                            hoveredAlpha = 0f,
                            pressedAlpha = 0f
                        )
                    )
                ) {
                    IconButton(
                        onClick = {
                            text = ""
                            onSearch(null)
                        },
                        colors = IconButtonDefaults.iconButtonColors(),
                        enabled = text.isNotEmpty(),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(closeIconSize)
                        )
                    }
                }
                IconButton(
                    onClick = { keyboardController?.hide() },
                    enabled = WindowInsets.isImeVisible,
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.hide_keyboard),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
        unfocusedPlaceholderColor = contentColor.copy(alpha = 0.5f),
        focusedPlaceholderColor = contentColor.copy(alpha = 0.5f),
    )
}
