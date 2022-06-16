#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normal;
} vertexData;

uniform sampler2D emit;

//fragment shader output
out vec4 color;

void main(){
    vec3 normals = normalize(vertexData.normal);

    vec3 emitCol = texture(emit, vertexData.texture).rgb;

    //color = vec4(abs(normals.x), abs(normals.y), abs(normals.z), 1.0f);
    color = vec4(emitCol, 1.0f);
}