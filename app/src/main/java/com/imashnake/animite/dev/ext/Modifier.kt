package com.imashnake.animite.dev.ext

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// TODO: Use a `Scaffold`.
val bottomNavigationBarSize = 65.dp
fun Modifier.bottomNavigationBarPadding(): Modifier = this.padding(bottom = bottomNavigationBarSize)
