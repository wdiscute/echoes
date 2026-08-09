#version 150

uniform sampler2D InSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

in vec2 texCoord;
out vec4 fragColor;

const float BLUR_STRENGTH = 1.0;
const float BLUR_START = 0.7;
const float BLUR_CURVE = 0.5;

// Number of samples in each direction.
const int SAMPLES = 8;


// ========================================
// MAIN
// ========================================

void main() {

    vec2 uv = texCoord;

    // ------------------------------------------------
    // Find distance to the nearest screen border.
    //
    // 0.0 = exactly on a border
    // 0.5 = center of the screen
    // ------------------------------------------------

    float distanceToBorder = min(
            min(uv.x, 1.0 - uv.x),
            min(uv.y, 1.0 - uv.y)
    );

    // Convert it so:
    //
    // 0.0 = center
    // 1.0 = border
    //
    float borderDistance =
    1.0 - (distanceToBorder * 2.0);

    // ------------------------------------------------
    // Create the blur ramp.
    // ------------------------------------------------

    float blurAmount = smoothstep(
            BLUR_START,
            1.0,
            borderDistance
    );

    // Make the ramp customizable.
    blurAmount = pow(
            blurAmount,
            BLUR_CURVE
    );

    // Completely sharp away from the border.
    if (blurAmount <= 0.001) {
        fragColor = texture(InSampler, uv);
        return;
    }

    // Size of one pixel in UV coordinates.
    vec2 pixelSize = 1.0 / InSize;

    vec4 blurred = vec4(0.0);
    float totalWeight = 0.0;

    // ------------------------------------------------
    // Gaussian blur
    // ------------------------------------------------

    for (int i = -SAMPLES; i <= SAMPLES; i++) {

        float x = float(i);

        float sigma = BLUR_STRENGTH * 0.5;

        float weight = exp(
                -(x * x) /
                (2.0 * sigma * sigma)
        );

        // Blur radius increases toward the border.
        float radius =
        BLUR_STRENGTH * blurAmount;

        vec2 offset =
        pixelSize * x * radius;

        // Horizontal
        blurred += texture(
                InSampler,
                uv + vec2(offset.x, 0.0)
        ) * weight;

        // Vertical
        blurred += texture(
                InSampler,
                uv + vec2(0.0, offset.y)
        ) * weight;

        totalWeight += weight * 2.0;
    }

    blurred /= totalWeight;

    vec4 original = texture(
            InSampler,
            uv
    );

    // ------------------------------------------------
    // Final blend.
    //
    // The closer to the border, the more the blurred
    // version replaces the original.
    // ------------------------------------------------

    fragColor = mix(
            original,
            blurred,
            blurAmount
    );
}