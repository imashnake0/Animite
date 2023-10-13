package com.imashnake.animite.profile

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.R as coreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination(route = "profile-screen")
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box {
            Image(
                painter = painterResource(coreR.drawable.background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(coreR.dimen.banner_height)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter
            )

            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.secondaryContainer.copy(
                                    alpha = 0.5f
                                )
                            )
                        )
                    )
                    .fillMaxWidth()
                    .height(dimensionResource(coreR.dimen.banner_height))
            ) { }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(coreR.string.coming_soon),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(coreR.dimen.large_padding),
                            bottom = dimensionResource(coreR.dimen.medium_padding)
                        )
                        .landscapeCutoutPadding()
                        .weight(1f, fill = false),
                    maxLines = 1
                )
            }
        }

        val orbitColor = MaterialTheme.colorScheme.onSecondaryContainer
        val infiniteTransition = rememberInfiniteTransition(label = "revolutions")
        val angle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "revolve_circle"
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(-1f)
        ) {
            val pivot = this.center + Offset(this.size.width/2, -this.size.height/3)
            val roundedRectangleSize = size.minDimension/7

            translate(
                left = this.size.width - roundedRectangleSize,
                top = this.center.y - this.size.height/3 - roundedRectangleSize
            ) {
                rotate(angle, pivot = Offset(roundedRectangleSize, roundedRectangleSize)) {
                    drawRoundRect(
                        color = orbitColor.copy(alpha = 0.9f),
                        size = Size(roundedRectangleSize*2, roundedRectangleSize*2),
                        cornerRadius = CornerRadius(
                            roundedRectangleSize/1.5f,
                            roundedRectangleSize/1.5f
                        )
                    )
                }
            }

            rotate(
                degrees = angle,
                pivot = pivot
            ) {
                translate(
                    left = this.size.width - roundedRectangleSize/2,
                    top = this.center.y - this.size.height/3 - roundedRectangleSize/2 + size.minDimension/2
                ) {
                    rotate(angle, pivot = Offset(roundedRectangleSize/2, roundedRectangleSize/2)) {
                        drawRoundRect(
                            color = orbitColor.copy(alpha = 0.7f),
                            size = Size(roundedRectangleSize, roundedRectangleSize),
                            cornerRadius = CornerRadius(
                                roundedRectangleSize/4,
                                roundedRectangleSize/4
                            )
                        )
                    }
                }

                translate(
                    left = this.size.width - roundedRectangleSize/2,
                    top = this.center.y - this.size.height/3 - roundedRectangleSize/2 - size.minDimension/2
                ) {
                    rotate(-2.5f*angle, pivot = Offset(roundedRectangleSize/2, roundedRectangleSize/2)) {
                        drawRoundRect(
                            color = orbitColor.copy(alpha = 0.4f),
                            size = Size(roundedRectangleSize, roundedRectangleSize),
                            cornerRadius = CornerRadius(
                                roundedRectangleSize/4,
                                roundedRectangleSize/4
                            )
                        )
                    }
                }

                translate(left = size.minDimension/2) {
                    drawCircle(
                        radius = size.minDimension/12,
                        color = orbitColor.copy(alpha = 0.2f),
                        center = pivot
                    )
                }

                translate(left = -size.minDimension/2) {
                    drawCircle(
                        radius = size.minDimension/17,
                        color = orbitColor.copy(alpha = 0.5f),
                        center = pivot
                    )
                }

                translate(top = roundedRectangleSize/4) {
                    drawCircle(
                        radius = size.minDimension/3,
                        color = orbitColor.copy(alpha = 0.2f),
                        center = this.center + Offset(this.size.width/2, -this.size.height/3),
                        style = Stroke(width = 5f)
                    )
                }
            }

            drawCircle(
                radius = size.minDimension/2,
                color = orbitColor,
                center = this.center + Offset(this.size.width/2, -this.size.height/3),
                style = Stroke(width = 5f)
            )
        }
    }
}
