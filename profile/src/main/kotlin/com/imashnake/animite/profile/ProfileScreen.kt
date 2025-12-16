package com.imashnake.animite.profile

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.animiteBlockQuoteStyle
import com.imashnake.animite.core.extensions.animiteCodeBlockStyle
import com.imashnake.animite.core.extensions.copy
import com.imashnake.animite.core.extensions.crossfadeModel
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.maxHeight
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.FallbackMessage
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.ProgressIndicatorScreen
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.profile.tabs.AboutTab
import com.imashnake.animite.profile.tabs.FavouritesTab
import com.imashnake.animite.profile.tabs.MediaTab
import com.imashnake.animite.profile.tabs.ProfileTab
import com.imashnake.animite.settings.SettingsPage
import kotlinx.coroutines.launch
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.core.R as coreR
import com.imashnake.animite.navigation.R as navigationR

private const val DROP_DOWN_ITEMS_COUNT = 2

@Suppress("LongMethod")
@Composable
fun ProfileScreen(
    onNavigateToMediaItem: (MediaPage) -> Unit,
    onNavigateToSettings: (SettingsPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(
            bottom = dimensionResource(navigationR.dimen.navigation_bar_height)
        )
        else -> PaddingValues(
            start = dimensionResource(navigationR.dimen.navigation_rail_width)
        )
    }
    val allPaddingValues = insetPaddingValues + navigationComponentPaddingValues

    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val viewer by viewModel.viewer.collectAsState()
    val viewerAnimeLists by viewModel.viewerAnimeLists.collectAsState()
    val viewerMangaLists by viewModel.viewerMangaLists.collectAsState()

    val data = listOf(viewer, viewerAnimeLists, viewerMangaLists)

    var isLogOutDialogShown by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoggedIn -> when {
                data.all { it is Resource.Success } -> viewer.data?.run {
                    BannerLayout(
                        banner = { modifier ->
                            Box {
                                AsyncImage(
                                    model = crossfadeModel(banner),
                                    contentDescription = null,
                                    modifier = modifier,
                                    contentScale = ContentScale.Crop
                                )
                                AsyncImage(
                                    model = crossfadeModel(avatar),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(start = LocalPaddings.current.medium)
                                        .padding(allPaddingValues.horizontalOnly)
                                        .size(100.dp),
                                )
                                SettingsAndMore(
                                    onNavigateToSettings = onNavigateToSettings,
                                    logOut = { isLogOutDialogShown = true },
                                    refresh = { viewModel.refresh { isDropdownExpanded = false } },
                                    expanded = isDropdownExpanded,
                                    setExpanded = { isDropdownExpanded = it },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(allPaddingValues.copy(bottom = 0.dp)),
                                )
                            }
                        },
                        content = {
                            Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = LocalPaddings.current.large)
                                        .padding(allPaddingValues.horizontalOnly)
                                ) {
                                    Text(
                                        text = name,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.titleLarge,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    UserDescription(
                                        description = description,
                                        modifier = Modifier.maxHeight(dimensionResource(R.dimen.user_about_height))
                                    )
                                }
                                UserTabs(
                                    user = this@run,
                                    animeCollection = viewerAnimeLists.data,
                                    mangaCollection = viewerMangaLists.data,
                                    onNavigateToMediaItem = onNavigateToMediaItem,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    contentPadding = navigationComponentPaddingValues + insetPaddingValues,
                                )
                            }
                        },
                        contentPadding = PaddingValues(top = LocalPaddings.current.large / 2)
                    )
                }
                else -> ProgressIndicatorScreen(Modifier.padding(allPaddingValues))
            }
            else -> Login(Modifier.padding(allPaddingValues))
        }

        if (isLogOutDialogShown)
            LogOutDialog(
                logOut = {
                    viewModel.logOut()
                    isLogOutDialogShown = false
                },
                dismiss = {
                    isLogOutDialogShown = false
                    isDropdownExpanded = false
                },
            )
    }
}

