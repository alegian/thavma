#version 150

in vec4 vertexColor;
in vec3 fragPosition;
flat in vec3 cornerPosition;
flat in vec3 fragCenter;
flat in float scale;

uniform mat4 ModelViewMat;
uniform float GameTime;

out vec4 fragColor;

float random(float seed) {
    return fract(sin(seed) * 43758.5453123);
}

vec2 random2(vec2 st) {
    st = vec2(dot(st, vec2(127.1, 311.7)),
    dot(st, vec2(269.5, 183.3)));
    return -1.0 + 2.0 * fract(sin(st) * 43758.5453123);
}

// Gradient Noise by Inigo Quilez - iq/2013
// https://www.shadertoy.com/view/XdXGW8
float noise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(mix(dot(random2(i + vec2(0.0, 0.0)), f - vec2(0.0, 0.0)),
                   dot(random2(i + vec2(1.0, 0.0)), f - vec2(1.0, 0.0)), u.x),
               mix(dot(random2(i + vec2(0.0, 1.0)), f - vec2(0.0, 1.0)),
                   dot(random2(i + vec2(1.0, 1.0)), f - vec2(1.0, 1.0)), u.x), u.y);
}

float fbm(vec2 st) {
    float value = 0.0;
    float amplitude = 0.5;
    for (int i = 0; i < 4; i++) {
        value += amplitude * noise(st);
        st *= 2.1;
        amplitude *= 0.5;
    }
    return value;
}

void main() {
    vec3 diff = fragCenter - fragPosition;
    vec3 maxDiff = fragCenter - cornerPosition;
    float maxLen = length(maxDiff);
    float dist = length(diff);
    float safeScale = max(scale, 0.001);
    float scaledDist = dist / (maxLen * safeScale);

    if (scaledDist > 1.15) discard;

    float time = GameTime * 1200.0;
    float phase = random(scale + 0.1) * 6.28318;

    vec4 cameraDiff = ModelViewMat * vec4(diff, 1.0);
    float angle = atan(cameraDiff.y, cameraDiff.x);

    // --- organic edge distortion ---
    float n1 = noise(vec2(angle * 1.5 + time * 0.25, phase + time * 0.1)) * 0.15;
    float n2 = noise(vec2(angle * 3.0 - time * 0.4, phase + 7.0)) * 0.08;
    float n3 = noise(vec2(angle * 6.0 + time * 0.6, phase + 15.0)) * 0.04;
    float edgeDistortion = n1 + n2 + n3;

    float pulse = sin(time * 0.7 + phase) * 0.04 + sin(time * 1.3 + phase * 2.0) * 0.02;

    float edgeThreshold = 1.0 - edgeDistortion - pulse;

    if (scaledDist > edgeThreshold && safeScale > 0.12) discard;
    if (scaledDist > 1.0) discard;

    // --- swirling internal texture ---
    float swirlAngle = angle + scaledDist * 2.0 - time * 0.15;
    vec2 swirlCoord = vec2(swirlAngle * 2.0 / 6.28318, scaledDist * 3.0 + time * 0.08);
    float swirl = fbm(swirlCoord + vec2(phase, phase * 0.7)) * 0.35 + 0.65;

    // --- radial gradient ---
    float gradient = 1.0 - pow(scaledDist, 1.2);

    // --- inner glow ---
    float innerGlow = exp(-scaledDist * 5.0);

    // --- soft edge fadeout ---
    float edgeFade = 1.0 - smoothstep(0.55, min(edgeThreshold, 1.0), scaledDist);

    // --- color composition ---
    vec3 baseColor = vertexColor.rgb;
    vec3 brightened = mix(baseColor, vec3(1.0), innerGlow * 0.65);
    brightened *= swirl;
    brightened += vec3(innerGlow * 0.25);

    float alpha = vertexColor.a * gradient * edgeFade;
    alpha = min(alpha + innerGlow * vertexColor.a * 0.4, vertexColor.a * 1.2);

    fragColor = vec4(brightened, alpha);
}
