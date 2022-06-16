#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 tx;
layout(location = 2) in vec3 normal;

//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec2 tcMultiplier;

out struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normal;

} vertexData;


void main(){
    mat4 modelView = view_matrix * model_matrix;
    vec4 pos = modelView * vec4(position, 1.0f);
    vec4 norm = inverse(transpose(modelView)) * vec4(normal, 1.0f);

    gl_Position = projection_matrix * pos;

    vertexData.position = pos.xyz;
    vertexData.texture = tx * tcMultiplier;
    vertexData.normal = norm.xyz;
}
