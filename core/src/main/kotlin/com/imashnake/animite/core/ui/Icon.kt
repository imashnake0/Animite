package com.imashnake.animite.core.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.imashnake.animite.core.R

/**
 * Wrapper for [IconButton]. Flattens an [IconButton] with an [Icon].
 *
 * @param onClick called when this icon button is clicked.
 * @param imageVector [ImageVector] to draw inside this icon.
 * @param modifier the [Modifier] to be applied to this icon button.
 * @param foregroundColor the color of the [Icon].
 * @param backgroundColor the background color of the [IconButton].
 * @param contentDescription text used by accessibility services to describe what this icon.
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    foregroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentDescription: String? = null
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(dimensionResource(R.dimen.icon_size)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = backgroundColor,
            contentColor = foregroundColor
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = foregroundColor
        )
    }
}

@Preview
@Composable
fun PreviewIconButton() {
    IconButton(
        onClick = {},
        imageVector = Icons.Rounded.AccountCircle,
        foregroundColor = MaterialTheme.colorScheme.onPrimary,
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentDescription = "Account"
    )
}
