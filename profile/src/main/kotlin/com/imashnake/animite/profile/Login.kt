package com.imashnake.animite.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import core.ui.LocalPaddings
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_URL

@Composable
fun Login(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    OutlinedButton(
        onClick = { uriHandler.openUri(ANILIST_AUTH_URL) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1E2630),
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Row(Modifier.wrapContentWidth()) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_anilist),
                contentDescription = "AniList icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Log in",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = LocalPaddings.current.tiny)
            )
        }
    }
}
