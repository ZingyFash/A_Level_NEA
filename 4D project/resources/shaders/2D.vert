#version 330

layout (location=0) in vec3 inPosition;
layout (location=1) in vec3 inColour;

out vec3 outColour;

uniform float aspectRatio;

void main()
{
    gl_Position = vec4(inPosition.x / aspectRatio - 0.5, inPosition.yz, 1.0);
    outColour = inColour;
}