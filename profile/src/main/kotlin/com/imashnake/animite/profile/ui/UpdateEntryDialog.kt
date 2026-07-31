package com.imashnake.animite.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.Divider
import com.imashnake.animite.core.ui.component.MediaMediumCard
import com.imashnake.animite.media.ext.res

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateEntryDialog(
    item: Media.Tracking,
    bannerImage: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(18.dp + LocalPaddings.current.large),
            modifier = modifier.fillMaxWidth()
        ) {
            Box {
                Column {
                    // Banner image
                    Box(Modifier.background(Color.Green).fillMaxWidth().aspectRatio(19f / 4))

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
        }
    }
}
