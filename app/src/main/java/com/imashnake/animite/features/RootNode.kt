package com.imashnake.animite.features

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.mandatorySystemGestures
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.composable.childrenAsState
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.newRoot
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

    @Composable
    override fun View(modifier: Modifier) {
        val screenState by backStack.childrenAsState()

        AnimiteTheme {
            // TODO Scaffold
            Column(modifier.background(MaterialTheme.colorScheme.background)) {
                Box(Modifier.fillMaxWidth().weight(1f)) {
                    Children(navModel = backStack)

                    SearchBar(
                        onItemClicked = { /* TODO */ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                start = dimensionResource(R.dimen.large_padding),
                                end = dimensionResource(R.dimen.large_padding),
                                bottom = dimensionResource(R.dimen.large_padding)
                            )
                            .navigationBarsPadding()
                    )
                }
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    windowInsets = WindowInsets.navigationBars.add(WindowInsets.mandatorySystemGestures)
                ) {
                    NavigationBarItem(
                        selected = screenState.last().key.navTarget == NavTarget.RSlash,
                        onClick = {
                            backStack.newRoot(NavTarget.RSlash)
                        },
                        icon = {
                            Icon(painterResource(R.drawable.rslash), stringResource(R.string.rslash))
                        }
                    )
                    NavigationBarItem(
                        selected = screenState.last().key.navTarget == NavTarget.Home,
                        onClick = {
                            backStack.newRoot(NavTarget.Home)
                        },
                        icon = {
                            Icon(painterResource(R.drawable.home), stringResource(R.string.home))
                        }
                    )
                    NavigationBarItem(
                        selected = screenState.last().key.navTarget == NavTarget.Profile,
                        onClick = {
                            backStack.newRoot(NavTarget.Profile)
                        },
                        icon = {
                            Icon(Icons.Rounded.AccountCircle, stringResource(R.string.profile))
                        }
                    )
                }
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
