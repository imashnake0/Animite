package com.imashnake.animite.features.home

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.imashnake.animite.features.media.MediaPage
import kotlinx.parcelize.Parcelize

class HomeNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Browse,
        savedStateMap = buildContext.savedStateMap,
    )
) : ParentNode<HomeNode.NavTarget>(
    navModel = backStack,
    buildContext
) {

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            is NavTarget.Browse -> node(buildContext) {
                Home(
                    onItemClicked = { id, mediaType ->
                        backStack.push(NavTarget.Details(id, mediaType.rawValue))
                    }
                )
            }
            is NavTarget.Details -> node(buildContext) {
                MediaPage(id = navTarget.id, mediaTypeArg = navTarget.mediaType)
            }
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(navModel = backStack, modifier = modifier)
    }

    sealed class NavTarget : Parcelable {
        @Parcelize
        object Browse : NavTarget()

        @Parcelize
        class Details(val id: Int?, val mediaType: String) : NavTarget()
    }
}
