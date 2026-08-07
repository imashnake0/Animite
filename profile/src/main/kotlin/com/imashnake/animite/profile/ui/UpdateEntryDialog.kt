package com.imashnake.animite.profile.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.Divider
import com.imashnake.animite.core.ui.component.DropDownIcon
import com.imashnake.animite.core.ui.component.MediaMediumCard
import com.imashnake.animite.core.ui.ext.crossfadeModel
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.dev.res
import com.imashnake.animite.profile.dev.title
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.media.R as mediaR

// TODO: This padding cannot be removed.
private val DropdownMenuItemHorizontalPadding = 12.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateEntryDialog(
    item: Media.Tracking,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatus by remember { mutableStateOf(item.status) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(18.dp + LocalPaddings.current.large),
            modifier = modifier.fillMaxWidth()
        ) {
            Header(
                title = item.title,
                coverImage = item.coverImage,
                bannerImage = item.bannerImage,
                format = item.format,
                season = item.season,
                seasonYear = item.seasonYear,
                tintColor = item.color?.toColorInt()
                    ?.let { int -> Color(int) }
                    ?.copy(alpha = 0.25f)
                    ?: Color.Transparent
            )

            Column(
                modifier = Modifier.padding(
                    horizontal = LocalPaddings.current.large,
                    vertical = LocalPaddings.current.medium
                )
            ) {
                Box(Modifier.fillMaxWidth()) {
                    StatusDropDown(
                        type = item.type,
                        selectedStatus = selectedStatus,
                        onSelectStatus = { selectedStatus = it },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    item.score?.let { score ->
                        Text(
                            text = score.value.toString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(score.color).copy(alpha = 0.8f),
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(0.5f)
                                .align(Alignment.CenterEnd)
                        )
                    }
                }

                Box(Modifier.background(Color.Green).height(500.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Header(
    coverImage: String?,
    title: String?,
    bannerImage: String?,
    format: Media.Format?,
    season: Media.Season?,
    seasonYear: Int?,
    tintColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Column {
            Box {
                Box(
                    modifier = Modifier
                        .background(tintColor)
                        .fillMaxWidth()
                        .aspectRatio(19f / 4)
                )

                AsyncImage(
                    model = crossfadeModel(bannerImage),
                    contentDescription = null,
                    error = painterResource(mediaR.drawable.background),
                    fallback = painterResource(mediaR.drawable.background),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(
                        color = tintColor,
                        blendMode = BlendMode.SrcAtop
                    ),
                    modifier = Modifier.fillMaxWidth().aspectRatio(19f / 4)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                modifier = Modifier.padding(
                    start = LocalPaddings.current.large + 96.dp + LocalPaddings.current.medium,
                    top = LocalPaddings.current.medium,
                    end = LocalPaddings.current.medium
                )
            ) {
                Text(
                    text = title.orEmpty(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    format?.let {
                        Text(
                            text = stringResource(it.res),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (season != null && format != null) {
                        Divider(shape = MaterialShapes.Triangle.toShape())
                    }

                    season?.let {
                        Text(
                            text = stringResource(it.res) +
                                    " ${seasonYear?.toString().orEmpty()}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        MediaMediumCard(
            image = coverImage,
            tag = null,
            label = null,
            onClick = {},
            tagMinLines = 1,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    top = LocalPaddings.current.large,
                    start = LocalPaddings.current.large
                )
        )
    }
}

// TODO: This should be an actual dropdown.
@Composable
fun StatusDropDown(
    type: Media.Small.Type,
    selectedStatus: User.TrackingStatus,
    onSelectStatus: (User.TrackingStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = if (type == Media.Small.Type.ANIME) {
        User.TrackingStatus.animeStatuses()
    } else User.TrackingStatus.mangaStatuses()
    val cascadeState = rememberCascadeState()
    var isStatusDropDownExpanded by remember { mutableStateOf(false) }

    val iconPadding = (dimensionResource(R.dimen.tracking_list_header_height) - dimensionResource(R.dimen.tracking_list_header_icon_size)) / 2
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(dimensionResource(R.dimen.tracking_list_header_height))
            .clip(CircleShape)
            .clickable { isStatusDropDownExpanded = true }
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                    alpha = 0.95f
                )
            )
            .padding(iconPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DropdownMenuItemHorizontalPadding + 4.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(selectedStatus.res),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
            )
            Text(
                text = stringResource(selectedStatus.title),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
                overflow = TextOverflow.Ellipsis,
                // DropDownIcon padding
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        DropDownIcon(isDroppedDown = isStatusDropDownExpanded)
    }

    CascadeDropdownMenu(
        expanded = isStatusDropDownExpanded,
        onDismissRequest = { isStatusDropDownExpanded = false },
        state = cascadeState,
        shape = RoundedCornerShape(dimensionResource(R.dimen.tracking_list_header_height) / 2),
        offset = DpOffset(x = 0.dp, y = LocalPaddings.current.tiny),
    ) {

        statuses.fastForEach {
            val backgroundColor by animateColorAsState(
                targetValue = if (it == selectedStatus)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                else Color.Transparent
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(it.title),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(it.res),
                        contentDescription = stringResource(it.title),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
                    )
                },
                onClick = {
                    onSelectStatus(it)
                    isStatusDropDownExpanded = false
                },
                contentPadding = PaddingValues(horizontal = iconPadding),
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.tracking_list_header_height) - LocalPaddings.current.tiny))
                    .background(color = backgroundColor)
                    .height(dimensionResource(R.dimen.tracking_list_header_height))
            )
        }
    }
}

