package com.imashnake.animite.core.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.ext.thenIf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    deviceScreenCornerRadiusDp: Dp,
    modifier: Modifier = Modifier,
    dragHandleBackgroundColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = LocalPaddings.current.large),
    content: @Composable (contentPadding: PaddingValues, modifier: Modifier) -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier.fillMaxHeight(),
        sheetState = sheetState,
        dragHandle = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .thenIf(dragHandleBackgroundColor != null) {
                        background(dragHandleBackgroundColor!!)
                    }
            ) {
                AnimatedContent(sheetState.targetValue) {
                    val (size, icon) = if (it == SheetValue.Expanded) {
                        16.dp to Icons.Rounded.Close
                    } else {
                        20.dp to Icons.Rounded.KeyboardArrowUp
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(LocalPaddings.current.small)
                            .size(size)
                    )
                }
            }
        },
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        shape = RoundedCornerShape(
            topStart = deviceScreenCornerRadiusDp,
            topEnd = deviceScreenCornerRadiusDp
        )
    ) {
        content(
            contentPadding,
            Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}
