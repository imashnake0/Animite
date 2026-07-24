package com.imashnake.animite.profile

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.graphics.toColorInt
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.banner.NestedScrollBannerLayout
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.BottomSheet
import com.imashnake.animite.core.ui.component.ProgressIndicatorScreen
import com.imashnake.animite.core.ui.ext.copy
import com.imashnake.animite.core.ui.ext.crossfadeModel
import com.imashnake.animite.core.ui.ext.horizontalOnly
import com.imashnake.animite.core.ui.ext.maxHeight
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.media.rememberColorSchemeFor
import com.imashnake.animite.profile.tabs.AboutTab
import com.imashnake.animite.profile.tabs.FavouritesTab
import com.imashnake.animite.profile.tabs.MediaTab
import com.imashnake.animite.profile.tabs.ProfileTab
import com.imashnake.animite.settings.SettingsPage
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownAnnotatorConfig
import kotlinx.coroutines.launch
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import com.imashnake.animite.navigation.R as navigationR

private const val DROP_DOWN_ITEMS_COUNT = 2

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun ProfileScreen(
    onNavigateToMediaItem: (MediaPage) -> Unit,
    onNavigateToSettings: (SettingsPage) -> Unit,
    showUserDescription: Boolean,
    deviceScreenCornerRadius: Int,
    useDarkTheme: Boolean,
    isAmoled: Boolean,
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
    val useProfileColor by viewModel.useProfileColor.collectAsState(initial = true)
    val viewerAvatar by viewModel.viewerAvatar.collectAsState(initial = "")
    val viewer by viewModel.viewer.collectAsState()
    val viewerAnimeLists by viewModel.viewerAnimeLists.collectAsState()
    val viewerMangaLists by viewModel.viewerMangaLists.collectAsState()

    val data = listOf(viewer, viewerAnimeLists, viewerMangaLists)

    var isLogOutDialogShown by remember { mutableStateOf(false) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val deviceScreenCornerRadiusDp = with(LocalDensity.current) {
        deviceScreenCornerRadius.toDp()
    }
    var showUserDescriptionSheet by remember { mutableStateOf(false) }
    val userDescriptionSheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoggedIn -> when {
                data.all { it is Resource.Success } -> viewer.data?.run {
                    MaterialTheme(
                        colorScheme = if (useProfileColor) {
                            rememberColorSchemeFor(
                                color = color?.toColorInt(),
                                useDarkTheme = useDarkTheme,
                                isAmoled = isAmoled
                            )
                        } else MaterialTheme.colorScheme
                    ) {
                        LaunchedEffect(avatar) {
                            if (viewerAvatar != avatar) viewModel.saveViewerAvatar(avatar)
                        }

                        var isRefreshing by remember { mutableStateOf(false) }
                        val pullToRefreshState = rememberPullToRefreshState()
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { viewModel.refresh { isRefreshing = it } },
                            state = pullToRefreshState,
                        ) {
                            NestedScrollBannerLayout(
                                banner = { ratio, modifier ->
                                    Box(Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                                        AsyncImage(
                                            model = crossfadeModel(banner),
                                            contentDescription = null,
                                            alpha = 1.2f * ratio - 0.2f,
                                            modifier = modifier,
                                            contentScale = ContentScale.Crop
                                        )
                                        AsyncImage(
                                            model = crossfadeModel(avatar),
                                            contentDescription = "Avatar",
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(start = LocalPaddings.current.large)
                                                .padding(allPaddingValues.horizontalOnly)
                                                .wrapContentSize()
                                                .maxHeight(100.dp)
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = LocalPaddings.current.small,
                                                        topEnd = LocalPaddings.current.small,
                                                    )
                                                )
                                                .graphicsLayer { alpha = 1.5f * ratio - 0.5f },
                                        )
                                    }
                                },
                                bannerElevatedContent = { ratio ->
                                    SettingsAndMore(
                                        onNavigateToSettings = onNavigateToSettings,
                                        logOut = { isLogOutDialogShown = true },
                                        expanded = isDropdownExpanded,
                                        setExpanded = { isDropdownExpanded = it },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(allPaddingValues.copy(bottom = 0.dp))
                                            .padding(
                                                start = LocalPaddings.current.large,
                                                bottom = LocalPaddings.current.large,
                                                end = LocalPaddings.current.large
                                            )
                                            .padding(top = LocalPaddings.current.large * ratio)
                                    )
                                },
                                content = {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(
                                            LocalPaddings.current.ultraTiny
                                        )
                                    ) {
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
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(LocalPaddings.current.small))
                                                    .clickable {
                                                        showUserDescriptionSheet = true
                                                    }
                                            )
                                        }
                                        UserTabs(
                                            user = this@run,
                                            animeCollection = viewerAnimeLists.data,
                                            mangaCollection = viewerMangaLists.data,
                                            onNavigateToMediaItem = onNavigateToMediaItem,
                                            showUserDescription = showUserDescription,
                                            onUserDescriptionClick = {
                                                showUserDescriptionSheet = true
                                            },
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            contentPadding = navigationComponentPaddingValues + insetPaddingValues,
                                        )
                                    }
                                },
                                contentBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentPadding = PaddingValues(top = LocalPaddings.current.large / 2)
                            )
                        }

                        if (showUserDescriptionSheet) {
                            BottomSheet(
                                sheetState = userDescriptionSheetState,
                                onDismissRequest = { showUserDescriptionSheet = false },
                                deviceScreenCornerRadiusDp = deviceScreenCornerRadiusDp,
                                contentPadding = PaddingValues(
                                    horizontal = LocalPaddings.current.large,
                                    vertical = LocalPaddings.current.medium
                                ),
                                modifier = Modifier,
                            ) { paddingValues, modifier ->
                                Column(modifier) {
                                    Text(
                                        text = this@run.name,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                            .padding(paddingValues)
                                    )

                                    this@run.description?.let {
                                        UserDescriptionMarkdown(
                                            content = it,
                                            modifier = Modifier.padding(paddingValues)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                else -> ProgressIndicatorScreen(Modifier.padding(allPaddingValues))
            }

            else -> {
                SettingsIcon(
                    onNavigateToSettings = onNavigateToSettings,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(LocalPaddings.current.large)
                        .padding(allPaddingValues.copy(bottom = 0.dp)),
                )
                Login(Modifier.padding(allPaddingValues))
            }
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
private fun SettingsAndMore(
    onNavigateToSettings: (SettingsPage) -> Unit,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    logOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cascadeState = rememberCascadeState()
    val cornerRadius by animateIntAsState(
        targetValue = if (expanded) 10 else 50,
        label = "corner_radius_animation",
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SettingsIcon(onNavigateToSettings = onNavigateToSettings)

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
                        .size(20.dp)
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
fun SettingsIcon(
    onNavigateToSettings: (SettingsPage) -> Unit,
    modifier: Modifier = Modifier
) {
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

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = CircleShape,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = stringResource(R.string.settings),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onNavigateToSettings(SettingsPage) }
                .padding(LocalPaddings.current.small)
                .size(16.dp)
                .graphicsLayer { rotationZ = angle }
        )
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
    showUserDescription: Boolean,
    onUserDescriptionClick: () -> Unit,
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

    // TODO: Store these in prefs.
    val animeListVisibility = remember(animeCollection?.namedLists?.size) {
        mutableStateMapOf(
            *List(animeCollection?.namedLists?.size ?: 0) { it to true }.toTypedArray()
        )
    }
    val mangaListVisibility = remember(mangaCollection?.namedLists?.size) {
        mutableStateMapOf(
            *List(mangaCollection?.namedLists?.size ?: 0) { it to true }.toTypedArray()
        )
    }

    var scrollableTabs by remember { mutableStateOf(false) }

    Column(modifier) {
        if (!scrollableTabs) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                divider = {},
                modifier = Modifier.padding(horizontalContentPadding)
            ) {
                titles.fastForEachIndexed { index, tab ->
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
                                maxLines = 1,
                                onTextLayout = { result ->
                                    if (result.hasVisualOverflow) {
                                        scrollableTabs = true
                                    }
                                }
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
        } else {
            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                edgePadding = LocalPaddings.current.small,
                divider = {},
                modifier = Modifier.padding(horizontalContentPadding)
            ) {
                titles.fastForEachIndexed { index, tab ->
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
                                maxLines = 1,
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
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { page ->

            val tabContentPadding = PaddingValues(
                all = LocalPaddings.current.large
            ) + contentPadding.copy(top = 0.dp)

            val mediaTabContentPadding = PaddingValues(
                start = LocalPaddings.current.medium,
                end = LocalPaddings.current.medium,
                bottom = LocalPaddings.current.medium
            ) + contentPadding.copy(top = 0.dp)

            Box(Modifier.fillMaxSize()) {
                when (ProfileTab.entries[page]) {
                    ProfileTab.ABOUT -> AboutTab(
                        user = user,
                        showUserDescription = showUserDescription,
                        onUserDescriptionClick = onUserDescriptionClick,
                        contentPadding = tabContentPadding,
                    )
                    ProfileTab.ANIME -> MediaTab(
                        mediaCollection = animeCollection,
                        listVisibility = animeListVisibility,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        contentPadding = mediaTabContentPadding,
                    )
                    ProfileTab.MANGA -> MediaTab(
                        mediaCollection = mangaCollection,
                        listVisibility = mangaListVisibility,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        contentPadding = mediaTabContentPadding,
                    )
                    ProfileTab.FAVOURITES -> FavouritesTab(
                        favouriteLists = user.favourites,
                        onNavigateToMediaItem = onNavigateToMediaItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        contentPadding = tabContentPadding,
                    )
                }
            }
        }
    }
}

@Composable
fun UserDescriptionMarkdown(
    content: String,
    modifier: Modifier = Modifier
) {
    Markdown(
        content = content,
        modifier = modifier,
        colors = markdownColor(text = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)),
        // TODO: Impl `AnilistFlavourDescriptor` and pass it here to handle custom stuff.
        //   https://github.com/JetBrains/markdown#extending-the-parser ->
        //   implement your own markdown flavour.
        flavour = GFMFlavourDescriptor(),
        annotator = markdownAnnotator(markdownAnnotatorConfig(eolAsNewLine = true)),
        typography = markdownTypography(
            text = MaterialTheme.typography.bodyMedium,
            paragraph = MaterialTheme.typography.bodyMedium,
            textLink = TextLinkStyles(
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    baselineShift = null
                ).toSpanStyle()
            )
        ),
        imageTransformer = Coil3ImageTransformerImpl
    )
}
