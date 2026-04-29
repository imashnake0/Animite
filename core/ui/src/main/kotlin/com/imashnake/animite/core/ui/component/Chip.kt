package com.imashnake.animite.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.imashnake.animite.core.ui.LocalPaddings
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Chip(
    color: Color,
    icon: ImageVector?,
    text: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    iconPadding: PaddingValues = PaddingValues(),
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick?.invoke() }
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = LocalPaddings.current.small)
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .padding(iconPadding)
                    .size(15.dp)
            )
        }
        text?.let {
            Text(
                text = text,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun ChipFlowRow(
    chips: ImmutableList<String>,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    chipColor: Color= MaterialTheme.colorScheme.tertiary,
    transformFilterIcon: @Composable ((String) -> ImageVector?)? = null,
    transformFilterText: @Composable ((String) -> String?)? = null,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        modifier = modifier.fillMaxWidth()
    ) {
        chips.fastForEach { chip ->
            Chip(
                color = chipColor,
                icon = transformFilterIcon?.invoke(chip),
                text = transformFilterText?.invoke(chip) ?: chip,
                onClick = { onChipClick(chip) },
            )
        }
    }
}

@Composable
fun EmptyChip() {
    Chip(
        color = MaterialTheme.colorScheme.primary,
        icon = null,
        text = "",
        modifier = Modifier.graphicsLayer { alpha = 0f }
    )
}
