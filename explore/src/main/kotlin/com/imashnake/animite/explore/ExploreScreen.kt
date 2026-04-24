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
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
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
import kotlinx.coroutines.launch
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.core.ui.R as coreUiR
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

    val searchQuery by viewModel.searchQuery.collectAsState()

    var isSortDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    val selectedSort by viewModel.selectedSort.collectAsState()
    val isDescending by viewModel.isDescending.collectAsState()

    val season by viewModel.selectedSeason.collectAsState()
    val year by viewModel.selectedYear.collectAsState()

    val chipGenreGroup by viewModel.chipGenreGroup.collectAsState()
    val chipFormatGroup by viewModel.chipFormatGroup.collectAsState()
    val chipStatusGroup by viewModel.chipStatusGroup.collectAsState()

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
                                searchQuery = searchQuery,
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
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                            showFilterBottomSheet = true
                                        }
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
                        chipGenreGroup = chipGenreGroup,
                        season = season,
                        selectSeason = viewModel::setMediaSeason,
                        year = year,
                        onYearChange = viewModel::setMediaYear,
                        yearRange = viewModel.yearRange,
                        chipFormatGroup = chipFormatGroup,
                        chipStatusGroup = chipStatusGroup,
                        includeChipFilter = viewModel::includeFilter,
                        excludeChipFilter = viewModel::excludeFilter,
                        clearChipFilter = viewModel::clearFilter,
                        resetChipFilters = viewModel::resetFilter,
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
    searchQuery: String?,
    onSearch: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val closeIconSize by animateFloatAsState(
        targetValue = if (!searchQuery.isNullOrEmpty()) 1f else 0f
    )

    TextField(
        value = searchQuery.orEmpty(),
        onValueChange = {
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
                    onClick = { onSearch(null) },
                    colors = IconButtonDefaults.iconButtonColors(),
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
                        dimensionResource(coreUiR.dimen.icon_size)
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
                                val order = AnimatedImageVector.animatedVectorResource(
                                    R.drawable.order
                                )
                                Icon(
                                    painter = rememberAnimatedVectorPainter(
                                        animatedImageVector = order,
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
    chipGenreGroup: ExploreViewModel.ChipFilterGroup,
    season: String?,
    selectSeason: (String?) -> Unit,
    year: Int?,
    yearRange: IntRange,
    onYearChange: (Int?) -> Unit,
    chipFormatGroup: ExploreViewModel.ChipFilterGroup,
    chipStatusGroup: ExploreViewModel.ChipFilterGroup,
    includeChipFilter: (ExploreViewModel.ChipFilterType, String) -> Unit,
    excludeChipFilter: (ExploreViewModel.ChipFilterType, String) -> Unit,
    clearChipFilter: (ExploreViewModel.ChipFilterType, String) -> Unit,
    resetChipFilters: (ExploreViewModel.ChipFilterType) -> Unit,
    reset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    BottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp,
        contentPadding = PaddingValues(horizontal = LocalPaddings.current.medium) +
                PaddingValues(bottom = LocalPaddings.current.large),
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
            ChipFilter(
                type = ExploreViewModel.ChipFilterType.GENRE,
                title = stringResource(R.string.genres),
                includedFilters = chipGenreGroup.includedFilters,
                excludedFilters = chipGenreGroup.excludedFilters,
                allFilters = chipGenreGroup.allFilters,
                onIncludedFilterClick = excludeChipFilter,
                onExcludedFilterClick = clearChipFilter,
                onAllFilterClick = includeChipFilter,
                resetFilters = resetChipFilters
            )

            SeasonFilter(
                selectedSeason = season,
                onSeasonSelected = {
                    haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    selectSeason(it)
                },
            )

            YearFilter(
                year = year,
                yearRange = yearRange,
                onYearChange = {
                    haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                    onYearChange(it)
                },
            )

            ChipFilter(
                type = ExploreViewModel.ChipFilterType.FORMAT,
                title = stringResource(R.string.format),
                filterIcon = { Media.Format.safeValueOf(it)?.icon?.let { icon ->
                    ImageVector.vectorResource(icon)
                } },
                filterText = { Media.Format.safeValueOf(it)?.res?.let { text ->
                    stringResource(text)
                } },
                includedFilters = chipFormatGroup.includedFilters,
                excludedFilters = chipFormatGroup.excludedFilters,
                allFilters = chipFormatGroup.allFilters,
                onIncludedFilterClick = excludeChipFilter,
                onExcludedFilterClick = clearChipFilter,
                onAllFilterClick = includeChipFilter,
                resetFilters = resetChipFilters
            )

            ChipFilter(
                type = ExploreViewModel.ChipFilterType.STATUS,
                title = stringResource(R.string.status),
                filterIcon = { Media.Status.safeValueOf(it)?.icon?.let { icon ->
                    ImageVector.vectorResource(icon)
                } },
                filterText = { Media.Status.safeValueOf(it)?.res?.let { text ->
                    stringResource(text)
                } },
                includedFilters = chipStatusGroup.includedFilters,
                excludedFilters = chipStatusGroup.excludedFilters,
                allFilters = chipStatusGroup.allFilters,
                onIncludedFilterClick = excludeChipFilter,
                onExcludedFilterClick = clearChipFilter,
                onAllFilterClick = includeChipFilter,
                resetFilters = resetChipFilters
            )

            Spacer(modifier = Modifier.size(LocalPaddings.current.small))

            // TODO: Not properly horizontally aligned when flowed.
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                val scope = rememberCoroutineScope()
                ResetAllButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.Reject)
                        reset()
                    },
                )
                Spacer(Modifier.size(LocalPaddings.current.medium))
                DoneButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        scope.launch {
                            sheetState.hide()
                            onDismissRequest()
                        }
                    },
                )
            }
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
    CollapsibleRow(
        title = stringResource(R.string.season),
        onLongClick = { onSeasonSelected(null) },
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            modifier = Modifier.padding(top = LocalPaddings.current.medium)
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(season.icon),
                            contentDescription = stringResource(season.res),
                            modifier = Modifier
                                .graphicsLayer { alpha = 0.5f }
                                .height(14.dp)
                        )

                        Text(
                            text = stringResource(season.res),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
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
    onYearChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    CollapsibleRow(
        title = stringResource(R.string.year),
        onLongClick = { onYearChange(null) },
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = LocalPaddings.current.medium)
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
            color = MaterialTheme.colorScheme.outlineVariant
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
private fun ChipFilter(
    type: ExploreViewModel.ChipFilterType,
    title: String?,
    includedFilters: ImmutableList<String>,
    excludedFilters: ImmutableList<String>,
    allFilters: ImmutableList<String>,
    onIncludedFilterClick: (ExploreViewModel.ChipFilterType, String) -> Unit,
    onExcludedFilterClick: (ExploreViewModel.ChipFilterType, String) -> Unit,
    onAllFilterClick: (ExploreViewModel.ChipFilterType, String) -> Unit,
    resetFilters: (ExploreViewModel.ChipFilterType) -> Unit,
    modifier: Modifier = Modifier,
    filterIcon: @Composable ((String) -> ImageVector?)? = null,
    filterText: @Composable ((String) -> String?)? = null,
) {
    val haptic = LocalHapticFeedback.current
    CollapsibleRow(
        title = title,
        onLongClick = { resetFilters(type) },
        modifier = modifier,
    ) {
        Column(Modifier.padding(top = LocalPaddings.current.medium)) {
            ChipFilterFlowRow(
                filters = includedFilters,
                onFilterClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    onIncludedFilterClick(type, it)
                },
                icon = ImageVector.vectorResource(R.drawable.include),
                title = stringResource(R.string.include_genre),
                chipColor = Color(0xFF80DF87),
                transformFilterIcon = filterIcon,
                transformFilterText = filterText,
                modifier = Modifier.padding(bottom = LocalPaddings.current.small)
            )

            ChipFilterFlowRow(
                filters = excludedFilters,
                onFilterClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    onExcludedFilterClick(type, it)
                },
                icon = ImageVector.vectorResource(R.drawable.exclude),
                title = stringResource(R.string.exclude_genre),
                chipColor = Color(0xFFFF9999),
                transformFilterIcon = filterIcon,
                transformFilterText = filterText,
                modifier = Modifier.padding(bottom = LocalPaddings.current.small)
            )

            ChipFilterFlowRow(
                filters = allFilters,
                onFilterClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                    onAllFilterClick(type, it)
                },
                icon = ImageVector.vectorResource(R.drawable.all),
                title = stringResource(R.string.all_genres),
                transformFilterIcon = filterIcon,
                transformFilterText = filterText,
            )
        }
    }
}

