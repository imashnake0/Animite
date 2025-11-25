package com.imashnake.animite.core.ui

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.R
import com.imashnake.animite.core.extensions.thenIf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dragHandleBackgroundColor: Color? = null,
    content: @Composable ColumnScope.(contentPadding: PaddingValues) -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier.fillMaxHeight(),
        sheetState = sheetState,
        dragHandle = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().thenIf(dragHandleBackgroundColor != null) {
                    background(dragHandleBackgroundColor!!)
                }
            ) {
                Image(
                    painter = rememberAnimatedVectorPainter(
                        AnimatedImageVector.animatedVectorResource(R.drawable.chevron_to_cross_anim),
                        atEnd = sheetState.targetValue == SheetValue.Expanded
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .padding(LocalPaddings.current.small)
                        .size(20.dp)
                )
            }
        },
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            content(PaddingValues(horizontal = LocalPaddings.current.large))
        }
    }
}
