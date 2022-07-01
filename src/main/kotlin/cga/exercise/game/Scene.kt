package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import org.joml.Math
import org.joml.Math.abs
import org.joml.Math.sin
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

    private var pointLight: PointLight = PointLight(
        camera.getWorldPosition(),
        Vector3f(1f, 0f, 0f),
        Vector3f(0.7f, 0.3f, 0f)
    )
    private var spotLight: SpotLight = SpotLight(Vector3f(0.0f, 1.0f, -2.0f), Vector3f(1.0f))
    private var corner1 =
        PointLight(Vector3f(20.0f, 4f, 20.0f), Vector3f(1.0f, 0.0f, 1.0f), Vector3f(0.7f, 0.3f, 0.0f))
    private var corner2 =
        PointLight(Vector3f(-20.0f, 4f, 20.0f), Vector3f(1.0f, 1.0f, 0.0f), Vector3f(0.7f, 0.3f, 0.0f))
    private var corner3 =
        PointLight(Vector3f(20.0f, 4f, -20.0f), Vector3f(0.0f, 1.0f, 1.0f), Vector3f(0.7f, 0.3f, 0.0f))
    private var corner4 =
        PointLight(Vector3f(-20.0f, 4f, -20.0f), Vector3f(1.0f, 0.5f, 0.5f), Vector3f(0.7f, 0.3f, 0.0f))

    private var oldMousePosX = -1.0
    private var oldMousePosY = -1.0
    private var bool = false

    //scene setup
    init {
        //initial opengl state
        GL30C.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        GL30C.glEnable(GL30C.GL_CULL_FACE); GLError.checkThrow()
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

        val groundMaterial = Material(groundDiff, groundEmit, groundSpec, 60f, Vector2f(64f))

        //Loading the Ground
        val resGround = OBJLoader.loadOBJ("assets/models/ground.obj")
        val objMeshGround = resGround.objects[0].meshes[0]
        val groundMesh = Mesh(objMeshGround.vertexData, objMeshGround.indexData, vertexAttributes, groundMaterial)
        ground.addMesh(groundMesh)

        tronBike.scale(Vector3f(0.8f))

        //Light setup
        pointLight.translate(Vector3f(0f, 3f, 0f))
        pointLight.parent = tronBike

        spotLight.rotate(Math.toRadians(-10f), Math.PI.toFloat(), 0f)
        spotLight.parent = tronBike

        //Camera setup
        camera.rotate(Math.toRadians(-35.0).toFloat(), 0.0f, 0.0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))
        camera.parent = tronBike
    }

    fun render(dt: Float, t: Float) {
        GL30C.glClear(GL30C.GL_COLOR_BUFFER_BIT or GL30C.GL_DEPTH_BUFFER_BIT)

        staticShader.setUniform("col", Vector3f(0.4235f, 0.4745f, 0.5804f)) //Paynes Grey
        //staticShader.setUniform("col", Vector3f(0f, 1f, 0f)) //Green

        camera.bind(staticShader)
        ground.render(staticShader)

        staticShader.setUniform("col", Vector3f(abs(sin(t / 1)), abs(sin(t / 3)), abs(sin(t / 2))))
        tronBike.render(staticShader)

        pointLight.bind(staticShader, "point")
        pointLight.lightCol = Vector3f(abs(sin(t / 1)), abs(sin(t / 3)), abs(sin(t / 2)))

        spotLight.bind(staticShader, "spot", camera.getCalculateViewMatrix())
        //spotLight.lightCol = Vector3f(abs(sin(t / 14)), abs(sin(t / 8)), abs(sin(t / 6)))

        corner1.bind(staticShader, "corner1")
        corner2.bind(staticShader, "corner2")
        corner3.bind(staticShader, "corner3")
        corner4.bind(staticShader, "corner4")

        //PartyLights
        //corner4.lightCol = Vector3f(abs(sin(t / 20)), abs(sin(t / 2)), abs(sin(t / 3)))
        //corner3.lightCol = Vector3f(abs(sin(t / 21)), abs(sin(t / 4)), abs(sin(t / 3)))
        //corner2.lightCol = Vector3f(abs(sin(t /15)), abs(sin(t / 1)), abs(sin(t / 4)))
        //corner1.lightCol = Vector3f(abs(sin(t / 22)), abs(sin(t / 3)), abs(sin(t / 1)))

        //RaveLights
        //corner4.lightCol = Vector3f(abs(tan(t / 1)), abs(tan(t / 2)), abs(tan(t / 3)))
        //corner3.lightCol = Vector3f(abs(tan(t / 1)), abs(tan(t / 2)), abs(tan(t / 3)))
        //corner2.lightCol = Vector3f(abs(tan(t / 1)), abs(tan(t / 2)), abs(tan(t / 3)))
        //corner1.lightCol = Vector3f(abs(tan(t / 1)), abs(tan(t / 2)), abs(tan(t / 3)))

        //ChillLight
        //corner4.lightCol = Vector3f(abs(cos(t / 1)), abs(cos(t / 2)), abs(cos(t / 3)))
        //corner3.lightCol = Vector3f(abs(cos(t / 1)), abs(cos(t / 2)), abs(cos(t / 3)))
        //corner2.lightCol = Vector3f(abs(cos(t / 1)), abs(cos(t / 2)), abs(cos(t / 3)))
        //corner1.lightCol = Vector3f(abs(cos(t / 1)), abs(cos(t / 2)), abs(cos(t / 3)))
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

    fun onMouseMove(xpos: Double, ypos: Double) {
        val deltaX = xpos - oldMousePosX
        val deltaY = ypos - oldMousePosY

        oldMousePosX = xpos
        oldMousePosY = ypos

        if (bool) {
            camera.rotateAroundPoint(
                0f,
                Math.toRadians(deltaX.toFloat() * -0.05f),
                0f,
                Vector3f(0f)
            )

            //camera.rotateAroundPoint(
            //    Math.toRadians(deltaY.toFloat() * -0.05f),
            //    0f,
            //    0f,
            //    Vector3f(0f)
            //)
        }
        bool = true
    }

    fun cleanup() {}
}
