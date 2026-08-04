#version 330

#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

in vec2 texCoord;

out vec4 fragColor;

// 0.0 = Off
// 1.0 = Full
const float STRENGTH = 0.01;

// RGB multipliers
const vec3 CHANNEL_GAIN = vec3(
        0.95,
        1.02,
        1.10
);

// Deep blue with a slight green tint
const vec3 SHADOW_TINT = vec3(
        0.00,
        0.01,
        0.32
);

// Shadow strength
const float SHADOW_STRENGTH = 0.60;

// Contrast
const float CONTRAST = 1.1;

// Saturation
const float SATURATION = 1.2;

//==================================================

vec3 ApplyColorGrade(vec3 color)
{
    vec3 original = color;

    // Slight RGB shift
    color *= CHANNEL_GAIN;

    // Brightness estimate
    float luma = dot(color, vec3(0.299, 0.587, 0.114));

    // Tint shadows more than highlights
    float shadow = 1.0 - smoothstep(0.15, 0.75, luma);

    color += SHADOW_TINT * shadow * SHADOW_STRENGTH;

    // Contrast
    color = (color - 0.5) * CONTRAST + 0.5;

    // Saturation
    float grey = dot(color, vec3(0.299, 0.587, 0.114));
    color = mix(vec3(grey), color, SATURATION);

    color = clamp(color, 0.0, 1.0);

    return mix(original, color, STRENGTH);
}

void main()
{
    fragColor = texture(InSampler, texCoord);

    fragColor.rgb = ApplyColorGrade(fragColor.rgb);
}