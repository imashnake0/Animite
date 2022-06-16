package com.imashnake.animite.ui.elements.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.imashnake.animite.R
import com.imashnake.animite.ui.theme.NavigationBar
import com.imashnake.animite.ui.theme.Text
import com.imashnake.animite.ui.theme.manropeFamily

@ExperimentalMaterial3Api
@Composable
fun CollapsedSearchBar(modifier: Modifier) {
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
@Composable
fun ExpandedSearchBar(modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = NavigationBar,
        shadowElevation = 20.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    // TODO: Perform network operation.
                    Log.d("TextField", it)
                },
                label = {
                    Text(
                        text = "Search",
                        color = Text.copy(alpha = 0.5F),
                        fontSize = 12.sp,
                        maxLines = 1,
                        fontFamily = manropeFamily,
                        fontWeight = FontWeight.Medium
                    )
                },
                modifier = Modifier.wrapContentWidth(),
                textStyle = TextStyle(
                    color = Text,
                    fontSize = 12.sp,
                    fontFamily = manropeFamily,
                    fontWeight = FontWeight.Medium
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Text,
                    focusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search),
                        contentDescription = "Search",
                        tint = Text,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewSearchBar() {
    Column(modifier = Modifier.padding(200.dp), ) {
        CollapsedSearchBar(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(20.dp))
        ExpandedSearchBar(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
