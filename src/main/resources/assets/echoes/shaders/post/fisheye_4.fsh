#version 330

#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

in vec2 texCoord;

out vec4 fragColor;

const float BH_PULL = 0.02;

void main()
{
    vec2 uv = texCoord;

    vec2 p = uv - vec2(0.5, 0.5);

    float r = length(p);
    float theta = atan(p.y, p.x);

    float influence = clamp(1.0 - r, 0.0, 1.0);

    // Sharpen the effect
    influence = pow(influence, 3);

    // Pull inward
    r *= (1.0 - BH_PULL * influence);

    // Convert back
    uv = vec2(0.5, 0.5) + vec2(cos(theta), sin(theta)) * r;

    fragColor = texture(InSampler, uv);
}