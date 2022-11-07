package com.imashnake.animite.features

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.imashnake.animite.features.home.Home
import com.imashnake.animite.features.profile.Profile
import com.imashnake.animite.features.rslash.RSlash
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
        Children(navModel = backStack)
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
