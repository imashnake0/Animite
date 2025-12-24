package com.imashnake.animite.settings

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.extensions.copy
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.layouts.banner.BannerLayout
import com.imashnake.animite.core.ui.layouts.banner.MountFuji
import com.imashnake.animite.navigation.R as navigationR
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(
            bottom = dimensionResource(navigationR.dimen.navigation_bar_height)
        )
        else -> PaddingValues(
            start = dimensionResource(navigationR.dimen.navigation_rail_width)
        )
    }
    val horizontalInsets = insetPaddingValues.horizontalOnly

    val scrollState = rememberScrollState()

    var selectedTheme by remember { mutableStateOf(THEME.DARK) }

    TranslucentStatusBarLayout(scrollState) {
        Box(modifier.verticalScroll(scrollState)) {
            BannerLayout(
                banner = { bannerModifier ->
                    MountFuji(bannerModifier)

                    Row(
                        modifier = bannerModifier
                            .padding(
                                horizontal = LocalPaddings.current.large,
                                vertical = LocalPaddings.current.medium
                            )
                            .padding(insetPaddingValues.copy(bottom = 0.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text(
                            text = stringResource(R.string.settings),
                            color = Color(0xB5001626),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .padding(navigationComponentPaddingValues.horizontalOnly),
                            maxLines = 1
                        )
                    }
                },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)) {
                        Text(
                            text = stringResource(R.string.theme),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)) {
                            THEME.entries.forEach { theme ->
                                ToggleButton(
                                    checked = selectedTheme == theme,
                                    onCheckedChange = { selectedTheme = theme },
                                    shapes = when (theme) {
                                        THEME.DARK -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                        THEME.DEVICE_THEME -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                    },
                                ) {
                                    Text(stringResource(theme.theme))
                                }
                            }
                        }
                    }
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

enum class THEME(@param:StringRes val theme: Int) {
    DARK(R.string.dark),
    LIGHT(R.string.light),
    DEVICE_THEME(R.string.device_theme),
}

@Serializable
data object SettingsPage
