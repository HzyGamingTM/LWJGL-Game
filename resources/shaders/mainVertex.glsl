#version 330 core

in vec3 position;
in vec3 color;
in vec2 textureCoord;

out vec3 passColor;
out vec2 passTextureCoord;

uniform mat4 model;
uniform mat4 view;
uniform float fovMul;
uniform float aspect;

void main() {
    gl_Position = view * model * vec4(position, 1.0);
    gl_Position.x *= aspect;
    gl_Position.w *= gl_Position.z + 1;

    passColor = color;
    passTextureCoord = textureCoord;
}