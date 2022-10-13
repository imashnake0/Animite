package com.imashnake.animite.dev.ext

import androidx.compose.ui.Modifier

fun Modifier.given(
    boolean: Boolean,
    execute: Modifier.() -> Modifier
): Modifier =
    if (boolean) {
        then(execute(Modifier))
    } else {
        this
    }
