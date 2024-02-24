package com.imashnake.animite.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.boswelja.markdown.material3.MarkdownDocument
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.core.extensions.animiteBlockQuoteStyle
import com.imashnake.animite.core.extensions.animiteCodeBlockStyle
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.extensions.maxHeight
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.NestedScrollableContent
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.imashnake.animite.core.R as coreR

@Destination(
    route = "user",
    deepLinks = [
        DeepLink(
            uriPattern = ANILIST_AUTH_DEEPLINK
        )
    ]
)
@RootNavGraph(start = true)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    accessToken: String? = null
) {
    accessToken?.let { viewModel.setAccessToken(it) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    val viewer by viewModel.viewer.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (!isLoggedIn) {
            Login()
        } else {
            viewer.data?.run {
                BannerLayout(
                    banner = {
                        Box {
                            AsyncImage(
                                model = bannerImage,
                                contentDescription = "banner",
                                modifier = it,
                                contentScale = ContentScale.Crop
                            )
                            AsyncImage(
                                model = avatar?.large,
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .landscapeCutoutPadding()
                                    .padding(start = LocalPaddings.current.medium)
                                    .size(100.dp)
                            )
                        }
                    },
                    content = {
                        val textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)

                        Column {
                            Text(
                                text = name,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis
                            )
                            Box(Modifier.maxHeight(dimensionResource(R.dimen.user_about_height))) {
                                NestedScrollableContent { contentModifier ->
                                    about?.let {
                                        MarkdownDocument(
                                            markdown = it,
                                            // TODO: Fix typography and make this an `animiteTextStyle()`.
                                            textStyles = m3TextStyles().copy(
                                                textStyle = m3TextStyles().textStyle.copy(
                                                    color = textColor
                                                )
                                            ),
                                            blockQuoteStyle = animiteBlockQuoteStyle(),
                                            codeBlockStyle = animiteCodeBlockStyle(),
                                            modifier = contentModifier
                                        )
                                    }
                                }
                            }
                        }
                    },
                    contentModifier = Modifier
                        .landscapeCutoutPadding()
                        .padding(
                            top = LocalPaddings.current.large,
                            start = LocalPaddings.current.large,
                            end = LocalPaddings.current.large,
                            bottom = dimensionResource(coreR.dimen.navigation_bar_height)
                        )
                )
            }
        }
    }
}
