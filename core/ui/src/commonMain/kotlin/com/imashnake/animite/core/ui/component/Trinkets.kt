package com.imashnake.animite.core.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import animite.core.ui.generated.resources.Res
import animite.core.ui.generated.resources.drop_down
import com.imashnake.animite.core.ui.LocalPaddings
import org.jetbrains.compose.resources.painterResource

@Composable
fun DropDownIcon(
    isDroppedDown: Boolean,
    modifier: Modifier = Modifier
) {
    val iconRotation by animateFloatAsState(if (isDroppedDown) 0f else -90f)
    Icon(
        painter = painterResource(Res.drawable.drop_down),
        contentDescription = null,
        modifier = modifier
            .requiredSize(16.dp)
            .graphicsLayer { rotationZ = iconRotation }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Divider(
    shape: Shape,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "divider")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Box(
        modifier = modifier
            .graphicsLayer { rotationZ = angle }
            .padding(horizontal = LocalPaddings.current.small)
            .size(4.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                shape = MaterialShapes.Triangle.toShape()
            )
    )
}
