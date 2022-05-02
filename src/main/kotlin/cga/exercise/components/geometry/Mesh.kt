package cga.exercise.components.geometry

import org.lwjgl.opengl.GL30

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
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = indexdata.size

    init {
        // Create and Bind VAO
        vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)

        // Create and Bind VBO
        vbo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexdata, GL30.GL_STATIC_DRAW)

        // Create and Bind IBO
        ibo = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, ibo)
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL30.GL_STATIC_DRAW)

        for (va in attributes) {
            GL30.glEnableVertexAttribArray(va.index)
            GL30.glVertexAttribPointer(va.index, va.n, va.type, va.normalized, va.stride, va.offset)
        }

        GL30.glBindVertexArray(0)
    }

    /**
     * renders the mesh
     */
    fun render() {
        GL30.glBindVertexArray(vao)
        GL30.glDrawElements(GL30.GL_TRIANGLES, indexcount, GL30.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0) //bind(0) = unbind
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) GL30.glDeleteBuffers(ibo) //Why was GL15 used here instead of GL30?
        if (vbo != 0) GL30.glDeleteBuffers(vbo) //Why was GL15 used here instead of GL30?
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }
}