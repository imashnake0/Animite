package com.imashnake.animite.ui.elements.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imashnake.animite.R
import com.imashnake.animite.ui.theme.NavigationBar
import com.imashnake.animite.ui.theme.Text

@ExperimentalMaterial3Api
@Composable
fun SearchBar(modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = NavigationBar,
        shadowElevation = 20.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                contentDescription = "Search",
                tint = Text
            )

            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "Profile",
                tint = Text
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewSearchBar() {
    Box(modifier = Modifier.padding(200.dp)) {
        SearchBar(modifier = Modifier.align(Alignment.BottomEnd))
    }
}
