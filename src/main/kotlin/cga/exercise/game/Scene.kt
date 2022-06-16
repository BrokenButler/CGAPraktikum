package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30C


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram =
        ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

    private var ground = Renderable()
    private var tronBike = ModelLoader.loadModel(
        "assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", Math.toRadians(-90f),
        Math.toRadians(90f), 0f
    ) ?: throw IllegalArgumentException("Could not load tronBike :/")

    private var camera = TronCamera()

    //scene setup
    init {
        //initial opengl state
        GL30C.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        GL30C.glDisable(GL30C.GL_CULL_FACE); GLError.checkThrow()
        GL30C.glFrontFace(GL30C.GL_CCW); GLError.checkThrow()
        GL30C.glCullFace(GL30C.GL_BACK); GLError.checkThrow()
        GL30C.glEnable(GL30C.GL_DEPTH_TEST); GLError.checkThrow()
        GL30C.glDepthFunc(GL30C.GL_LESS); GLError.checkThrow()

        val stride = 8 * 4
        val attrPos = VertexAttribute(0, 3, GL30C.GL_FLOAT, false, stride, 0)
        val attrTC = VertexAttribute(1, 2, GL30C.GL_FLOAT, false, stride, 3 * 4)
        val attrNorm = VertexAttribute(2, 3, GL30C.GL_FLOAT, false, stride, 5 * 4)
        val vertexAttributes = arrayOf<VertexAttribute>(attrPos, attrTC, attrNorm)

        //Material
        val groundEmit = Texture2D("assets/textures/ground_emit.png", true)
        val groundDiff = Texture2D("assets/textures/ground_diff.png", true)
        val groundSpec = Texture2D("assets/textures/ground_spec.png", true)

        groundEmit.setTexParams(GL30C.GL_REPEAT, GL30C.GL_REPEAT, GL30C.GL_LINEAR_MIPMAP_LINEAR, GL30C.GL_LINEAR)
        groundDiff.setTexParams(GL30C.GL_REPEAT, GL30C.GL_REPEAT, GL30C.GL_LINEAR_MIPMAP_LINEAR, GL30C.GL_LINEAR)
        groundSpec.setTexParams(GL30C.GL_REPEAT, GL30C.GL_REPEAT, GL30C.GL_LINEAR_MIPMAP_LINEAR, GL30C.GL_LINEAR)

        val groundMaterial = Material(groundDiff, groundEmit, groundSpec, 60f, Vector2f(64f, 64f))

        //Loading the Ground
        val resGround = OBJLoader.loadOBJ("assets/models/ground.obj")
        val objMeshGround = resGround.objects[0].meshes[0]
        val groundMesh = Mesh(objMeshGround.vertexData, objMeshGround.indexData, vertexAttributes, groundMaterial)
        ground.addMesh(groundMesh)

        tronBike.scale(Vector3f(0.8f))

        //Camera setup
        camera.rotate(Math.toRadians(-35.0).toFloat(), 0.0f, 0.0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))
        camera.parent = tronBike
    }

    fun render(dt: Float, t: Float) {
        GL30C.glClear(GL30C.GL_COLOR_BUFFER_BIT or GL30C.GL_DEPTH_BUFFER_BIT)
        camera.bind(staticShader)
        ground.render(staticShader)
        tronBike.render(staticShader)
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW.GLFW_KEY_W)) {
            tronBike.translate(Vector3f(0.0f, 0.0f, -10 * dt))

            if (window.getKeyState(GLFW.GLFW_KEY_A))
                tronBike.rotate(0.0f, 2 * dt, 0.0f)

            if (window.getKeyState(GLFW.GLFW_KEY_D))
                tronBike.rotate(0.0f, -2 * dt, 0.0f)
        }

        if (window.getKeyState(GLFW.GLFW_KEY_S)) {
            tronBike.translate(Vector3f(0.0f, 0.0f, 10 * dt))

            if (window.getKeyState(GLFW.GLFW_KEY_A))
                tronBike.rotate(0.0f, 2 * dt, 0.0f)

            if (window.getKeyState(GLFW.GLFW_KEY_D))
                tronBike.rotate(0.0f, -2 * dt, 0.0f)
        }

        if (window.getKeyState(GLFW.GLFW_KEY_Q))
            tronBike.rotate(0.0f, 0.0f, 2 * dt)

        if (window.getKeyState(GLFW.GLFW_KEY_E))
            tronBike.rotate(0.0f, 0.0f, -2 * dt)
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}


    fun cleanup() {}
}
