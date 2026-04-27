package com.imashnake.animite.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.rememberDefaultPaddings

@Composable
fun Paginator(
    page: Int?,
    pageRange: IntRange,
    onPageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            val screenDpSize = LocalWindowInfo.current.containerDpSize
            val yearItemSize = if (screenDpSize.width > (56 * 5).dp) {
                56.dp
            } else screenDpSize.width / 5

            var shortenYear by remember { mutableStateOf(false) }
            PageOutline(shortenYear)

            val paginatorState = rememberLazyListState()
            LaunchedEffect(page) {
                page?.let {
                    paginatorState.animateScrollToItem(page - pageRange.first())
                }
            }
            LazyRow(
                state = paginatorState,
                contentPadding = PaddingValues(horizontal = yearItemSize * 2f),
                userScrollEnabled = false,
                modifier = Modifier.requiredWidth(yearItemSize * 5),
            ) {
                items(pageRange.count()) {
                    val currentPage = pageRange.first + it
                    val textAlpha by animateFloatAsState(
                        if (currentPage == page) 1f else 0.5f
                    )
                    Box(Modifier.requiredSize(yearItemSize)) {
                        val text = if (shortenYear) {
                            "'${currentPage.toString().takeLast(2)}"
                        } else "$currentPage"
                        Text(
                            text = text,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = textAlpha
                            ),
                            onTextLayout = { result ->
                                if (result.hasVisualOverflow) shortenYear = true
                            },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
        Button(
            enabled = (page ?: 0) > pageRange.first,
            onClick = { onPageChanged((page ?: 0) - 1) },
            contentPadding = PaddingValues(),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.requiredSize(24.dp)
            )
        }
        Button(
            enabled = (page ?: 0) < pageRange.last,
            onClick = { onPageChanged((page ?: 0) + 1) },
            contentPadding = PaddingValues(),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.requiredSize(24.dp)
            )
        }
    }
}

@Composable
private fun PageOutline(
    shortenPage: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.border(
            width = 2.dp,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        // Fake year field
        Text(
            text = if (shortenPage) "000" else "0000",
            color = Color.Transparent,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(LocalPaddings.current.small)
        )
    }
}

@Preview
@Composable
fun PreviewPaginator() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        var page by remember { mutableIntStateOf(0) }
        Paginator(
            page = page,
            pageRange = 0..3,
            onPageChanged = { page = it},
        )
    }
}
