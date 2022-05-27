#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec2 tx;
    vec3 normal;
} vertexData;

//fragment shader output
out vec4 color;

void main(){
    vec3 normals = normalize(vertexData.normal);
    color = vec4(abs(normals.x), abs(normals.y), abs(normals.z), 1.0f);
}