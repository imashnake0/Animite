package com.imashnake.animite.core.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.imashnake.animite.core.R
import org.intellij.lang.annotations.Language
import kotlin.math.PI

@Language("AGSL")
val sun = """
  uniform float2 resolution;
  // 0 -> 2 * PI
  uniform float radius;
  uniform float time;
  uniform float PI;
  
  half4 main(float2 coord) {
      half3 sunColor = half3(1.0, 1.0, 0.8);
      
      // calculate cycloid's x and y based on angle and radius
      // todo: make it an ellipse???
      float t = mod(time, 2.0 * PI);
      float r = radius;
      float x = r * (t - sin(t));
      float y = resolution.y - (r * (1 - cos(t)));
      float2 center = float2(x, y);
      float d = distance(center, coord) / resolution.y;
      
      // rgba
      return half4(sunColor,  (0.05 / d) - 0.1);
  }
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun PreviewSunShader() {
    val time = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        do {
            withFrameMillis {
                time.floatValue += 0.01f
            }
        } while (true)
    }
    val sunShader = remember { RuntimeShader(sun) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.banner_height))
            .drawWithCache {
                with(sunShader) {
                    setFloatUniform(
                        "resolution",
                        size.width,
                        size.height
                    )
                    setFloatUniform(
                        "radius",
                        size.height / 2.7f,
                    )
                    setFloatUniform("time", time.floatValue)
                    setFloatUniform("PI", PI.toFloat())
                }
                onDrawBehind {
                    drawRect(brush = ShaderBrush(sunShader))
                }
            }
    )
}
