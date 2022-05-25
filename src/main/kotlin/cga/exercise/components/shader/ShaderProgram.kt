package cga.exercise.components.shader

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30C
import java.nio.FloatBuffer
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Fabian on 16.09.2017.
 */
class ShaderProgram(vertexShaderPath: String, fragmentShaderPath: String) {
    private var programID: Int = 0
    //Matrix buffers for setting matrix uniforms. Prevents allocation for each uniform
    private val m4x4buf: FloatBuffer = BufferUtils.createFloatBuffer(16)
    /**
     * Sets the active shader program of the OpenGL render pipeline to this shader
     * if this isn't already the currently active shader
     */
    fun use() {
        val curprog = GL30C.glGetInteger(GL30C.GL_CURRENT_PROGRAM)
        if (curprog != programID) GL30C.glUseProgram(programID)

    }

    /**
     * Frees the allocated OpenGL objects
     */
    fun cleanup() {
        GL30C.glDeleteProgram(programID)
    }

    //setUniform() functions are added later during the course
    // float vector uniforms
    /**
     * Sets a single float uniform
     * @param name  Name of the uniform variable in the shader
     * @param value Value
     * @return returns false if the uniform was not found in the shader
     */
    fun setUniform(name: String, value: Float): Boolean {
        if (programID == 0) return false
        val loc = GL30C.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL30C.glUniform1f(loc, value)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Matrix4f, transpose: Boolean): Boolean {
        if (programID == 0) return false
        val loc = GL30C.glGetUniformLocation(programID, name)
        if (loc != -1) {
            GL30C.glUniformMatrix4fv(loc, transpose, value[m4x4buf])
            return true
        }
        return false
    }


    /**
     * Creates a shader object from vertex and fragment shader paths
     * @param vertexShaderPath      vertex shader path
     * @param fragmentShaderPath    fragment shader path
     * @throws Exception if shader compilation failed, an exception is thrown
     */
    init {
        val vPath = Paths.get(vertexShaderPath)
        val fPath = Paths.get(fragmentShaderPath)
        val vSource = String(Files.readAllBytes(vPath))
        val fSource = String(Files.readAllBytes(fPath))
        val vShader = GL30C.glCreateShader(GL30C.GL_VERTEX_SHADER)
        if (vShader == 0) throw Exception("Vertex shader object couldn't be created.")
        val fShader = GL30C.glCreateShader(GL30C.GL_FRAGMENT_SHADER)
        if (fShader == 0) {
            GL30C.glDeleteShader(vShader)
            throw Exception("Fragment shader object couldn't be created.")
        }
        GL30C.glShaderSource(vShader, vSource)
        GL30C.glShaderSource(fShader, fSource)
        GL30C.glCompileShader(vShader)
        if (GL30C.glGetShaderi(vShader, GL30C.GL_COMPILE_STATUS) == GL30C.GL_FALSE) {
            val log = GL30C.glGetShaderInfoLog(vShader)
            GL30C.glDeleteShader(fShader)
            GL30C.glDeleteShader(vShader)
            throw Exception("Vertex shader compilation failed:\n$log")
        }
        GL30C.glCompileShader(fShader)
        if (GL30C.glGetShaderi(fShader, GL30C.GL_COMPILE_STATUS) == GL30C.GL_FALSE) {
            val log = GL30C.glGetShaderInfoLog(fShader)
            GL30C.glDeleteShader(fShader)
            GL30C.glDeleteShader(vShader)
            throw Exception("Fragment shader compilation failed:\n$log")
        }
        programID = GL30C.glCreateProgram()
        if (programID == 0) {
            GL30C.glDeleteShader(vShader)
            GL30C.glDeleteShader(fShader)
            throw Exception("Program object creation failed.")
        }
        GL30C.glAttachShader(programID, vShader)
        GL30C.glAttachShader(programID, fShader)
        GL30C.glLinkProgram(programID)
        if (GL30C.glGetProgrami(programID, GL30C.GL_LINK_STATUS) == GL30C.GL_FALSE) {
            val log = GL30C.glGetProgramInfoLog(programID)
            GL30C.glDetachShader(programID, vShader)
            GL30C.glDetachShader(programID, fShader)
            GL30C.glDeleteShader(vShader)
            GL30C.glDeleteShader(fShader)
            throw Exception("Program linking failed:\n$log")
        }
        GL30C.glDetachShader(programID, vShader)
        GL30C.glDetachShader(programID, fShader)
        GL30C.glDeleteShader(vShader)
        GL30C.glDeleteShader(fShader)
    }
}