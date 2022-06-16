package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram

class Renderable(private val meshes: MutableList<Mesh> = mutableListOf()) : IRenderable, Transformable() {

    fun addMesh(mesh: Mesh) {
        meshes.add(mesh)
    }

    fun removeMesh(mesh: Mesh) {
        meshes.remove(mesh)
    }

    override fun render(shaderProgram: ShaderProgram) {
        shaderProgram.use()
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)
        for (m in meshes) {
            m.render(shaderProgram)
        }
    }
}