package com.imashnake.animite.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import animite.core.ui.generated.resources.Res
import animite.core.ui.generated.resources.drop_down
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
