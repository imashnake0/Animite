package com.imashnake.animite.explore

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.media.MediaMediumList
import com.imashnake.animite.media.ext.res
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.navigation.R as navigationR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars
        .exclude(WindowInsets.navigationBars)
        .union(WindowInsets.displayCutout),
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val sort by viewModel.selectedSort.collectAsState()

    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(bottom = dimensionResource(navigationR.dimen.navigation_bar_height))
        else -> PaddingValues(start = dimensionResource(navigationR.dimen.navigation_rail_width))
    }
    val insetAndNavigationPaddingValues = contentWindowInsets.asPaddingValues() + navigationComponentPaddingValues

    val exploreList by viewModel.exploreList.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (exploreList) {
            is Resource.Success -> {
                Box {
                    MediaMediumList(
                        mediaMediumList = exploreList.data.orEmpty().toImmutableList(),
                        onItemClick = { _, _ -> },
                        contentPadding = insetAndNavigationPaddingValues,
                    )

                    Sort(
                        sorts = Media.Sort.entries.toImmutableList(),
                        selectedSort = sort,
                        onSortSelected = { viewModel.setMediaSort(it) },
                        expanded = isDropdownExpanded,
                        setExpanded = { isDropdownExpanded = it },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(insetAndNavigationPaddingValues)
                            .padding(WindowInsets.navigationBars.asPaddingValues())
                            .padding(LocalPaddings.current.large)
                    )
                }
            }
            is Resource.Loading -> {

            }
            is Resource.Error -> {

            }
        }
    }
}

@Composable
private fun Sort(
    sorts: ImmutableList<Media.Sort>,
    selectedSort: Media.Sort,
    onSortSelected: (Media.Sort) -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cascadeState = rememberCascadeState()
    val cornerRadius by animateIntAsState(
        targetValue = if (expanded) 10 else 50,
        label = "corner_radius_animation",
    )

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
                imageVector = ImageVector.vectorResource(R.drawable.filter),
                contentDescription = stringResource(R.string.filter),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable { setExpanded(!expanded) }
                    .padding(LocalPaddings.current.medium)
                    .size(LocalPaddings.current.large)
            )
        }
        CascadeDropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) },
            state = cascadeState,
            shape = RoundedCornerShape(
                topStartPercent = 50 / 5,
                topEndPercent = 50 / 5,
                bottomEndPercent = 10 / 5,
                bottomStartPercent = 50 / 5,
            ),
            offset = DpOffset(x = 0.dp, y = LocalPaddings.current.tiny),
        ) {
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(LocalPaddings.current.small),
//            ) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(R.drawable.descending),
//                    contentDescription = stringResource(R.string.descending),
//                    modifier = Modifier.size(LocalPaddings.current.medium)
//                )
//                Text(
//                    text = stringResource(R.string.descending),
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontSize = 12.sp
//                )
//            }
            sorts.forEach { sort ->
                val backgroundColor by animateColorAsState(
                    targetValue = if (sort == selectedSort)
                        MaterialTheme.colorScheme.primary
                    else Color.Transparent
                )
                val textColor by animateColorAsState(
                    targetValue = if (sort == selectedSort)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onBackground
                )
                DropdownMenuItem(
                    text = { Text(stringResource(sort.res)) },
                    leadingIcon = null,
                    colors = MenuDefaults.itemColors(
                        textColor = textColor,
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { onSortSelected(sort); setExpanded(false) },
                    contentPadding = PaddingValues(
                        vertical = LocalPaddings.current.small,
                        horizontal = LocalPaddings.current.medium
                    ),
                    modifier = Modifier.background(color = backgroundColor)
                )
            }
        }
    }
}
