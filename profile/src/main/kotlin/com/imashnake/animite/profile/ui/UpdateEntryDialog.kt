package com.imashnake.animite.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import com.imashnake.animite.media.R as mediaR

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateEntryDialog(
    item: Media.Tracking,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(18.dp + LocalPaddings.current.large),
            modifier = modifier.fillMaxWidth()
        ) {
            // region header
            Box {
                Column {
                    Box {
                        val tint = item.color?.toColorInt()
                            ?.let { int -> Color(int) }
                            ?.copy(alpha = 0.25f)
                            ?: Color.Transparent

                        Box(
                            modifier = Modifier
                                .background(tint)
                                .fillMaxWidth()
                                .aspectRatio(19f / 4)
                        )

                        AsyncImage(
                            model = crossfadeModel(item.bannerImage),
                            contentDescription = null,
                            error = painterResource(mediaR.drawable.background),
                            fallback = painterResource(mediaR.drawable.background),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            colorFilter = ColorFilter.tint(
                                color = tint,
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
                            text = item.title.orEmpty(),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            item.format?.let {
                                Text(
                                    text = stringResource(it.res),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (item.season != null && item.format != null) {
                                Divider(shape = MaterialShapes.Triangle.toShape())
                            }

                            item.season?.let {
                                Text(
                                    text = stringResource(it.res) +
                                            " ${item.seasonYear?.toString().orEmpty()}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                MediaMediumCard(
                    image = item.coverImage,
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
            // endregion

            // region entry options
            Column(
                modifier = Modifier.padding(
                    horizontal = LocalPaddings.current.large,
                    vertical = LocalPaddings.current.medium)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.large)) {
                    StatusDropDown(status = item.status)

                    item.score?.let { score ->
                        Text(
                            text = score.value.toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(score.color).copy(alpha = 0.8f),
                            modifier = Modifier.align(Alignment.CenterVertically).padding(top = 5.dp)
                        )
                    }
                }
            }
            // endregion
        }
    }
}

// TODO: This should be an actual dropdown.
@Composable
fun StatusDropDown(
    status: User.TrackingStatus,
    modifier: Modifier = Modifier
) {
    val iconPadding = (dimensionResource(R.dimen.tracking_list_header_height) - dimensionResource(R.dimen.tracking_list_header_icon_size)) / 2
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        modifier = modifier
            .height(dimensionResource(R.dimen.tracking_list_header_height))
            .clip(CircleShape)
            .clickable {}
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                    alpha = 0.95f
                )
            )
            .padding(iconPadding)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(status.res),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
        )
        Text(
            text = stringResource(status.title),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
        )
        DropDownIcon(isDroppedDown = false)
    }
}

