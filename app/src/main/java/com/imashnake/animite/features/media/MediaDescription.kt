package com.imashnake.animite.features.media

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DescriptionBottomSheet(
    title: String,
    description: String,
    content: @Composable () -> Unit = {}
) {
    // show a bottom sheet on full screen with MediaDescription in view
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            BottomSheetValue.Expanded,
        )
    )
    BottomSheetScaffold(
        sheetGesturesEnabled = false,
        sheetPeekHeight = 200.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        scaffoldState = bottomSheetState,
        modifier = Modifier.navigationBarsPadding(),
        sheetContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            )
            MediaDescription(
                description = description
            )
        }
    ) {
        content()
    }
}

@Composable
fun MediaDescription(
    description: String
) {
    Text(
        text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
    )
}

@Preview
@Composable
fun MediaDescriptionPreview() {
    DescriptionBottomSheet(
        title = "Animite",
        description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
    )
}