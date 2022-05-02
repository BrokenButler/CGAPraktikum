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

        val vertices = floatArrayOf(
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f
        )

        val indices = intArrayOf(
            0, 1, 2,
            0, 2, 4,
            4, 2, 3
        )

        val mesh = Mesh(vertices, indices, attrib)

        meshList.add(mesh)
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
