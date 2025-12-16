package com.imashnake.animite.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import kotlinx.serialization.Serializable

@Composable
fun SettingsPage(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    TranslucentStatusBarLayout(scrollState) {
        Box(modifier.verticalScroll(scrollState)) {
            BannerLayout(
                banner = { bannerModifier ->
                    Image(
                        painter = ColorPainter(MaterialTheme.colorScheme.primary),
                        contentDescription = null,
                        modifier = bannerModifier.bannerParallax(scrollState),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )
                },
                content = {
                    Text("what")
                }
            )
        }
    }
}

@Serializable
data object SettingsPage
