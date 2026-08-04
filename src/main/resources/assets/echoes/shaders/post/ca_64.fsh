#version 330

#moj_import <minecraft:globals.glsl>

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

in vec2 texCoord;

out vec4 fragColor;

const vec2 CA_CENTER = vec2(0.5);

const float CA_STRENGTH = 0.512;

const float CA_RADIUS = 1.0;

const float CA_FALLOFF = 2.0;

void main()
{
    vec2 uv = texCoord;

    vec2 dir = uv - CA_CENTER;

    float dist = length(dir);

    if (dist > 0.0001)
    dir /= dist;

    float influence = clamp(dist / CA_RADIUS, 0.0, 1.0);
    influence = pow(influence, CA_FALLOFF);

    vec2 offset = dir * CA_STRENGTH * influence;

    float r = texture(InSampler, uv + offset).r;
    float g = texture(InSampler, uv).g;
    float b = texture(InSampler, uv - offset).b;

    float a = texture(InSampler, uv).a;

    fragColor = vec4(r, g, b, a);
}