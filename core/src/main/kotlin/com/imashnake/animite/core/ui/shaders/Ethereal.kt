package com.imashnake.animite.core.ui.shaders

import org.intellij.lang.annotations.Language


@Language("AGSL")
val Util = """
  float pi = 3.1415926536;
  float twopi = 6.28318530718;
  
  float random(float2 st) {
      return fract(sin(dot(st.xy, float2(12.9898,78.233))) * 43758.5453123);
  }
  
  float plot(vec2 st, float pct){
      return smoothstep(pct - 0.01, pct, st.y) - smoothstep(pct, pct + 0.01, st.y);
  }
""".trimIndent()

@Language("AGSL")
val etherealShader = """
  $Util
    
  uniform float2 resolution;
  uniform float time;

  layout(color) uniform half4 color1;
  layout(color) uniform half4 color2;
      
  half4 main(float2 coord) {
      float2 uv = coord.xy / resolution;
      
//      // Show path
//      if (
//          uv.y < -sin(6 * uv.x)/4 + 0.5 &&
//          uv.y > -sin(6 * uv.x)/4 + 0.47
//      ) color = 0.0;
      
      // Moving center
      vec2 center = vec2(pow(sin(time * 0.2), 2.0) * resolution.x, (-sin(6 * pow(sin(time * 0.2), 2.0))/4 + 0.47) * resolution.y);
      
      // Radius and size of circular gradient
      float radius = 350.0 + (pow(sin(time), 2.0) * 50.0);
      float amount = 400.0;
      
      float d = length(coord - center) - (radius - amount);
      float3 pct = float3(smoothstep(0.0, amount, d));
      float3 color = mix(color1.rgb, color2.rgb, pct);
      return half4(color, 0.2);
  }
""".trimIndent()