@Composable
private fun ChipFilterFlowRow(
    filters: ImmutableList<String>,
    onFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    transformFilterIcon: @Composable ((String) -> ImageVector?)? = null,
    transformFilterText: @Composable ((String) -> String?)? = null,
    icon: ImageVector? = null,
    title: String? = null,
    chipColor: Color= MaterialTheme.colorScheme.tertiary,
) {
    AnimatedContent(filters) {
        if (it.isNotEmpty()) {
            Column {
                FlowFilterGroupHeaders(icon = icon, title = title)

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                    verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                    modifier = modifier.fillMaxWidth()
                ) {
                    it.fastForEach { filter ->
                        Chip(
                            color = chipColor,
                            icon = transformFilterIcon?.invoke(filter),
                            text = transformFilterText?.invoke(filter) ?: filter,
                            onClick = { onFilterClick(filter) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlowFilterGroupHeaders(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
        modifier = modifier.padding(bottom = LocalPaddings.current.tiny)
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
}

// TODO: Maybe move this to core:ui.
@Composable
private fun DoneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.done),
                contentDescription = stringResource(R.string.done),
                modifier = Modifier.size(24.dp)
            )
            Text(stringResource(R.string.done))
        }
    }
}

// TODO: Maybe move this to core:ui.
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

// TODO: Maybe move this to core:ui.
@Composable
private fun CollapsibleRow(
    title: String?,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    var isVisible by rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(LocalPaddings.current.small))
            .combinedClickable(
                onClick = { isVisible = !isVisible },
                onLongClick = onLongClick
            )
            .padding(LocalPaddings.current.small)
    ) {
        title?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium.copy(baselineShift = null),
                )
                val iconRotation by animateFloatAsState(if (isVisible) 0f else -90f)
                Icon(
                    painter = painterResource(R.drawable.drop_down),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .requiredSize(dimensionResource(coreUiR.dimen.icon_size))
                        .graphicsLayer { rotationZ = iconRotation }
                )
            }
        }

        AnimatedVisibility(visible = isVisible) {
            content()
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
