package com.imashnake.animite.settings

import android.graphics.Bitmap
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.imashnake.animite.core.extensions.DayPart
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.extensions.toDayPart
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.layouts.banner.BannerLayout
import com.imashnake.animite.core.ui.layouts.banner.MountFuji
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.filterNotNull

private const val DISCORD_URL = "https://discord.gg/HEB7duYdqe"
private const val GITHUB_URL = "https://github.com/imashnake0/Animite/"
private const val PRIVACY_POLICY = "https://imashnake.deno.dev/animite.html"
private const val SYSTEM_DAY_PART = "SYSTEM"
private const val ANIMITE = "Animite"

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsPage(
    versionName: String,
    modifier: Modifier = Modifier,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val horizontalInsets = insetPaddingValues.horizontalOnly

    val scrollState = rememberScrollState()

    val selectedTheme by viewModel.theme.filterNotNull().collectAsState(initial = Theme.DEVICE_THEME.name)
    val useSystemColorScheme by viewModel.useSystemColorScheme.filterNotNull().collectAsState(initial = true)
    val haptic = LocalHapticFeedback.current

    val isDevOptionsEnabled by viewModel.isDevOptionsEnabled.filterNotNull().collectAsState(initial = false)

    val isDarkMode = selectedTheme == Theme.DARK.name ||
            (selectedTheme == Theme.DEVICE_THEME.name && isSystemInDarkTheme())

    var devOptionsCount by remember { mutableIntStateOf(0) }

    val dayHour by viewModel.dayHour.collectAsState(initial = null)

    TranslucentStatusBarLayout(scrollState) {
        Box(modifier.verticalScroll(scrollState)) {
            BannerLayout(
                banner = { bannerModifier ->
                    MountFuji(
                        setDayHour = dayHour,
                        header = stringResource(R.string.settings),
                        insetPaddingValues = insetPaddingValues,
                        modifier = bannerModifier,
                    )
                },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)) {
                        Text(
                            text = stringResource(R.string.appearance),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Items(
                            items = persistentListOf(
                                Item(
                                    icon = R.drawable.theme,
                                    label = R.string.theme,
                                    orientation = Item.Orientation.VERTICAL
                                ),
                                Item(
                                    icon = R.drawable.palette,
                                    label = R.string.palette,
                                    orientation = Item.Orientation.HORIZONTAL
                                )
                            ),
                            onItemClick = { index ->
                                when (index) {
                                    1 -> {
                                        viewModel.setUseSystemColorScheme(!useSystemColorScheme)
                                        haptic.performHapticFeedback(
                                            hapticFeedbackType = if (useSystemColorScheme) {
                                                HapticFeedbackType.ToggleOff
                                            } else HapticFeedbackType.ToggleOn
                                        )
                                    }

                                    else -> {}
                                }
                            },
                            isDarkMode = isDarkMode,
                            modifier = Modifier.fillMaxWidth()
                        ) { index ->
                            when (index) {
                                0 -> Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        ButtonGroupDefaults.ConnectedSpaceBetween
                                    )
                                ) {
                                    Theme.entries.forEach { theme ->
                                        ToggleButton(
                                            checked = selectedTheme == theme.name,
                                            onCheckedChange = {
                                                viewModel.setTheme(theme)
                                                haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                                            },
                                            shapes = when (theme) {
                                                Theme.DARK -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                                Theme.DEVICE_THEME -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                            },
                                            colors = ToggleButtonDefaults.toggleButtonColors(
                                                containerColor = MaterialTheme.colorScheme.background
                                            ),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(stringResource(theme.theme))
                                        }
                                    }
                                }

                                1 -> {
                                    Switch(
                                        checked = useSystemColorScheme,
                                        onCheckedChange = {
                                            viewModel.setUseSystemColorScheme(it)
                                            haptic.performHapticFeedback(
                                                hapticFeedbackType = if (!it) {
                                                    HapticFeedbackType.ToggleOff
                                                } else HapticFeedbackType.ToggleOn
                                            )
                                        },
                                        thumbContent = {
                                            if (useSystemColorScheme) {
                                                Icon(
                                                    imageVector = Icons.Filled.Check,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        },
                                    )
                                }

                                else -> {}
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)) {
                        Text(
                            text = stringResource(R.string.about),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)) {
                            AboutItem(
                                isDarkMode = isDarkMode,
                                versionName = versionName,
                                isDevOptionsEnabled = isDevOptionsEnabled,
                                devOptionsCount = devOptionsCount,
                                onClick = { devOptionsCount++ },
                                enableDevOptions = { viewModel.setDevOptions(true) },
                                disableDevOptions = {
                                    devOptionsCount = 0; viewModel.setDevOptions(false)
                                }
                            )

                            PrivacyPolicyItem(isDarkMode = isDarkMode)
                        }
                    }

                    AnimatedVisibility(
                        enter = fadeIn(),
                        exit = fadeOut(),
                        visible = isDevOptionsEnabled
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)) {
                            Text(
                                text = stringResource(R.string.dev_options),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                            )

                            Items(
                                items = persistentListOf(
                                    Item(
                                        icon = R.drawable.day_hour,
                                        label = R.string.day_hour,
                                        orientation = Item.Orientation.VERTICAL
                                    ),
                                    Item(
                                        icon = R.drawable.day_part,
                                        label = R.string.day_part,
                                        orientation = Item.Orientation.VERTICAL
                                    ),
                                ),
                                onItemClick = {},
                                isDarkMode = isDarkMode,
                            ) { index ->
                                when (index) {
                                    0 -> {
                                        Slider(
                                            value = animateFloatAsState(dayHour ?: 0f).value,
                                            onValueChange = { viewModel.setDayHour(it) },
                                            valueRange = 0f..24f,
                                        )
                                    }
                                    1 -> {
                                        Column(verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)) {
                                            DayPart.entries.map { it.name }.plus(SYSTEM_DAY_PART).forEach {
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .padding(start = LocalPaddings.current.large)
                                                        .clip(CircleShape)
                                                        .clickable {
                                                            if (it != SYSTEM_DAY_PART) {
                                                                viewModel.setDayHour(
                                                                    when(DayPart.valueOf(it)) {
                                                                        DayPart.MORNING -> 6f
                                                                        DayPart.AFTERNOON -> 12f
                                                                        DayPart.EVENING -> 18f
                                                                        DayPart.NIGHT -> 21f
                                                                    }
                                                                )
                                                            } else viewModel.setDayHour(null)
                                                        }
                                                        .padding(
                                                            top = LocalPaddings.current.tiny,
                                                            bottom = LocalPaddings.current.tiny,
                                                            start = LocalPaddings.current.tiny,
                                                            end = LocalPaddings.current.medium
                                                        )
                                                ) {
                                                    RadioButton(
                                                        selected = if (dayHour == null) it == SYSTEM_DAY_PART else it == dayHour?.toDayPart()?.name,
                                                        onClick = null,
                                                    )
                                                    Text(
                                                        text = it,
                                                        style = MaterialTheme.typography.labelSmallEmphasized,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
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
    items: ImmutableList<Item>,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit,
    itemContent: @Composable (Int) -> Unit,
) {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
            modifier = modifier
        ) {
            items.forEachIndexed { index, item ->
                val top = when (index) {
                    0 -> LocalPaddings.current.large
                    else -> LocalPaddings.current.tiny
                }
                val bottom = when (index) {
                    items.lastIndex -> LocalPaddings.current.large
                    else -> LocalPaddings.current.tiny
                }

                when (item.orientation) {
                    Item.Orientation.HORIZONTAL -> HorizontalItem(
                        item = index to item,
                        shape = RoundedCornerShape(
                            topStart = top,
                            topEnd = top,
                            bottomEnd = bottom,
                            bottomStart = bottom,
                        ),
                        onItemClick = { onItemClick(index) },
                        background = if (isDarkMode) {
                            MaterialTheme.colorScheme.surfaceContainer
                        } else MaterialTheme.colorScheme.surface
                    ) {
                        itemContent(index)
                    }
                    Item.Orientation.VERTICAL -> VerticalItem(
                        item = index to item,
                        shape = RoundedCornerShape(
                            topStart = top,
                            topEnd = top,
                            bottomEnd = bottom,
                            bottomStart = bottom,
                        ),
                        onItemClick = { onItemClick(index) },
                        background = if (isDarkMode) {
                            MaterialTheme.colorScheme.surfaceContainer
                        } else MaterialTheme.colorScheme.surface
                    ) {
                        itemContent(index)
                    }
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun HorizontalItem(
    item: Pair<Int, Item>,
    shape: Shape,
    background: Color,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPaddings provides rememberDefaultPaddings(),
        // Remove default M3 padding
        LocalMinimumInteractiveComponentSize provides 0.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .clickable { onItemClick() }
                .background(background)
                .padding(LocalPaddings.current.medium)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(item.second.icon),
                contentDescription = null,
            )
            Text(
                text = stringResource(item.second.label),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
            )
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun VerticalItem(
    item: Pair<Int, Item>,
    shape: Shape,
    background: Color,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPaddings provides rememberDefaultPaddings(),
        // Remove default M3 padding
        LocalMinimumInteractiveComponentSize provides 0.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
            modifier = modifier
                .fillMaxWidth()
                .clip(shape)
                .clickable { onItemClick() }
                .background(background)
                .padding(LocalPaddings.current.medium)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(item.second.icon),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(item.second.label),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f),
                )
            }
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AboutItem(
    isDarkMode: Boolean,
    versionName: String,
    isDevOptionsEnabled: Boolean,
    devOptionsCount: Int,
    onClick: () -> Unit,
    enableDevOptions: () -> Unit,
    disableDevOptions: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        targetValue = if (isDarkMode) Color(0x190FFF66) else Color(0x59FFC0CB),
        animationSpec = tween(500)
    )
    val textColor by animateColorAsState(
        targetValue = if (isDarkMode) Color(0xFF3BFF84) else Color(0xFFDA6482),
        animationSpec = tween(500)
    )
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val stepsFromDeveloper = pluralStringResource(
        id = R.plurals.almost_developer,
        count = 10 - devOptionsCount,
        10 - devOptionsCount
    )
    var toast: Toast? by remember { mutableStateOf(null) }

    val haptic = LocalHapticFeedback.current

    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = LocalPaddings.current.large,
                        topEnd = LocalPaddings.current.large,
                        bottomStart = LocalPaddings.current.small,
                        bottomEnd = LocalPaddings.current.small
                    )
                )
                .combinedClickable(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                        if (toast != null) {
                            toast?.cancel()
                        }
                        if (isDevOptionsEnabled) {
                            toast = Toast.makeText(context, R.string.already_developer, Toast.LENGTH_SHORT)
                            toast?.show()
                            return@combinedClickable
                        }
                        if (devOptionsCount < 10) {
                            onClick()
                            toast = Toast.makeText(
                                context,
                                stepsFromDeveloper,
                                Toast.LENGTH_SHORT
                            )
                        } else if (devOptionsCount == 10) {
                            enableDevOptions()
                            onClick()
                            toast = Toast.makeText(context, R.string.now_developer, Toast.LENGTH_SHORT)
                        }
                        toast?.show()
                    },
                    onLongClick = {
                        if (isDevOptionsEnabled) {
                            if (toast != null) {
                                toast?.cancel()
                            }
                            disableDevOptions()
                            toast = Toast.makeText(context, R.string.disabled_dev_options, Toast.LENGTH_SHORT)
                            toast?.show()
                        }
                    }
                )
                .background(background)
                .padding(LocalPaddings.current.medium)
                .height(IntrinsicSize.Min)
        ) {
            if (LocalInspectionMode.current) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x4F0A1625))
                )
            } else {
                val drawable = LocalContext.current.packageManager
                    .getApplicationIcon(LocalContext.current.packageName)
                Image(
                    bitmap = drawable
                        .toBitmap(config = Bitmap.Config.ARGB_8888)
                        .asImageBitmap(),
                    contentDescription = stringResource(R.string.app_icon),
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxHeight().weight(1f)
            ) {
                Text(
                    text = ANIMITE,
                    color = textColor,
                    style = MaterialTheme.typography.titleSmallEmphasized,
                )
                Text(
                    text = "v$versionName",
                    color = textColor.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelSmallEmphasized,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { uriHandler.openUri(DISCORD_URL) }
                        .background(Color(0xFF5865f2))
                        .size(28.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.discord),
                        contentDescription = stringResource(R.string.discord),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { uriHandler.openUri(GITHUB_URL) }
                        .background(Color.White)
                        .size(28.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.github),
                        contentDescription = stringResource(R.string.github),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PrivacyPolicyItem(
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        targetValue = if (isDarkMode) Color(0x190FFF66) else Color(0x59FFC0CB),
        animationSpec = tween(500)
    )
    val uriHandler = LocalUriHandler.current

    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = LocalPaddings.current.small,
                        topEnd = LocalPaddings.current.small,
                        bottomStart = LocalPaddings.current.large,
                        bottomEnd = LocalPaddings.current.large
                    )
                )
                .clickable {}
                .background(background)
                .padding(LocalPaddings.current.medium)
                .height(IntrinsicSize.Min)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.privacy_policy),
                contentDescription = stringResource(R.string.privacy_policy),
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = stringResource(R.string.privacy_policy),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.open_link),
                contentDescription = stringResource(R.string.privacy_policy),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { uriHandler.openUri(PRIVACY_POLICY) }
                    .padding(LocalPaddings.current.small)
                    .size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(
    showBackground = true,
    backgroundColor = 0xFFE0E4ED
)
@Composable
private fun PreviewItems() {
    var selectedTheme by remember { mutableStateOf(Theme.DARK) }
    var useColorScheme by remember { mutableStateOf(true) }

    val padding = 10.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = Modifier.padding(vertical = padding)
    ) {
        Items(
            items = persistentListOf(
                Item(
                    icon = R.drawable.theme,
                    label = R.string.theme,
                    orientation = Item.Orientation.VERTICAL
                ),
                Item(
                    icon = R.drawable.palette,
                    label = R.string.palette,
                    orientation = Item.Orientation.HORIZONTAL
                )
            ),
            onItemClick = {},
            isDarkMode = false,
            modifier = Modifier.fillMaxWidth().padding(horizontal = padding)
        ) { index ->
            when (index) {
                0 -> Row(
                    horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                ) {
                    Theme.entries.forEach { theme ->
                        ToggleButton(
                            checked = selectedTheme == theme,
                            onCheckedChange = { selectedTheme = theme },
                            shapes = when (theme) {
                                Theme.DARK -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                Theme.DEVICE_THEME -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                            modifier = Modifier.weight(1f)
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

        AboutItem(
            isDarkMode = false,
            versionName = "0.0.0-alpha0",
            isDevOptionsEnabled = false,
            devOptionsCount = 0,
            onClick = {},
            enableDevOptions = {},
            disableDevOptions = {},
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

private data class Item(
    @param:DrawableRes val icon: Int,
    @param:StringRes val label: Int,
    val orientation: Orientation,
) {
    enum class Orientation {
        HORIZONTAL, VERTICAL
    }
}


enum class Theme(@param:StringRes val theme: Int) {
    DARK(R.string.dark),
    LIGHT(R.string.light),
    DEVICE_THEME(R.string.system),
}

