package cga.exercise.components.texture

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.EXTTextureFilterAnisotropic
import org.lwjgl.opengl.GL30C
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer


/**
 * Created by Fabian on 16.09.2017.
 */
class Texture2D(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean) : ITexture {
    private var texID: Int = -1

    init {
        try {
            processTexture(imageData, width, height, genMipMaps)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        //create texture from file
        //don't support compressed textures for now
        //instead stick to pngs
        operator fun invoke(path: String, genMipMaps: Boolean): Texture2D {
            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            val readChannels = BufferUtils.createIntBuffer(1)
            //flip y coordinate to make OpenGL happy
            STBImage.stbi_set_flip_vertically_on_load(true)
            val imageData = STBImage.stbi_load(path, x, y, readChannels, 4)
                ?: throw Exception("Image file \"" + path + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())

            try {
                return Texture2D(imageData, x.get(), y.get(), genMipMaps)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                throw ex
            } finally {
                STBImage.stbi_image_free(imageData)
            }
        }
    }

    override fun processTexture(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean) {
        texID = GL30C.glGenTextures()
        GL30C.glBindTexture(GL30C.GL_TEXTURE_2D, texID)
        GL30C.glTexImage2D(
            GL30C.GL_TEXTURE_2D,
            0,
            GL30C.GL_RGBA8,
            width,
            height,
            0,
            GL30C.GL_RGBA,
            GL30C.GL_UNSIGNED_BYTE,
            imageData
        )
        if (genMipMaps) {
            GL30C.glGenerateMipmap(GL30C.GL_TEXTURE_2D)
        }
        unbind()
    }

    override fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int) {
        GL30C.glBindTexture(GL30C.GL_TEXTURE_2D, texID)
        GL30C.glTexParameteri(GL30C.GL_TEXTURE_2D, GL30C.GL_TEXTURE_WRAP_S, wrapS)
        GL30C.glTexParameteri(GL30C.GL_TEXTURE_2D, GL30C.GL_TEXTURE_WRAP_T, wrapT)
        GL30C.glTexParameteri(GL30C.GL_TEXTURE_2D, GL30C.GL_TEXTURE_MIN_FILTER, minFilter)
        GL30C.glTexParameteri(GL30C.GL_TEXTURE_2D, GL30C.GL_TEXTURE_MAG_FILTER, magFilter)

        GL30C.glTexParameterf(GL30C.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f)

        unbind()
    }

    override fun bind(textureUnit: Int) {
        GL30C.glActiveTexture(GL30C.GL_TEXTURE0 + textureUnit)
        GL30C.glBindTexture(GL30C.GL_TEXTURE_2D, texID)
    }

    override fun unbind() {
        GL30C.glBindTexture(GL30C.GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        unbind()
        if (texID != 0) {
            GL30C.glDeleteTextures(texID)
            texID = 0
        }
    }
}