package com.imashnake.animite.core.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import org.intellij.lang.annotations.Language

@Language("AGSL")
val sun = """
  uniform float2 resolution;
  
  half4 main(float2 coord) {
      float2 uv = coord.xy / resolution;
          
      return half4(uv.x, uv.y, 1.0, 1.0);
  }
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun PreviewSunShader() {
    val sunShader = remember { RuntimeShader(sun) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .drawWithCache {
                with(sunShader) {
                    setFloatUniform(
                        "resolution",
                        size.width,
                        size.height
                    )
                }
                onDrawBehind {
                    drawRect(brush = ShaderBrush(sunShader))
                }
            }
    )
}
