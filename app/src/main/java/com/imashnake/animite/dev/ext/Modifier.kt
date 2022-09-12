package com.imashnake.animite.dev.ext

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val bottomNavigationBarSize = 65.dp
fun Modifier.bottomNavigationBarPadding(): Modifier = this.padding(bottomNavigationBarSize)
