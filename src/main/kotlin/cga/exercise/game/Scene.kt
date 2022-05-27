package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.OBJLoader
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30C


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram

    private var ground = Renderable()
    private var sphere = Renderable()

    private var camera = TronCamera()

    //scene setup
    init {
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

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

        //Loading the Ground
        val resGround = OBJLoader.loadOBJ("assets/models/ground.obj")
        val objMeshGround = resGround.objects[0].meshes[0]
        val groundMesh = Mesh(objMeshGround.vertexData, objMeshGround.indexData, vertexAttributes)
        ground.addMesh(groundMesh)

        //Loading the Sphere
        val resSphere = OBJLoader.loadOBJ("assets/models/sphere.obj")
        val objMeshSphere = resSphere.objects[0].meshes[0]
        val sphereMesh = Mesh(objMeshSphere.vertexData, objMeshSphere.indexData, vertexAttributes)
        sphere.addMesh(sphereMesh)

        //Camera setup
        camera.rotate(Math.toRadians(-20.0).toFloat(), 0.0f, 0.0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))
        camera.parent = sphere
    }

    fun render(dt: Float, t: Float) {
        GL30C.glClear(GL30C.GL_COLOR_BUFFER_BIT or GL30C.GL_DEPTH_BUFFER_BIT)
        camera.bind(staticShader)
        ground.render(staticShader)
        sphere.render(staticShader)
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW.GLFW_KEY_W))
            sphere.translate(Vector3f(0.0f, 0.0f, -20 * dt))

        if (window.getKeyState(GLFW.GLFW_KEY_S))
            sphere.translate(Vector3f(0.0f, 0.0f, 20 * dt))

        if (window.getKeyState(GLFW.GLFW_KEY_A))
            sphere.rotate(0.0f, 2 * dt, 0.0f)

        if (window.getKeyState(GLFW.GLFW_KEY_D))
            sphere.rotate(0.0f, -2 * dt, 0.0f)

        if (window.getKeyState(GLFW.GLFW_KEY_Q))
            sphere.rotate(0.0f, 0.0f, 2 * dt)

        if (window.getKeyState(GLFW.GLFW_KEY_E))
            sphere.rotate(0.0f, 0.0f, -2 * dt)

    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}


    fun cleanup() {}
}
