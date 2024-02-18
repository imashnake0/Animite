package com.nasdroid.core.markdown.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Displays a Markdown Rule. A rule is a horizontal line, usually used to separate content.
 */
@Composable
fun MarkdownRule(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier)
}
