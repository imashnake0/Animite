package com.imashnake.animite.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import animite.core.ui.generated.resources.Res
import animite.core.ui.generated.resources.drag_handle
import com.imashnake.animite.core.ui.LocalPaddings
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sh.calvin.reorderable.ReorderableColumn

@Composable
fun <T> ReorderableList(
    list: ImmutableList<T>,
    modifier: Modifier = Modifier,
    isItemChecked: (index: Int) -> Boolean,
    checkItem: (Int) -> Unit,
    onSettle: (from: Int, to: Int) -> Unit,
    onMove: () -> Unit = {},
    transformItemText: (T) -> StringResource
) {
    val haptic = LocalHapticFeedback.current

    ReorderableColumn(
        list = list.toList(),
        onSettle = onSettle,
        verticalArrangement = Arrangement.spacedBy(
            ButtonGroupDefaults.ConnectedSpaceBetween
        ),
        onMove = {
            onMove()
            haptic.performHapticFeedback(
                HapticFeedbackType.SegmentFrequentTick
            )
        },
        modifier = modifier
    ) { index, item, _ ->
        key(item) {
            ReorderableItem {
                ToggleButton(
                    checked = isItemChecked(index),
                    onCheckedChange = { checkItem(index) },
                    shapes = ButtonGroupDefaults.connectedMiddleButtonShapes(
                        checkedShape = RoundedCornerShape(10.dp)
                    ),
                    colors = ToggleButtonDefaults.toggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        checkedContentColor = MaterialTheme.colorScheme.onBackground,
                        checkedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var position by remember(isItemChecked) { mutableStateOf(Offset.Zero) }
                    var maxRadius by remember { mutableFloatStateOf(0f) }
                    val radius by animateFloatAsState(
                        targetValue = if (isItemChecked(index)) maxRadius else 0f,
                        animationSpec = tween(300)
                    )
                    val checkedContainerColor = MaterialTheme.colorScheme.primary.copy(0.1f)

                    Box(
                        modifier = Modifier.pointerInput(isItemChecked) {
                            detectTapGestures(onTap = { position = it; checkItem(index) })
                        }.drawBehind {
                            // If the circle is at the start of the item we want the circumference
                            // to still not be visible.
                            maxRadius = size.width * 1.3f
                            drawCircle(
                                color = checkedContainerColor,
                                radius = radius,
                                center = position
                            )
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AnimatedVisibility(visible = isItemChecked(index)) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.size(LocalPaddings.current.small))
                                    }
                                }
                                Text(text = stringResource(transformItemText(item)))
                            }
                            Icon(
                                painter = painterResource(Res.drawable.drag_handle),
                                contentDescription = stringResource(Res.string.drag_handle),
                                modifier = Modifier.draggableHandle()
                            )
                        }
                    }
                }
            }
        }
    }
}
