#version 330

layout (location=0) in vec3 inPosition;
layout (location=1) in vec3 inColour;

out vec3 outColour;

uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;

void main()
{
    gl_Position = projectionMatrix * (modelMatrix * vec4(inPosition, 1.0)+vec4(2,0,-5,0));
    outColour = inColour;
}