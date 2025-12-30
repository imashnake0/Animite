package com.imashnake.animite.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.core.extensions.copy
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.layouts.banner.BannerLayout
import com.imashnake.animite.core.ui.layouts.banner.MountFuji
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val horizontalInsets = insetPaddingValues.horizontalOnly

    val scrollState = rememberScrollState()

    val selectedTheme by viewModel.theme.filterNotNull().collectAsState(initial = THEME.DEVICE_THEME.name)
    val useSystemColorScheme by viewModel.useSystemColorScheme.filterNotNull().collectAsState(initial = true)
    val haptic = LocalHapticFeedback.current

    var useColorScheme by remember { mutableStateOf(true) }

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
                            modifier = Modifier.weight(1f, fill = false),
                            maxLines = 1
                        )
                    }
                },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)) {
                        Text(
                            text = stringResource(R.string.appearance),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Items(
                            items = listOf(
                                Item(
                                    icon = R.drawable.theme,
                                    label = R.string.theme
                                ),
                                Item(
                                    icon = R.drawable.palette,
                                    label = R.string.palette
                                )
                            ),
                            isDarkMode = selectedTheme == THEME.DARK.name ||
                                    (selectedTheme == THEME.DEVICE_THEME.name && isSystemInDarkTheme()),
                            modifier = Modifier.fillMaxWidth()
                        ) { index ->
                            when (index) {
                                0 -> Row(horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)) {
                                    THEME.entries.forEach { theme ->
                                        ToggleButton(
                                            checked = selectedTheme == theme.name,
                                            onCheckedChange = {
                                                viewModel.setTheme(theme)
                                                haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                            },
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
                                1 -> {
                                    Switch(
                                        checked = useSystemColorScheme,
                                        onCheckedChange = { viewModel.setUseSystemColorScheme(it) },
                                        thumbContent = {
                                            if (useColorScheme) {
                                                Icon(
                                                    imageVector = Icons.Filled.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                                )
                                            }
                                        },
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                },
                contentPadding = PaddingValues(
                    top = LocalPaddings.current.large,
                    bottom = LocalPaddings.current.large + insetPaddingValues.calculateBottomPadding(),
                    start = LocalPaddings.current.large,
                    end = LocalPaddings.current.large,
                ) + horizontalInsets
            )
        }
    }
}

@Composable
private fun Items(
    items: List<Item>,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    itemContent: @Composable (Int) -> Unit,
) {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
            modifier = modifier
        ) {
            items.forEachIndexed { index, item ->
                val topPercent = when (index) {
                    0 -> 50
                    else -> 10
                }
                val bottomPercent = when (index) {
                    items.lastIndex -> 50
                    else -> 10
                }

                Item(
                    item = index to item,
                    shape = RoundedCornerShape(
                        topStartPercent = topPercent,
                        topEndPercent = topPercent,
                        bottomEndPercent = bottomPercent,
                        bottomStartPercent = bottomPercent,
                    ),
                    background = if (isDarkMode) {
                        MaterialTheme.colorScheme.surfaceContainer
                    } else MaterialTheme.colorScheme.surface
                ) { index ->
                    itemContent(index)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Item(
    item: Pair<Int, Item>,
    shape: Shape,
    background: Color,
    modifier: Modifier = Modifier,
    content: @Composable (index: Int) -> Unit,
) {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .background(background)
                .padding(LocalPaddings.current.medium)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(item.second.icon),
                contentDescription = null,
                modifier = Modifier.padding(start = LocalPaddings.current.small)
            )
            Text(
                text = stringResource(item.second.label),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
            )
            content(item.first)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun PreviewItems() {
    var selectedTheme by remember { mutableStateOf(THEME.DARK) }
    var useColorScheme by remember { mutableStateOf(true) }
    Items(
        items = listOf(
            Item(
                icon = R.drawable.theme,
                label = R.string.theme
            ),
            Item(
                icon = R.drawable.palette,
                label = R.string.palette
            )
        ),
        isDarkMode = false,
        modifier = Modifier.fillMaxWidth()
    ) { index ->
        when (index) {
            0 -> Row(horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)) {
                THEME.entries.forEach { theme ->
                    ToggleButton(
                        checked = selectedTheme.name == theme.name,
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
            1 -> {
                Switch(
                    checked = useColorScheme,
                    onCheckedChange = { useColorScheme = it },
                    thumbContent = {
                        if (useColorScheme) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                )
            }
            else -> {}
        }
    }
}

private data class Item(
    @param:DrawableRes val icon: Int,
    @param:StringRes val label: Int,
)

enum class THEME(@param:StringRes val theme: Int) {
    DARK(R.string.dark),
    LIGHT(R.string.light),
    DEVICE_THEME(R.string.device),
}

@Serializable
data object SettingsPage
