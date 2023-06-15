#version 330 core

in vec2 v_tex;
uniform sampler2D texSampler;
out vec4 outColor;

void main() {
    outColor = texture(texSampler, v_tex);
}