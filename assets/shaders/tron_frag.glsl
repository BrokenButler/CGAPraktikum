#version 330 core

//input from vertex shader
in struct VertexData
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

uniform sampler2D diff;
uniform sampler2D emit;
uniform sampler2D specular;
uniform float shininess;

//PointLight
uniform vec3 pointLightCol;
uniform vec3 pointLightAttParam;

//SpotLight
uniform vec3 spotLightCol;
uniform vec3 spotLightAttParam;
uniform vec2 spotLightAngle;
uniform vec3 spotLightDir;

//Corner1
uniform vec3 corner1LightCol;
uniform vec3 corner1LightAttParam;

//Corner2
uniform vec3 corner2LightCol;
uniform vec3 corner2LightAttParam;

//Corner3
uniform vec3 corner3LightCol;
uniform vec3 corner3LightAttParam;

//Corner4
uniform vec3 corner4LightCol;
uniform vec3 corner4LightAttParam;

uniform vec3 col;

//fragment shader output
out vec4 color;

vec3 diffSpec(vec3 normale, vec3 lightDir, vec3 viewDir, vec3 diff, vec3 spec, float shini){
    vec3 diffuse = diff * max(dot(normale, lightDir), 0.0f);//Diffuse Farbe * Skalarprodukt von Normale und LichtPos, oder 0.0

    //Phong
    //vec3 reflectDir = reflect(-lightDir,normale);             //reflection direction
    //float cosb = max(dot(viewDir, reflectDir), 0.0f);         //Skalarprodukt von Position und reflection direction
    //vec3 specular = spec * pow(cosb, shini);                  //Speculare Farbe * cosb^shini

    //Blinn-Phong
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float specular = pow(max(dot(normale, halfwayDir), 0.0f), 16.0f);

    return diffuse + specular * spec;
}

float attenuate(float length, vec3 attParam){ //Gamma Correction nachschauen
    return 1.0/(attParam.x + attParam.y * length + attParam.z * pow(length, 2));
}

vec3 pointLightIntensity(vec3 lightColor, float length, vec3 attP){
    return lightColor * attenuate(length, attP);
}

vec3 spotLightIntensity(vec3 spotLightColor, float length, vec3 sp, vec3 spDir){
    float cosTheta = dot(sp, normalize(spDir));
    float cosPhi = cos(spotLightAngle.x);
    float cosGamma = cos(spotLightAngle.y);

    float intensity = (cosTheta - cosGamma)/(cosPhi - cosGamma);
    float cintensity = clamp(intensity, 0.0f, 1.0f);

    return spotLightColor * cintensity * attenuate(length, spotLightAttParam);
}

vec3 gamma(vec3 C_linear){
    return pow(C_linear, vec3(2.2));
}

vec3 invgamma(vec3 C_gamma){
    return pow(C_gamma, vec3(1.0/2.2));
}

void main(){
    vec3 normals = normalize(vertexData.normal);
    vec3 positions = normalize(vertexData.position);

    //PointLight
    float lpLength = length(vertexData.toPointLight);
    vec3 lp = normalize(vertexData.toPointLight);
    //SpotLight
    float spLength = length(vertexData.toSpotLight);
    vec3 sp = normalize(vertexData.toSpotLight);
    //Corner1
    float cpLength = length(vertexData.toCornerLight1);
    vec3 cp1 = normalize(vertexData.toCornerLight1);
    //Corner2
    float cpLength2 = length(vertexData.toCornerLight2);
    vec3 cp2 = normalize(vertexData.toCornerLight2);
    //Corner3
    float cpLength3 = length(vertexData.toCornerLight3);
    vec3 cp3 = normalize(vertexData.toCornerLight3);
    //Corner4
    float cpLength4 = length(vertexData.toCornerLight4);
    vec3 cp4 = normalize(vertexData.toCornerLight4);

    vec3 diffCol = texture(diff, vertexData.texture).rgb;
    diffCol = invgamma(diffCol);
    vec3 emitCol = texture(emit, vertexData.texture).rgb;
    emitCol = invgamma(emitCol);
    vec3 specCol = texture(specular, vertexData.texture).rgb;
    specCol = invgamma(specCol);

    vec3 emissive = emitCol * col;

    //Ambient Term
    //PointLight
    emissive += diffSpec(normals, lp, positions, diffCol, specCol, shininess) * pointLightIntensity(pointLightCol, lpLength, pointLightAttParam);
    //SpotLight
    emissive += diffSpec(normals, sp, positions, diffCol, specCol, shininess) * spotLightIntensity(spotLightCol, spLength, sp, spotLightDir);
    //Corner1
    emissive += diffSpec(normals, cp1, positions, diffCol, specCol, shininess) * pointLightIntensity(corner1LightCol, cpLength, corner1LightAttParam);
    //Corner2
    emissive += diffSpec(normals, cp2, positions, diffCol, specCol, shininess) * pointLightIntensity(corner2LightCol, cpLength2, corner2LightAttParam);
    //Corner3
    emissive += diffSpec(normals, cp3, positions, diffCol, specCol, shininess) * pointLightIntensity(corner3LightCol, cpLength3, corner3LightAttParam);
    //Corner4
    emissive += diffSpec(normals, cp4, positions, diffCol, specCol, shininess) * pointLightIntensity(corner4LightCol, cpLength4, corner4LightAttParam);

    emissive = gamma(emissive);
    color = vec4(emissive, 1.0);
}