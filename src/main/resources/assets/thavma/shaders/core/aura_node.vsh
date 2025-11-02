#version 150

in vec3 Position;
in vec4 Color;
in vec3 Center;
in float Scale;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 vertexColor;
out vec3 fragPosition;
flat out vec3 cornerPosition;
flat out vec3 fragCenter;
flat out float scale;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    fragPosition = Position;
    cornerPosition = Position;
    fragCenter = Center;
    scale = Scale;
}
