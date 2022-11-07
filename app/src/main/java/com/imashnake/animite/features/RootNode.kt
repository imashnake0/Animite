package com.imashnake.animite.features

import android.os.Parcelable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.composable.childrenAsState
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.singleTop
import com.imashnake.animite.R
import com.imashnake.animite.features.home.Home
import com.imashnake.animite.features.profile.Profile
import com.imashnake.animite.features.rslash.RSlash
import com.imashnake.animite.features.searchbar.SearchBar
import com.imashnake.animite.features.theme.AnimiteTheme
import kotlinx.parcelize.Parcelize

class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Home,
        savedStateMap = buildContext.savedStateMap,
    )
) : ParentNode<RootNode.NavTarget>(
    navModel = backStack,
    buildContext
) {

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.Home -> node(buildContext) { Home() }
            NavTarget.Profile -> node(buildContext) { Profile() }
            NavTarget.RSlash -> node(buildContext) { RSlash() }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun View(modifier: Modifier) {
        val screenState by backStack.childrenAsState()

        AnimiteTheme {
            Scaffold(
                contentWindowInsets = WindowInsets(0), // We handle insets ourselves
                bottomBar = {
                    NavigationBar(
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        // TODO We don't need to be creating 3 of everything
                        NavigationBarItem(
                            selected = screenState.last().key.navTarget == NavTarget.RSlash,
                            onClick = { backStack.singleTop(NavTarget.RSlash) },
                            icon = {
                                Icon(painterResource(R.drawable.rslash), stringResource(R.string.rslash))
                            }
                        )
                        NavigationBarItem(
                            selected = screenState.last().key.navTarget == NavTarget.Home,
                            onClick = { backStack.singleTop(NavTarget.Home) },
                            icon = {
                                Icon(painterResource(R.drawable.home), stringResource(R.string.home))
                            }
                        )
                        NavigationBarItem(
                            selected = screenState.last().key.navTarget == NavTarget.Profile,
                            onClick = { backStack.singleTop(NavTarget.Profile) },
                            icon = {
                                Icon(Icons.Rounded.AccountCircle, stringResource(R.string.profile))
                            }
                        )
                    }
                },
                floatingActionButton = {
                    SearchBar(onItemClicked = { /* TODO */ })
                },
                floatingActionButtonPosition = FabPosition.End
            ) {
                Children(navModel = backStack, modifier = Modifier.padding(it))
            }
        }
    }

    sealed class NavTarget : Parcelable {
        @Parcelize
        object RSlash : NavTarget()
        @Parcelize
        object Home : NavTarget()
        @Parcelize
        object Profile : NavTarget()
    }
}
