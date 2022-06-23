#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoor;
layout(location = 2) in vec3 normal;

//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec2 tcMultiplier;

uniform vec3 pointLightPos;

uniform vec3 spotLightPos;

uniform vec3 corner1LightPos;
uniform vec3 corner2LightPos;
uniform vec3 corner3LightPos;
uniform vec3 corner4LightPos;

out struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normal;
    vec3 toPointLight;
    vec3 toSpotLight;
    vec3 toCornerLight1;
    vec3 toCornerLight2;
    vec3 toCornerLight3;
    vec3 toCornerLight4;

} vertexData;



void main(){
    mat4 modelView = view_matrix * model_matrix;
    vec4 pos = modelView * vec4(position, 1.0f);
    vec4 norm = inverse(transpose(modelView)) * vec4(normal, 1.0f);

    //PointLight
    vec4 lightPos = view_matrix * vec4(pointLightPos, 1.0f);
    vertexData.toPointLight = (lightPos - pos).xyz;

    //SpotLight
    vec4 sLightPos = view_matrix * vec4(spotLightPos, 1.0f);
    vertexData.toSpotLight = (sLightPos - pos).xyz;

    //Corner1
    vec4 cLightPos1 = view_matrix * vec4(corner1LightPos, 1.0f);
    vertexData.toCornerLight1 = (cLightPos1 - pos).xyz;

    //Corner2
    vec4 cLightPos2 = view_matrix * vec4(corner2LightPos, 1.0f);
    vertexData.toCornerLight2 = (cLightPos2 - pos).xyz;

    //Corner3
    vec4 cLightPos3 = view_matrix * vec4(corner3LightPos, 1.0f);
    vertexData.toCornerLight3 = (cLightPos3 - pos).xyz;

    //Corner4
    vec4 cLightPos4 = view_matrix * vec4(corner4LightPos, 1.0f);
    vertexData.toCornerLight4 = (cLightPos4 - pos).xyz;

    gl_Position = projection_matrix * pos;

    vertexData.position = pos.xyz;
    vertexData.texture = texCoor * tcMultiplier;
    vertexData.normal = norm.xyz;
}
