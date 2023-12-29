package com.imashnake.animite.core.ui

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import com.imashnake.animite.core.R

@Composable
fun ScrollableText(
    text: String,
    modifier: Modifier = Modifier,
    gradientSize: Dp = dimensionResource(R.dimen.edge_gradient_size),
    gradientColor: Color = MaterialTheme.colorScheme.background
) {
    Box(modifier) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = ContentAlpha.medium
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = gradientSize)
        )

        Box(
            modifier = Modifier
                .height(gradientSize)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            gradientColor,
                            Transparent
                        )
                    )
                )
        ) { }

        Box(
            modifier = Modifier
                .height(gradientSize)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Transparent,
                            gradientColor
                        )
                    )
                )
        ) { }
    }
}

/**
 * TODO: Get rid of this once Compose supports HTML/Markdown
 *  https://issuetracker.google.com/issues/139326648
 */
@Composable
fun ScrollableTextView(
    text: String,
    modifier: Modifier = Modifier,
    gradientSize: Dp = dimensionResource(R.dimen.edge_gradient_size),
    gradientColor: Color = MaterialTheme.colorScheme.background
) {
    val html = remember(text) {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    Box(modifier) {
        val textColor = MaterialTheme.colorScheme.onBackground.copy(
            alpha = ContentAlpha.medium
        ).toArgb()

        AndroidView(
            factory = {
                TextView(it).apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    setTextColor(textColor)
                    textSize = 14f
                    // This is needed since `FontFamily` can't be used with `AndroidView`.
                    typeface = ResourcesCompat.getFont(it, R.font.manrope_medium)
                }
            },
            update = { it.text = html },
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(vertical = gradientSize)
        )

        Box(
            modifier = Modifier
                .height(gradientSize)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            gradientColor,
                            Transparent
                        )
                    )
                )
        ) { }

        Box(
            modifier = Modifier
                .height(gradientSize)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Transparent,
                            gradientColor
                        )
                    )
                )
        ) { }
    }
}
