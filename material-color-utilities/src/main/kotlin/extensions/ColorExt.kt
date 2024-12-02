package extensions

import androidx.compose.ui.graphics.Color

/**
 * [androidx.core.graphics.ColorUtils.blendARGB] for [androidx.compose.ui.graphics.Color].
 */
fun Color.blendWith(other: Color, ratio: Float): Color {
    val inverseRatio = 1 - ratio
    val a = this.alpha * inverseRatio + other.alpha * ratio
    val r = this.red * inverseRatio + other.red * ratio
    val g = this.green * inverseRatio + other.green * ratio
    val b = this.blue * inverseRatio + other.blue * ratio
    return Color(alpha = a, red = r, green = g, blue = b)
}
