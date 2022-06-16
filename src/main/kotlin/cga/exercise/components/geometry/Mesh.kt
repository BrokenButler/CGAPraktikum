package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL30C

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(
    vertexdata: FloatArray,
    indexdata: IntArray,
    attributes: Array<VertexAttribute>,
    var material: Material? = null
) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = indexdata.size

    init {
        // Create and Bind VAO
        vao = GL30C.glGenVertexArrays()
        GL30C.glBindVertexArray(vao)

        // Create and Bind VBO
        vbo = GL30C.glGenBuffers()
        GL30C.glBindBuffer(GL30C.GL_ARRAY_BUFFER, vbo)
        GL30C.glBufferData(GL30C.GL_ARRAY_BUFFER, vertexdata, GL30C.GL_STATIC_DRAW)

        // Create and Bind IBO
        ibo = GL30C.glGenBuffers()
        GL30C.glBindBuffer(GL30C.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL30C.glBufferData(GL30C.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL30C.GL_STATIC_DRAW)

        for (va in attributes) {
            GL30C.glEnableVertexAttribArray(va.index)
            GL30C.glVertexAttribPointer(va.index, va.n, va.type, va.normalized, va.stride, va.offset)
        }

        GL30C.glBindVertexArray(0)
    }

    /**
     * renders the mesh
     */
    fun render() {
        GL30C.glBindVertexArray(vao)
        GL30C.glDrawElements(GL30C.GL_TRIANGLES, indexcount, GL30C.GL_UNSIGNED_INT, 0)
        GL30C.glBindVertexArray(0) //bind(0) = unbind
    }

    fun render(shaderProgram: ShaderProgram) {
        material?.bind(shaderProgram)
        render()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) GL30C.glDeleteBuffers(ibo) //Why was GL15 used here instead of GL30?
        if (vbo != 0) GL30C.glDeleteBuffers(vbo) //Why was GL15 used here instead of GL30?
        if (vao != 0) GL30C.glDeleteVertexArrays(vao)
    }
}