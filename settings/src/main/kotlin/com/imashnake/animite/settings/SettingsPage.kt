package com.imashnake.animite.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.layouts.banner.BannerLayout
import com.imashnake.animite.core.ui.layouts.banner.MountFuji
import kotlinx.serialization.Serializable

@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val horizontalInsets = insetPaddingValues.horizontalOnly

    val scrollState = rememberScrollState()

    TranslucentStatusBarLayout(scrollState) {
        Box(modifier.verticalScroll(scrollState)) {
            BannerLayout(
                banner = { bannerModifier ->
                    MountFuji(bannerModifier)
                },
                content = {
                    Text(
                        text = stringResource(R.string.settings),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                contentPadding = PaddingValues(
                    top = LocalPaddings.current.large / 2,
                    bottom = LocalPaddings.current.large + insetPaddingValues.calculateBottomPadding(),
                    start = LocalPaddings.current.large,
                    end = LocalPaddings.current.large,
                ) + horizontalInsets
            )
        }
    }
}

@Serializable
data object SettingsPage
