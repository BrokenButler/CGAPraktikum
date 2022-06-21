package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(var lightPos: Vector3f = Vector3f(), var lightCol: Vector3f = Vector3f()) : IPointLight,
    Transformable() {

    init {
        preTranslate(lightPos)
    }

    override fun bind(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("LightPos", getWorldPosition())
        shaderProgram.setUniform("LightCol", lightCol)
    }
}