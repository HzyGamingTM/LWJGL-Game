#version 330 core

in vec2 position;
out vec2 textureCoord;
uniform mat4 transformationMatrix;

void main(void) {
    gl_Position = vec4(position, 0.0, 1.0);
    textureCoord = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
}