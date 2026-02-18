package com.imashnake.animite.core.ui.shaders

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.imashnake.animite.core.R
import org.intellij.lang.annotations.Language

/**
 * Night sky shader from [shadertoy](https://www.shadertoy.com/view/Md2SR3).
 * @author xaot88
 */
@Language("AGSL")
val nightSky = """
uniform float2 resolution;
    
// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.

// Return random noise in the range [0.0, 1.0], as a function of x.
float noise2d(float2 x) {
    float xhash = cos(x.x * 37.0);
    float yhash = cos(x.y * 57.0);
    return fract(415.92653 * ( xhash + yhash ));
}

// Convert noise2d() into a "star field" by stomping everthing below fThreshhold to zero.
float noisyStarField(in float2 vSamplePos, float fThreshhold) {
    float starVal = noise2d(vSamplePos);
    if (starVal >= fThreshhold)
        starVal = pow((starVal - fThreshhold) / (1.0 - fThreshhold), 6.0);
    else
        starVal = 0.0;
    return starVal;
}

// Stabilize noisyStarField() by only sampling at integer values.
float stableStarField(in float2 vSamplePos, float fThreshhold) {
    // Linear interpolation between four samples.
    // Note: This approach has some visual artifacts.
    // There must be a better way to "anti alias" the star field.
    float fractX = fract(vSamplePos.x);
    float fractY = fract(vSamplePos.y);
    float2 floorSample = floor(vSamplePos);
    float v1 = noisyStarField(floorSample, fThreshhold);
    float v2 = noisyStarField(floorSample + float2(0.0, 1.0), fThreshhold);
    float v3 = noisyStarField(floorSample + float2(1.0, 0.0), fThreshhold);
    float v4 = noisyStarField(floorSample + float2(1.0, 1.0), fThreshhold);

    float starVal = v1 * (1.0 - fractX) * (1.0 - fractY)
        + v2 * (1.0 - fractX) * fractY
        + v3 * fractX * (1.0 - fractY)
        + v4 * fractX * fractY;
    return starVal;
}

half4 main(float2 coord) {
    // Sky Background Color
    float3 color = float3(0.0, 0.0, 0.0) * coord.y / resolution.y;

    // Note: Choose fThreshhold in the range [0.99, 0.9999].
    // Higher values (i.e., closer to one) yield a sparser starfield.
    float StarFieldThreshhold = 0.99;

    // Stars with a slow crawl.
    float xRate = 0.0;
    float yRate = -0.00;
    float starVal = stableStarField(coord, StarFieldThreshhold);
    color += float3(starVal);

    return half4(color, 1.0);
}
""".trimIndent()

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun PreviewNightSkyShader() {
    val nightSky = remember { RuntimeShader(nightSky) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.banner_height))
            .drawWithCache {
                with(nightSky) {
                    setFloatUniform(
                        "resolution",
                        size.width,
                        size.height
                    )
                }
                onDrawBehind {
                    drawRect(brush = ShaderBrush(nightSky))
                }
            }
    )
}
