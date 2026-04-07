package com.imashnake.animite.explore

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.BottomSheet
import com.imashnake.animite.core.ui.component.Chip
import com.imashnake.animite.core.ui.ext.copy
import com.imashnake.animite.core.ui.ext.horizontalOnly
import com.imashnake.animite.media.MediaMediumList
import com.imashnake.animite.media.ext.icon
import com.imashnake.animite.media.ext.res
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.navigation.R as navigationR

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun ExploreScreen(
    onItemClick: (Int, MediaType, String?) -> Unit,
    listState: LazyListState,
    deviceScreenCornerRadius: Int,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(bottom = dimensionResource(navigationR.dimen.navigation_bar_height))
        else -> PaddingValues(start = dimensionResource(navigationR.dimen.navigation_rail_width))
    }
    val insetAndNavigationPaddingValues = contentWindowInsets.asPaddingValues() + navigationComponentPaddingValues

    val exploreList by viewModel.exploreList.collectAsState()

    var showFilterBottomSheet by rememberSaveable { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val deviceScreenCornerRadiusDp = with(LocalDensity.current) {
        deviceScreenCornerRadius.toDp()
    }

    val haptic = LocalHapticFeedback.current

    var isSortDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    val selectedSort by viewModel.selectedSort.collectAsState()
    val isDescending by viewModel.isDescending.collectAsState()

    val season by viewModel.selectedSeason.collectAsState()
    val year by viewModel.selectedYear.collectAsState()

    val allGenres by viewModel.allGenres.collectAsState()
    val includedGenres by viewModel.includedGenres.collectAsState()
    val excludedGenres by viewModel.excludedGenres.collectAsState()

    val allFormats by viewModel.allFormats.collectAsState()
    val includedFormats by viewModel.includedFormats.collectAsState()
    val excludedFormats by viewModel.excludedFormats.collectAsState()

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

                Box {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            LocalPaddings.current.small + LocalPaddings.current.tiny
                        ),
                        modifier = Modifier
                            .background(barBackgroundColor)
                            .padding(insetAndNavigationPaddingValues.copy(bottom = 0.dp))
                            .fillMaxWidth()
                            .padding(vertical = LocalPaddings.current.small)
                            .zIndex(Float.MAX_VALUE)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = LocalPaddings.current.large)
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

                        Sort(
                            sorts = Media.Sort.entries.toImmutableList(),
                            selectedSort = selectedSort,
                            onSortSelected = { viewModel.setMediaSort(it) },
                            isDescending = isDescending,
                            toggleOrder = { viewModel.setIsDescending(!isDescending) },
                            expanded = isSortDropdownExpanded,
                            setExpanded = {
                                haptic.performHapticFeedback(
                                    if (it) {
                                        HapticFeedbackType.ToggleOn
                                    } else HapticFeedbackType.ToggleOff
                                )
                                isSortDropdownExpanded = it
                            },
                            insetAndNavigationPaddingValues = insetAndNavigationPaddingValues,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // TODO: Add filter chips here.
                    }

                    Column(Modifier.padding(insetAndNavigationPaddingValues.horizontalOnly)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.size(11.dp))
                            Text(
                                text = "",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
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
                                top = insetAndNavigationPaddingValues.calculateTopPadding()
                                        + TextFieldDefaults.MinHeight
                                        + 2 * LocalPaddings.current.small,
                                bottom =  insetAndNavigationPaddingValues.calculateBottomPadding()
                            )
                        )
                    }
                }

                if (showFilterBottomSheet) {
                    FilterBottomSheet(
                        sheetState = filterSheetState,
                        onDismissRequest = { showFilterBottomSheet = false },
                        deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp,
                        allGenres = allGenres.orEmpty().sorted().toImmutableList(),
                        includedGenres = includedGenres.sorted().toImmutableList(),
                        excludedGenres = excludedGenres.sorted().toImmutableList(),
                        includeGenre = viewModel::includeMediaGenre,
                        excludeGenre = viewModel::excludeMediaGenre,
                        clearGenre = viewModel::clearMediaGenre,
                        resetGenres = viewModel::resetGenres,
                        season = season,
                        selectSeason = viewModel::setMediaSeason,
                        year = year,
                        onYearChange = viewModel::setMediaYear,
                        yearRange = viewModel.yearRange,
                        allFormats = allFormats.orEmpty().sorted().toImmutableList(),
                        includedFormats = includedFormats.sorted().toImmutableList(),
                        excludedFormats = excludedFormats.sorted().toImmutableList(),
                        includeFormat = viewModel::includeMediaFormat,
                        excludeFormat = viewModel::excludeMediaFormat,
                        clearFormat = viewModel::clearMediaFormat,
                        resetFormats = viewModel::resetFormats,
                        reset = viewModel::reset,
                        modifier = Modifier.padding(insetAndNavigationPaddingValues.horizontalOnly)
                    )
                }
            }
            // TODO: Add loading and error states.
            is Resource.Loading -> {}
            is Resource.Error -> {}
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun SearchBar(
    onSearch: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val closeIconSize by animateFloatAsState(
        targetValue = if (text.isNotEmpty()) 1f else 0f
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
                text = stringResource(R.string.search),
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
                        modifier = Modifier
                            .size(40.dp)
                            .graphicsLayer {
                                scaleX = closeIconSize
                                scaleY = closeIconSize
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun Sort(
    sorts: ImmutableList<Media.Sort>,
    selectedSort: Media.Sort,
    onSortSelected: (Media.Sort) -> Unit,
    isDescending: Boolean,
    toggleOrder: () -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    insetAndNavigationPaddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val cascadeState = rememberCascadeState()
    val haptic = LocalHapticFeedback.current

    Box(modifier) {
        AnimatedContent(
            targetState = selectedSort,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { setExpanded(!expanded) }
                    .padding(
                        vertical = LocalPaddings.current.tiny,
                        horizontal = LocalPaddings.current.small
                    )
                    .graphicsLayer {
                        alpha = 0.5f
                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(it.icon),
                    contentDescription = null,
                    modifier = Modifier.size(11.dp)
                )
                Text(
                    text = stringResource(it.res),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )

                val expandToCollapse = AnimatedImageVector.animatedVectorResource(
                    R.drawable.order
                )
                Icon(
                    painter = rememberAnimatedVectorPainter(
                        animatedImageVector = expandToCollapse,
                        atEnd = isDescending,
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        val screenDpSize = LocalWindowInfo.current.containerDpSize
        val layoutDirection = LocalLayoutDirection.current
        val offsetX = (
                screenDpSize.width
                        - insetAndNavigationPaddingValues.calculateStartPadding(layoutDirection)
                        - insetAndNavigationPaddingValues.calculateEndPadding(layoutDirection)
                        - 196.dp
        ) / 2
        val cornerRadius = LocalPaddings.current.large + LocalPaddings.current.tiny / 2
        CascadeDropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            state = cascadeState,
            offset = DpOffset(x = offsetX, y = LocalPaddings.current.tiny),
            shape = RoundedCornerShape(cornerRadius),
        ) {
            Text(
                text = stringResource(R.string.sort),
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(top = LocalPaddings.current.small)
                    .align(Alignment.CenterHorizontally)
            )
            sorts.fastForEach { sort ->
                val isSortSelected = sort == selectedSort
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSortSelected)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    else Color.Transparent
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSortSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onBackground
                )
                val iconSize by animateDpAsState(
                    targetValue = if (isSortSelected)
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
                            if (isSortSelected) {
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
                        if (isSortSelected) {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun FilterBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    deviceScreenCornerRadiusDp: Dp,
    allGenres: ImmutableList<String>,
    includedGenres: ImmutableList<String>,
    excludedGenres: ImmutableList<String>,
    includeGenre: (String) -> Unit,
    excludeGenre: (String) -> Unit,
    clearGenre: (String) -> Unit,
    resetGenres: () -> Unit,
    season: String?,
    selectSeason: (String?) -> Unit,
    year: Int?,
    yearRange: IntRange,
    onYearChange: (Int?) -> Unit,
    allFormats: ImmutableList<String>,
    includedFormats: ImmutableList<String>,
    excludedFormats: ImmutableList<String>,
    includeFormat: (String) -> Unit,
    excludeFormat: (String) -> Unit,
    clearFormat: (String) -> Unit,
    resetFormats: () -> Unit,
    reset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    BottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp,
        contentPadding = PaddingValues(horizontal = LocalPaddings.current.medium),
        modifier = modifier,
    ) { paddingValues, modifier ->
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            modifier = modifier
                .background(
                    Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.surfaceContainerHighest,
                        1f to MaterialTheme.colorScheme.background
                    )
                )
                .padding(paddingValues)
        ) {
            GenresFilter(
                includedGenres = includedGenres,
                excludedGenres = excludedGenres,
                allGenres = allGenres,
                onIncludedGenreClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    excludeGenre(it)
                },
                onExcludedGenreClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    clearGenre(it)
                },
                onAllGenreClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                    includeGenre(it)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(LocalPaddings.current.small))
                    .combinedClickable(onClick = {}, onLongClick = resetGenres)
                    .padding(LocalPaddings.current.small)
            )

            SeasonFilter(
                selectedSeason = season,
                onSeasonSelected = {
                    haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    selectSeason(it)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(LocalPaddings.current.small))
                    .combinedClickable(onClick = {}, onLongClick = { selectSeason(null) })
                    .padding(LocalPaddings.current.small)
            )

            YearFilter(
                year = year,
                yearRange = yearRange,
                onYearChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onYearChange(it)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(LocalPaddings.current.small))
                    .combinedClickable(onClick = {}, onLongClick = { onYearChange(null) })
                    .padding(LocalPaddings.current.small)
            )

            FormatFilter(
                includedFormats = includedFormats,
                excludedFormats = excludedFormats,
                allFormats = allFormats,
                onIncludedFormatClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    excludeFormat(it)
                },
                onExcludedFormatClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    clearFormat(it)
                },
                onAllFormatClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                    includeFormat(it)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(LocalPaddings.current.small))
                    .combinedClickable(onClick = {}, onLongClick = resetFormats)
                    .padding(LocalPaddings.current.small)
            )

            Spacer(modifier = Modifier.size(LocalPaddings.current.small))

            ResetAllButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                    reset()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun GenresFilter(
    includedGenres: ImmutableList<String>,
    excludedGenres: ImmutableList<String>,
    allGenres: ImmutableList<String>,
    onIncludedGenreClick: (String) -> Unit,
    onExcludedGenreClick: (String) -> Unit,
    onAllGenreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.genres),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(baselineShift = null),
        )

        Column {
            FlowFilter(
                filters = includedGenres,
                onFilterClick = onIncludedGenreClick,
                icon = ImageVector.vectorResource(R.drawable.include_genre),
                title = stringResource(R.string.include_genre),
                chipColor = Color(0xFF80DF87),
                modifier = Modifier.padding(bottom = LocalPaddings.current.small)
            )

            FlowFilter(
                filters = excludedGenres,
                onFilterClick = onExcludedGenreClick,
                icon = ImageVector.vectorResource(R.drawable.exclude_genre),
                title = stringResource(R.string.exclude_genre),
                chipColor = Color(0xFFFF9999),
                modifier = Modifier.padding(bottom = LocalPaddings.current.small)
            )

            FlowFilter(
                filters = allGenres,
                onFilterClick = onAllGenreClick,
                icon = ImageVector.vectorResource(R.drawable.all_genres),
                title = stringResource(R.string.all_genres),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SeasonFilter(
    selectedSeason: String?,
    onSeasonSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.season),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(baselineShift = null),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
        ) {
            Media.Season.entries.fastForEach { season ->
                ToggleButton(
                    checked = selectedSeason?.let { Media.Season.safeValueOf(it) } == season,
                    onCheckedChange = { onSeasonSelected(season.name) },
                    shapes = when (season) {
                        Media.Season.WINTER -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                        Media.Season.FALL -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                    },
                    colors = ToggleButtonDefaults.tonalToggleButtonColors(),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)
                    ) {
                        var showLabel by remember { mutableStateOf(false) }
                        Icon(
                            imageVector = ImageVector.vectorResource(season.icon),
                            contentDescription = stringResource(season.res),
                            modifier = Modifier
                                .graphicsLayer { alpha = if (showLabel) 0.5f else 1f }
                                .height(14.dp)
                                .align(Alignment.CenterVertically)
                        )

                        if (showLabel) {
                            Text(
                                text = stringResource(season.res),
                                onTextLayout = {
                                    if (it.hasVisualOverflow) showLabel = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun YearFilter(
    year: Int?,
    yearRange: IntRange,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.year),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(baselineShift = null),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            val screenDpSize = LocalWindowInfo.current.containerDpSize
            val yearItemSize = if (screenDpSize.width > (56 * 5).dp) {
                56.dp
            } else screenDpSize.width / 5

            val firstYear = yearRange.first()
            val lastYear = yearRange.last()
            val yearCount = yearRange.count()

            AnimatedVisibility(year != null) {
                Column {
                    Box(contentAlignment = Alignment.Center) {
                        Box(contentAlignment = Alignment.Center) {
                            var shortenYear by remember { mutableStateOf(false) }
                            YearOutline(shortenYear)

                            val listState = rememberLazyListState()
                            LaunchedEffect(year) {
                                year?.let {
                                    listState.animateScrollToItem(year - firstYear)
                                }
                            }
                            LazyRow(
                                state = listState,
                                contentPadding = PaddingValues(horizontal = yearItemSize * 2f),
                                userScrollEnabled = false,
                                modifier = Modifier.requiredWidth(yearItemSize * 5),
                            ) {
                                items(yearCount) {
                                    val currentYear = firstYear + it
                                    val textAlpha by animateFloatAsState(
                                        if (currentYear == year) 1f else 0.5f
                                    )
                                    Box(Modifier.requiredSize(yearItemSize)) {
                                        val text = if (shortenYear) {
                                            "'${currentYear.toString().takeLast(2)}"
                                        } else "$currentYear"
                                        Text(
                                            text = text,
                                            maxLines = 1,
                                            color = MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = textAlpha
                                            ),
                                            onTextLayout = { result ->
                                                if (result.hasVisualOverflow) shortenYear = true
                                            },
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        }
                        Button(
                            enabled = (year ?: 0) > firstYear,
                            onClick = { onYearChange((year ?: 0) - 1) },
                            contentPadding = PaddingValues(),
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.step_year_left),
                                contentDescription = stringResource(R.string.increase_year),
                                modifier = Modifier.requiredSize(24.dp)
                            )
                        }
                        Button(
                            enabled = (year ?: 0) < lastYear,
                            onClick = { onYearChange((year ?: 0) + 1) },
                            contentPadding = PaddingValues(),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.step_year_right),
                                contentDescription = stringResource(R.string.decrease_year),
                                modifier = Modifier.requiredSize(24.dp)
                            )
                        }
                    }
                    Spacer(Modifier.size(LocalPaddings.current.small))
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = firstYear.toString(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )
                Slider(
                    value = year?.toFloat() ?: 0f,
                    onValueChange = { onYearChange(it.toInt()) },
                    colors = SliderDefaults.colors(
                        activeTrackColor = SliderDefaults.colors().inactiveTrackColor,
                        inactiveTickColor = Color.Transparent,
                        activeTickColor = Color.Transparent
                    ),
                    steps = yearCount,
                    valueRange = firstYear.toFloat()..lastYear.toFloat(),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = lastYear.toString(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun YearOutline(
    shortenYear: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.border(
            width = 2.dp,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.tertiary
        )
    ) {
        // Fake year field
        Text(
            text = if (shortenYear) "000" else "0000",
            color = Color.Transparent,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(LocalPaddings.current.small)
        )
    }
}

@Composable
private fun FormatFilter(
    includedFormats: ImmutableList<String>,
    excludedFormats: ImmutableList<String>,
    allFormats: ImmutableList<String>,
    onIncludedFormatClick: (String) -> Unit,
    onExcludedFormatClick: (String) -> Unit,
    onAllFormatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.format),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium.copy(baselineShift = null),
        )

        Column {
            FlowFilter(
                filters = includedFormats,
                onFilterClick = onIncludedFormatClick,
                icon = ImageVector.vectorResource(R.drawable.include_genre),
                title = stringResource(R.string.include_genre),
                chipColor = Color(0xFF80DF87),
                transformFilterText = { stringResource(Media.Format.valueOf(it).res) },
                modifier = Modifier.padding(bottom = LocalPaddings.current.small)
            )

            FlowFilter(
                filters = excludedFormats,
                onFilterClick = onExcludedFormatClick,
                icon = ImageVector.vectorResource(R.drawable.exclude_genre),
                title = stringResource(R.string.exclude_genre),
                chipColor = Color(0xFFFF9999),
                transformFilterText = { stringResource(Media.Format.valueOf(it).res) },
                modifier = Modifier.padding(bottom = LocalPaddings.current.small)
            )

            FlowFilter(
                filters = allFormats,
                onFilterClick = onAllFormatClick,
                icon = ImageVector.vectorResource(R.drawable.all_genres),
                title = stringResource(R.string.all_genres),
                transformFilterText = { stringResource(Media.Format.valueOf(it).res) },
            )
        }
    }
}

@Composable
private fun FlowFilter(
    filters: ImmutableList<String>,
    onFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    transformFilterText: @Composable ((String) -> String)? = null,
    icon: ImageVector? = null,
    title: String? = null,
    chipColor: Color= MaterialTheme.colorScheme.tertiary,
) {
    AnimatedContent(filters) {
        if (it.isNotEmpty()) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                    modifier = Modifier.padding(bottom = LocalPaddings.current.tiny)
                ) {
                    icon?.let {
                        Icon(
                            imageVector = icon,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            contentDescription = null,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    title?.let {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                    verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                    modifier = modifier.fillMaxWidth()
                ) {
                    it.fastForEach { filter ->
                        Chip(
                            color = chipColor,
                            icon = null,
                            text = transformFilterText?.invoke(filter) ?: filter,
                            modifier = Modifier.clickable { onFilterClick(filter) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResetAllButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.reset),
                contentDescription = stringResource(R.string.reset_all),
                modifier = Modifier.size(24.dp)
            )
            Text(stringResource(R.string.reset_all))
        }
    }
}

@Composable
private fun searchTextFieldColors(
    contentColor: Color = LocalContentColor.current
) = TextFieldDefaults.colors(
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
