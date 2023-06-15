#version 330 core

in vec3 passColor;
in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D tex;

void main() {
    vec4 texture = texture(tex, passTextureCoord);

    float brightness = (texture.r * 0.2126) + (texture.g * 0.7152) + (texture.b * 0.0722);
    outColor = brightness * texture;
}