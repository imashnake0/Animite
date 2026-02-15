package com.imashnake.animite.features

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.features.searchbar.SearchFrontDrop
import com.imashnake.animite.features.theme.AnimiteTheme
import com.imashnake.animite.features.theme.manropeFontFamily
import com.imashnake.animite.navigation.AnimeRoute
import com.imashnake.animite.navigation.MangaRoute
import com.imashnake.animite.navigation.LocalSharedTransitionScope
import com.imashnake.animite.navigation.NavigationBar
import com.imashnake.animite.navigation.NavigationRail
import com.imashnake.animite.navigation.Navigator
import com.imashnake.animite.navigation.Nested
import com.imashnake.animite.navigation.Nested.MediaRoute
import com.imashnake.animite.profile.ProfileViewModel
import com.imashnake.animite.navigation.Root
import com.imashnake.animite.settings.SettingsViewModel
import com.imashnake.animite.settings.Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    var showSplashScreen = true

    @Inject
    internal lateinit var entryProviders: Set<@JvmSuppressWildcards EntryProviderScope<NavKey>.() -> Unit>

    @Inject
    internal lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition { showSplashScreen }
        enableEdgeToEdge()
        setContent {
            val fontFamilyResolver = LocalFontFamilyResolver.current
            LaunchedEffect(Unit) {
                fontFamilyResolver.preload(manropeFontFamily)
                showSplashScreen = false
            }

            val theme by settingsViewModel.theme
                .filterNotNull()
                .collectAsState(initial = Theme.DEVICE_THEME.name)

            val useSystemColorScheme by settingsViewModel.useSystemColorScheme
                .filterNotNull()
                .collectAsState(initial = false)

            val useDarkTheme = when (Theme.valueOf(theme)) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                Theme.DEVICE_THEME -> isSystemInDarkTheme()
            }

            val dayHour by settingsViewModel.dayHour.collectAsState(initial = null)

            val avatar by profileViewModel.viewerAvatar.collectAsState(initial = null)

            // gross
            if (intent.action == Intent.ACTION_VIEW) {
                val data = intent.dataString?.replace("#", "?").orEmpty().toUri()
                val route = Root.ProfileRoute(
                    accessToken = data.getQueryParameter("access_token"),
                    tokenType = data.getQueryParameter("token_type"),
                    expiresIn = data.getQueryParameter("expires_in")?.toInt() ?: 0,
                )
                navigator.navigateTo(route)
            }

            AnimiteTheme(
                useDarkTheme = useDarkTheme,
                useSystemColorScheme = useSystemColorScheme,
                dayHour = dayHour
            ) {
                MainScreen(
                    navigator = navigator,
                    navEntries = entryProviders,
                    avatar = avatar,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    navigator: Navigator,
    navEntries: Set<@JvmSuppressWildcards EntryProviderScope<NavKey>.() -> Unit>,
    avatar: String?,
    modifier: Modifier = Modifier,
) {
    val isNavBarVisible = navigator.isCurrentScreenRoot()
    val isFabVisible = navigator.backStack.lastOrNull() !is Nested.SettingsRoute

    // TODO: Refactor to use Scaffold once AnimatedVisibility issues are fixed;
    //  see https://issuetracker.google.com/issues/258270139.
    Box(modifier) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            SharedTransitionLayout {
                CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                    NavDisplay(
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        backStack = navigator.backStack,
                        entryProvider = entryProvider {
                            navEntries.forEach { builder -> this.builder() }
                        }
                    )
                }
            }
        }

        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                AnimatedVisibility(
                    visible = isNavBarVisible,
                    modifier = Modifier.align(Alignment.CenterStart),
                    enter = slideInHorizontally { -it },
                    exit = slideOutHorizontally { -it }
                ) {
                    NavigationRail(
                        backStack = navigator.backStack,
                        avatar = avatar,
                        navigateTo = navigator::navigateTo
                    )
                }
            }

            else -> {
                AnimatedVisibility(
                    visible = isNavBarVisible,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    NavigationBar(
                        backStack = navigator.backStack,
                        avatar = avatar,
                        navigateTo = navigator::navigateTo
                    )
                }
            }
        }

        SearchFrontDrop(
            hasExtraPadding = isNavBarVisible &&
                    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT,
            onItemClick = { id, mediaType, title ->
                navigator.navigateTo(
                    MediaRoute(
                        id = id,
                        source = MediaList.Type.SEARCH.name,
                        mediaType = mediaType.rawValue,
                        title = title,
                    )
                )
            },
            isFabVisible = isFabVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    start = LocalPaddings.current.large,
                    end = LocalPaddings.current.large,
                    bottom = LocalPaddings.current.large,
                )
        )
    }
}
