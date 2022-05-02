package cga.exercise.game

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import org.lwjgl.opengl.GL30


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    private val meshList = mutableListOf<Mesh>()


    //scene setup
    init {
        staticShader = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")

        //initial opengl state
        GL30.glClearColor(0.6f, 1.0f, 1.0f, 1.0f); GLError.checkThrow()
        GL30.glDisable(GL30.GL_CULL_FACE); GLError.checkThrow()
        GL30.glFrontFace(GL30.GL_CCW); GLError.checkThrow()
        GL30.glCullFace(GL30.GL_BACK); GLError.checkThrow()
        GL30.glEnable(GL30.GL_DEPTH_TEST); GLError.checkThrow()
        GL30.glDepthFunc(GL30.GL_LESS); GLError.checkThrow()

        val pos = VertexAttribute(0, 3, GL30.GL_FLOAT, false, 24, 0)
        val col = VertexAttribute(1, 3, GL30.GL_FLOAT, false, 24, 12)

        val attrib = arrayOf<VertexAttribute>(pos, col)

        /**
        //House
        val houseVertices = floatArrayOf(
        -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
        -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f
        )

        val houseIndices = intArrayOf(
        0, 1, 2,
        0, 2, 4,
        4, 2, 3
        )
        val house = Mesh(houseVertices, houseIndices, attrib)
        meshList.add(house)
         */

        //DBP
        //D
        val dVertices = floatArrayOf(
            -1.0f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.625f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.5f, -0.25f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.5f, 0.25f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.625f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
            -1.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.875f, -0.375f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.75f, -0.375f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.625f, -0.25f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.625f, 0.25f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.75f, 0.375f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.875f, 0.375f, 0.0f, 1.0f, 0.0f, 0.0f
        )
        val dIndices = intArrayOf(
            0, 1, 6,
            1, 7, 6,
            1, 8, 7,
            1, 2, 8,
            2, 9, 8,
            2, 3, 9,
            3, 10, 9,
            3, 4, 10,
            4, 11, 10,
            4, 5, 11,
            5, 6, 11,
            5, 0, 6
        )
        val dMesh = Mesh(dVertices, dIndices, attrib)
        meshList.add(dMesh)

        //B
        val bVertices = floatArrayOf(
            -0.25f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.125f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.25f, -0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.125f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.25f, 0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.125f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.25f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.125f, -0.375f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, -0.375f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.125f, -0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, -0.125f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.125f, -0.125f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.125f, 0.125f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.125f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.125f, 0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.375f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.125f, 0.375f, 0.0f, 0.0f, 1.0f, 0.0f
        )
        val bIndices = intArrayOf(
            0, 1, 7,
            1, 8, 7,
            1, 2, 8,
            2, 9, 8,
            2, 3, 9,
            3, 10, 9,
            3, 11, 10,
            3, 12, 11,
            3, 13, 12,
            3, 4, 13,
            4, 14, 13,
            4, 5, 14,
            5, 15, 14,
            5, 6, 15,
            6, 16, 15,
            6, 0, 7,
            6, 7, 11,
            6, 11, 12,
            6, 12, 16
        )
        val bMesh = Mesh(bVertices, bIndices, attrib)
        meshList.add(bMesh)

        //P
        val pVertices = floatArrayOf(
            0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.675f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.675f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.875f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.25f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.875f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.675f, 0.125f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.75f, 0.125f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.875f, 0.25f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.75f, 0.375f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.675f, 0.375f, 0.0f, 0.0f, 0.0f, 1.0f
        )
        val pIndices = intArrayOf(
            0, 1, 2,
            2, 3, 7,
            3, 8, 7,
            3, 4, 8,
            4, 9, 8,
            4, 5, 9,
            5, 10, 9,
            5, 6, 10,
            6, 11, 10,
            6, 0, 2,
            6, 2, 7,
            6, 7, 11
        )
        val pMesh = Mesh(pVertices, pIndices, attrib)
        meshList.add(pMesh)
    }

    fun render(dt: Float, t: Float) {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
        for (m in meshList) {
            staticShader.use()
            m.render()
        }
    }

    fun update(dt: Float, t: Float) {}

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}


    fun cleanup() {}
}
