package com.imashnake.animite.core.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ToggleText(
    texts: ImmutableList<String?>,
    text: @Composable (text: String?) -> Unit ,
    modifier: Modifier = Modifier
) {
    var selectedTextIndex by remember { mutableIntStateOf(0) }
    val textCount = texts.size

    AnimatedContent(
        targetState = selectedTextIndex % textCount,
        modifier = modifier
            .clip(RoundedCornerShape(15))
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { selectedTextIndex++ },
                onLongClick = { selectedTextIndex = 0 }
            )
    ) {
        text(texts[it])
    }
}
