package com.imashnake.animite.core.extensions

import kotlin.math.*

/**
 * I made this before I discovered [android.graphics.Color.parseColor] (thanks boswelja :kappa:).
 */
fun String.toHexColor(): Long {
    var value = 0
    if(this.length == 7) {
        this.forEachIndexed { index, char ->
            when(char) {
                '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    value += (char.code - 48)*((16f.pow(6 - index)).toInt())
                }
                'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' -> {
                    value += (char.code - 87)*((16f.pow(6 - index)).toInt())
                }
            }
        }
    }
    return value + 0xFF000000
}