@Composable
private fun UserDescription(description: String?, modifier: Modifier = Modifier) {
    description?.let {
        Crossfade(targetState = description, modifier = modifier.animateContentSize()) {
            Box {
                NestedScrollableContent { contentModifier ->
                    MarkdownDocument(
                        markdown = it,
                        // TODO: Fix typography and make this an `animiteTextStyle()`.
                        textStyles = m3TextStyles().copy(
                            textStyle = m3TextStyles().textStyle.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)
                            )
                        ),
                        blockQuoteStyle = animiteBlockQuoteStyle(),
                        codeBlockStyle = animiteCodeBlockStyle(),
                        modifier = contentModifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsAndMore(
    onNavigateToSettings: (SettingsPage) -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    logOut: () -> Unit,
    refresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cascadeState = rememberCascadeState()
    val cornerRadius by animateIntAsState(
        targetValue = if (expanded) 10 else 50,
        label = "corner_radius_animation",
    )
    val infiniteTransition = rememberInfiniteTransition(label = "settings_icon")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(LocalPaddings.current.large)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = stringResource(R.string.settings),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable { onNavigateToSettings(SettingsPage) }
                    .padding(LocalPaddings.current.small)
                    .size(LocalPaddings.current.medium)
                    .graphicsLayer { rotationZ = angle }
            )
        }

        Box {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomEndPercent = cornerRadius,
                    bottomStartPercent = 50,
                ),
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = stringResource(R.string.more_options),
                    tint = MaterialTheme.colorScheme.primary,
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
                    topStartPercent = 50 / DROP_DOWN_ITEMS_COUNT,
                    topEndPercent = 10 / DROP_DOWN_ITEMS_COUNT,
                    bottomEndPercent = 50 / DROP_DOWN_ITEMS_COUNT,
                    bottomStartPercent = 50 / DROP_DOWN_ITEMS_COUNT,
                ),
                offset = DpOffset(x = 0.dp, y = LocalPaddings.current.tiny),
            ) {
                // TODO: This is horrible UX, use nested scroll <-> pull to refresh.
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.refresh)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(R.string.refresh),
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        leadingIconColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = refresh,
                    contentPadding = PaddingValues(LocalPaddings.current.medium)
                )

                // TODO: The material3 DropdownMenuItem doesn't respect layout direction padding.
                //  Figure out why/create an issue. saket-cascade works just fine.
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.log_out)) },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_logout),
                            contentDescription = stringResource(R.string.log_out),
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        leadingIconColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = logOut,
                    contentPadding = PaddingValues(LocalPaddings.current.medium)
                )
            }
        }
    }
}

@Composable
fun LogOutDialog(
    logOut: () -> Unit,
    dismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                contentDescription = "Log out",
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = { Text(text = stringResource(R.string.log_out)) },
        text = { Text(text = stringResource(R.string.log_out_confirmation)) },
        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(onClick = logOut) {
                Text(stringResource(R.string.log_out))
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTabs(
    user: User,
    animeCollection: User.MediaCollection?,
    mangaCollection: User.MediaCollection?,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { ProfileTab.entries.size })
    val titles = ProfileTab.entries
    val onBackground = MaterialTheme.colorScheme.onBackground
    val horizontalContentPadding = contentPadding.horizontalOnly

    Column(modifier) {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            divider = {},
            modifier = Modifier.padding(horizontalContentPadding)
        ) {
            titles.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = {
                        Text(
                            text = stringResource(tab.titleRes),
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = onBackground.copy(
                                alpha = if (pagerState.currentPage == index) 1f else 0.5f
                            ),
                            maxLines = 1
                        )
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = LocalPaddings.current.ultraTiny,
                            vertical = LocalPaddings.current.small
                        )
                        .clip(CircleShape)
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            onBackground.copy(alpha = 0.03f),
                            Color.Transparent
                        )
                    )
                )
        ) { page ->

            val tabContentPadding = PaddingValues(
                all = LocalPaddings.current.large
            ) + contentPadding.copy(top = 0.dp)

            Box(Modifier.fillMaxSize()) {
                when (ProfileTab.entries[page]) {
                    ProfileTab.ABOUT -> AboutTab(
                        user = user,
                        contentPadding = tabContentPadding,
                    )
                    ProfileTab.ANIME -> MediaTab(
                        mediaCollection = animeCollection,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        contentPadding = tabContentPadding,
                    )
                    ProfileTab.MANGA -> MediaTab(
                        mediaCollection = mangaCollection,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        contentPadding = tabContentPadding,
                    )
                    ProfileTab.FAVOURITES -> FavouritesTab(
                        favouriteLists = user.favourites,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        contentPadding = tabContentPadding,
                    )
                    else -> FallbackMessage(
                        message = stringResource(coreR.string.coming_soon),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(tabContentPadding)
                    )
                }
            }
        }
    }
}
